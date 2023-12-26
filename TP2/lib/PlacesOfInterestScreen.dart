import 'dart:async';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:location/location.dart';
import 'package:tp2/RecentLocationManager.dart';
import 'package:tp2/data/PlacesOfInterest.dart';
import 'package:tp2/data/Categories.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'DetailsPlaceScreen.dart';
import 'data/Locations.dart';

class PlacesOfInterestScreen extends StatefulWidget {
  const PlacesOfInterestScreen({super.key});
  static const String routeName = '/PlacesOfInterestScreen';

  @override
  State<PlacesOfInterestScreen> createState() => _PlacesOfInterestScreenState();
}


class PlacesService {
  Future<List<PlaceOfInterest>> getPlaces(String? orderBy, String? category, String locationId, LocationData location) async {
    var db = FirebaseFirestore.instance;

    QuerySnapshot<Map<String, dynamic>> collection = await db.collection('PlacesOfInterest').get();

    // Adiciona a lógica de ordenação
    if (orderBy == 'Alphabetical Asc (A -> Z)') {
      collection = await db.collection('PlacesOfInterest').orderBy('name').get();
    } else if (orderBy == 'Alphabetical Desc (Z -> A)') {
      collection = await db.collection('PlacesOfInterest').orderBy('name', descending: true).get();
    } else if (orderBy == 'Distance') {
      collection.docs.sort((a, b) {
        double distanceA = Locations.distanceCalculater(
          location.latitude!,
          location.longitude!,
          a['latitude'],
          a['longitude'],
        );

        double distanceB = Locations.distanceCalculater(
          location.latitude!,
          location.longitude!,
          b['latitude'],
          b['longitude'],
        );

        return distanceA.compareTo(distanceB);
      });
    }

    List<PlaceOfInterest> locations = [];
    for (var doc in collection.docs) {
      Map<String, dynamic> data = doc.data();
      if(data['locationId'] == locationId ){
        if(data['categoryId'] == category || category == null) {

          locations.add(PlaceOfInterest(
              id: doc.id,
              name: data['name'],
              authorEmail: data['authorEmail'],
              imageName: data['imageName'],
              imageUri: data['imageUri'],
              latitude: data['latitude'],
              longitude: data['longitude'],
              categoryId: data['categoryId'],
              locationId: data['locationId']
          ));
        }
      }
    }
    return locations;
  }
}

class PreferenceManager {
  static Future<SharedPreferences> _getPrefs() async {
    return await SharedPreferences.getInstance();
  }

  static Future<bool?> getLikeStatus(String placeId) async {
    final SharedPreferences prefs = await _getPrefs();
    return prefs.getBool('$placeId-liked');
  }

  static Future<bool> setLikeStatus(String placeId, bool liked) async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    return await prefs.setBool('$placeId-liked', liked);
  }
}

class _PlacesOfInterestScreenState extends State<PlacesOfInterestScreen> {
  // Location
  Location currentLocation = Location();

  bool _serviceEnabled = false;
  PermissionStatus _permissionGranted = PermissionStatus.denied;
  LocationData _locationData = LocationData.fromMap({
    "latitude": 40.192639,
    "longitude": -8.411899,
  });

  void getLocation() async {
    _serviceEnabled = await currentLocation.serviceEnabled();
    if (!_serviceEnabled) {
      _serviceEnabled = await currentLocation.requestService();
      if (!_serviceEnabled) {
        return;
      }
    }

    _permissionGranted = await currentLocation.hasPermission();
    if (_permissionGranted == PermissionStatus.denied) {
      _permissionGranted = await currentLocation.requestPermission();
      if (_permissionGranted != PermissionStatus.granted) {
        return;
      }
    }
    _locationData = await currentLocation.getLocation();
    setState(() {});
  }

  // end Location

  static const List<String> orderOptions = [
    'Alphabetical Asc (A -> Z)',
    'Alphabetical Desc (Z -> A)',
    'Distance',
  ];

  final PlacesService _placesService = PlacesService();
  final CategoryService _categoryService = CategoryService();
  late List<Categories> _categories = [];
  String ?_selectedCategory;
  String locationId = "";
  String ?placeId;
  String ?orderByValue ;
  bool? _liked;

  @override
  Widget build(BuildContext context) {
    // Recupera os argumentos passados
    final Locations location = ModalRoute.of(context)!.settings.arguments as Locations;
    locationId = location.id;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Places Of Interest'),
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          // Row with Dropdowns
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              children: [
                // Dropdown for Order By
                Expanded(
                  child: DropdownButton<String>(
                    hint: const Text('Order by',
                        style: TextStyle(color: Colors.black)),
                    value: null,
                    icon: const Icon(Icons.arrow_downward),
                    elevation: 16,
                    style: const TextStyle(color: Colors.black),
                    underline: Container(
                      height: 2,
                      color: Colors.amber,
                    ),
                    onChanged: (String? value) {
                      // Atualiza o valor do Dropdown e chama a função de atualização
                      setState(() {
                        orderByValue = value!;
                        _updatePlaces();
                      });
                    },
                    items: orderOptions.map<DropdownMenuItem<String>>((String value) {
                      return DropdownMenuItem<String>(
                        value: value,
                        child: Text(value),
                      );
                    }).toList(),
                  ),
                ),
                const SizedBox(width: 16.0), // Add spacing between dropdowns
                // Dropdown for Categories
                Expanded(
                  child: FutureBuilder<List<Categories>>(
                    future: _categoryService.getCategories(),
                    builder: (context, snapshot) {
                      if (!snapshot.hasData || snapshot.data!.isEmpty) {
                        return const Text('No categories available.');
                      } else {
                        _categories = snapshot.data!;
                        return DropdownButton<String>(
                          hint: const Text('Select a category',
                              style: TextStyle(color: Colors.black)),
                          value: null,
                          icon: const Icon(Icons.arrow_downward),
                          elevation: 16,
                          style: const TextStyle(color: Colors.black),
                          underline: Container(
                            height: 2,
                            color: Colors.amber,
                          ),
                          onChanged: (newValue) {
                            setState(() {
                              _selectedCategory = newValue!;
                              _updatePlaces();
                            });
                          },
                          items: _categories.map((category) {
                            return DropdownMenuItem<String>(
                              value: category.id,
                              child: Text(category.name),
                            );
                          }).toList(),
                        );
                      }
                    },
                  ),
                ),
              ],
            ),
          ),
          // Lista de Localizações
          Expanded(
            child: FutureBuilder<List<PlaceOfInterest>>(
              future: _placesService.getPlaces(orderByValue, _selectedCategory, location.id, _locationData),
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting) {
                  return const Center(child: CircularProgressIndicator());
                } else if (snapshot.hasError) {
                  return Text('Error: ${snapshot.error}');
                } else {
                  List<PlaceOfInterest> locations = snapshot.data!;
                  return ListView(
                    children: locations.map((location) => Card(
                      child: GestureDetector(
                        child: ListTile(
                          title: Column(
                            children: [
                              // Image widget placed above the Text
                              Image.network(
                                location.imageUri,
                                width: double.infinity,
                                fit: BoxFit.cover,
                                loadingBuilder: (BuildContext context, Widget child, ImageChunkEvent? loadingProgress) {
                                  if (loadingProgress == null) {
                                    return child;
                                  } else {
                                    return Center(
                                      child: CircularProgressIndicator(
                                        value: loadingProgress.expectedTotalBytes != null
                                            ? loadingProgress.cumulativeBytesLoaded / (loadingProgress.expectedTotalBytes ?? 1)
                                            : null,
                                      ),
                                    );
                                  }
                                },
                              ),
                            ],
                          ),
                          subtitle: Row(
                            children: [
                              Padding(
                                padding: const EdgeInsets.all(8.0),
                                child: Text(
                                  location.name,
                                  style: const TextStyle(
                                    fontSize: 19.0,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                              ),
                              const Spacer(),
                              FutureBuilder<bool?>(
                                future: PreferenceManager.getLikeStatus(location.id),
                                builder: (context, snapshot) {
                                   if (snapshot.hasError) {
                                    // Lidar com erro, se houver
                                    return Text('Erro: ${snapshot.error}');
                                  } else {
                                     // Usar o valor retornado
                                     bool? liked = snapshot.data;
                                     if (liked == null) {
                                       return IconButton(
                                           onPressed: () {
                                             setState(() {
                                               _liked = true;
                                               placeId = location.id;
                                               PreferenceManager.setLikeStatus(
                                                   placeId!, _liked!);
                                             });
                                           },
                                           icon: const Icon(
                                               Icons.thumb_up_alt_outlined));
                                     } else {
                                       return IconButton(
                                         icon: Icon(
                                           liked ? Icons.thumb_up : Icons
                                               .thumb_up_alt_outlined,
                                           color: liked ? Colors.green : null,
                                         ),
                                         onPressed: () {
                                           setState(() {
                                             _liked = true;
                                             placeId = location.id;
                                             PreferenceManager.setLikeStatus(
                                                 placeId!, _liked!);
                                           });
                                         },
                                       );
                                     }
                                   }
                                },
                              ),
                              FutureBuilder<bool?>(
                                future: PreferenceManager.getLikeStatus(location.id),
                                builder: (context, snapshot) {
                                  if (snapshot.hasError) {
                                    // Lidar com erro, se houver
                                    return Text('Erro: ${snapshot.error}');
                                  } else {
                                    // Usar o valor retornado
                                    bool? liked = snapshot.data;
                                    if (liked == null) {
                                      return IconButton(
                                          onPressed: () {
                                            setState(() {
                                              _liked = false;
                                              placeId = location.id;
                                              PreferenceManager.setLikeStatus(
                                                  placeId!, _liked!);
                                            });
                                          },
                                          icon: const Icon(
                                              Icons.thumb_down_alt_outlined));
                                    } else {
                                      return IconButton(
                                        icon: Icon(
                                          !liked ? Icons.thumb_down : Icons
                                              .thumb_down_alt_outlined,
                                          color: !liked ? Colors.red : null,
                                        ),
                                        onPressed: () {
                                          setState(() {
                                            _liked = false;
                                            placeId = location.id;
                                            PreferenceManager.setLikeStatus(
                                                placeId!, _liked!);
                                          });
                                        },
                                      );
                                    }
                                  }
                                },
                              ),
                              Hero(
                                tag: "btnDetails",
                                child: IconButton(
                                  icon: const Icon(Icons.more_vert), // Ícone de três pontos,
                                  onPressed: () async {
                                    setState(() async {
                                      RecentLocationsManager.addRecentLocation(location);
                                      await Navigator.pushNamed(
                                          context, DetailsPlaceScreen.routeName,
                                          arguments: location
                                      );
                                    });
                                  },
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                    )).toList(),
                  );
                }
              },
            ),
          ),
        ],
      ),
    );
  }
  // Função para atualizar a lista de localizações com base no Dropdown
  void _updatePlaces() {
    _placesService.getPlaces(orderByValue, _selectedCategory, locationId, _locationData);
  }
}









