
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:tp2/data/PlacesOfInterest.dart';

import 'DetailsScreen.dart';
import 'data/Location.dart';

class PlacesOfInterestScreen extends StatefulWidget {
  const PlacesOfInterestScreen({super.key});

  static const String routeName = '/PlacesOfInterestScreen';

  @override
  State<PlacesOfInterestScreen> createState() => _PlacesOfInterestScreenState();
}

class _PlacesOfInterestScreenState extends State<PlacesOfInterestScreen> {
  @override
  Widget build(BuildContext context) {
    // Recupera os argumentos passados
    final Location location = ModalRoute.of(context)!.settings.arguments as Location;
    final PlacesService _placesService = PlacesService();

    return Scaffold(
      appBar: AppBar(
        title: Text('Places Of Interest'),
      ),
      body: Expanded(
        child: FutureBuilder<List<PlaceOfInterest>>(
          future: _placesService.getPlaces("", "", location.id),
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return CircularProgressIndicator();
            } else if (snapshot.hasError) {
              return Text('Error: ${snapshot.error}');
            } else {
              List<PlaceOfInterest> locations = snapshot.data!;
              return ListView(
                children: locations.map((location) => Card(
                  child:GestureDetector(
                    child: ListTile(
                      title: Column(
                        children: [
                          // Image widget placed above the Text
                          Image.network(
                            location.imageUri, // Replace with the URL of the image
                            width: double.infinity, // Adjust the width as needed
                            height: 100, // Adjust the height as needed
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
                            child: Text(location.name,
                              style: const TextStyle(
                              fontSize: 18, // Adjust the font size as needed
                              //fontWeight: FontWeight.bold, // Adjust the font weight as needed
                              color: Colors.black, // Adjust the text color as needed
                            ),),
                          ),
                          Spacer(),
                          Hero(
                            tag: "btnIcon",
                            child: IconButton(
                              icon: Icon(Icons.more_vert), // Ícone de três pontos,
                              onPressed: () async {
                                var obj = await Navigator.pushNamed(
                                    context, DetailsScreen.routeName,
                                    arguments: location
                                );
                              },
                            ),
                          )
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
    );
  }
}


class PlacesService {
  Future<List<PlaceOfInterest>> getPlaces(String orderBy, String searchTerm, String locationId) async {
    var db = FirebaseFirestore.instance;

    QuerySnapshot<Map<String, dynamic>> collection = await db.collection('PlacesOfInterest').get();

    // Adiciona a lógica de ordenação
    if (orderBy == 'Alphabetical Asc (A -> Z)') {
      collection = await db.collection('Locations').orderBy('name').get();
    } else if (orderBy == 'Alphabetical Desc (Z -> A)') {
      collection = await db.collection('Locations').orderBy('name', descending: true).get();
    } else if (orderBy == 'Distance') {
      // Lógica para ordenar por distância, se aplicável
    }

    List<PlaceOfInterest> locations = [];
    for (var doc in collection.docs) {
      Map<String, dynamic> data = doc.data();

      // Adiciona a lógica de pesquisa
      if(data['locationId'] == locationId) {
        if (searchTerm.isEmpty || searchTerm == "" ||
            data['name'].toString().toLowerCase() == searchTerm.toLowerCase()) {
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
