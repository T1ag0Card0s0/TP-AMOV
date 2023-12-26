import 'package:flutter/material.dart';
import 'package:tp2/RecentLocationManager.dart';
import 'package:tp2/data/PlacesOfInterest.dart';
import 'DetailsPlaceScreen.dart';
import 'PlacesOfInterestScreen.dart';


class RecentPlacesScreen extends StatefulWidget {
  const RecentPlacesScreen({super.key});
  static const String routeName = '/RecentPlacesScreen';

  @override
  State<RecentPlacesScreen> createState() => _RecentPlacesScreenState();
}

class _RecentPlacesScreenState extends State<RecentPlacesScreen> {
  String ?placeId;
  bool? _liked;

  @override
  Widget build(BuildContext context) {
    // Recupera os argumentos passados
    //final Locations location = ModalRoute.of(context)!.settings.arguments as Locations;
    //locationId = location.id;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Recent Places Of Interest'),
      ),
      body: Expanded(
            child: FutureBuilder<List<PlaceOfInterest>>(
              future: RecentLocationsManager.getRecentLocations(),
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
                                              Icons.thumb_up_alt_outlined)
                                      );
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
          )
      );
  }
}









