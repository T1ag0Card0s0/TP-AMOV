
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
class Location {
  final String id;
  final String name;
  final String description;
  final String imagePath;

  Location({
    required this.id,
    required this.name,
    required this.description,
    required this.imagePath,
  });

  factory Location.fromMap(Map<String, dynamic> data, String documentId) {
    return Location(
      id: documentId,
      name: data['name'] ?? '',
      description: data['description'] ?? '',
      imagePath: data['image'] ?? '',
    );
  }
}
class LocationService {
  Future<List<Location>> getLocations(String orderBy) async {
    var db = FirebaseFirestore.instance;
    var collection = await db.collection('Locations').orderBy(orderBy).get();
    List<Location> locations = [];
    for(var doc in collection.docs) {
      Map<String, dynamic> data = doc.data() as Map<String, dynamic>;

      locations.add(Location(
        id: doc.id,
        name: data['name'],
        description: data['description'],
        imagePath: data['imagePath'],
      ));
    }
    return locations;
  }
}

class ListScreen extends StatefulWidget {
  const ListScreen({super.key});

  static const String routeName = '/ListScreen';

  @override
  State<ListScreen> createState() => _ListScreenState();
}

class _ListScreenState extends State<ListScreen> {

  @override
  Widget build(BuildContext context) {
    final LocationService _locationService = LocationService();
    String orderByValue = 'name'; // Valor padrão para ordenação
    return Scaffold(
      appBar: AppBar(
        title: const Hero(tag: "btnSecond", child: Text('ListScreen')),
        backgroundColor: Colors.amber[100],
        actions: [
          // Dropdown para selecionar o critério de ordenação
          DropdownButton<String>(
            value: orderByValue,
            onChanged: (String ?value) {
              setState(() {
                orderByValue = value!;
              });
            },
            items: ['name', 'description', 'id']
                .map((String value) {
              return DropdownMenuItem<String>(
                value: value,
                child: Text(value),
              );
            })
                .toList(),
          ),
        ],
      ),
      body: FutureBuilder<List<Location>>(
        future: _locationService.getLocations(orderByValue),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return CircularProgressIndicator();
          } else if (snapshot.hasError) {
            return Text('Error: ${snapshot.error}');
          } else {
            List<Location> locations = snapshot.data!;

            return Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                // Mostrar cards das localizações
                Column(
                  children: locations
                      .map(
                        (location) => Card(
                      child: Column(
                        children: [
                          Image.network(location.imagePath),
                          Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Text(location.name),
                          ),
                        ],
                      ),
                    ),
                  )
                      .toList(),
                ),
              ],
            );
          }
        },
      ),
    );
  }
}


