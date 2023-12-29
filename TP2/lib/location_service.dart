import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:location/location.dart';
import 'data/Locations.dart';

class LocationService {
  Future<List<Locations>> getLocations(String? orderBy, String? searchTerm, LocationData location) async {
    var db = FirebaseFirestore.instance;

    QuerySnapshot<Map<String, dynamic>> collection = await db.collection('Locations').get();

    List<Locations> locations = collection.docs.map((doc) {
      Map<String, dynamic> data = doc.data();
      return Locations(
        id: doc.id,
        name: data['name'],
        description: data['description'],
        imageUri: data['imageUri'],
        latitude: data['latitude'],
        longitude: data['longitude'],
        user1: data['user1'],
        user2: data['user2']
      );
    }).toList();

    // Adiciona a lógica de ordenação
    if (orderBy == 'Alphabetical Asc (A -> Z)') {
      locations.sort((a, b) => a.name.compareTo(b.name));
    } else if (orderBy == 'Alphabetical Desc (Z -> A)') {
      locations.sort((a, b) => b.name.compareTo(a.name));
    } else if (orderBy == 'Distance') {
      locations.sort((a, b) {
        double distanceA = Locations.distanceCalculator(
          location.latitude!,
          location.longitude!,
          a.latitude,
          a.longitude,
        );

        double distanceB = Locations.distanceCalculator(
          location.latitude!,
          location.longitude!,
          b.latitude,
          b.longitude,
        );

        return distanceA.compareTo(distanceB);
      });
    }

    // Adiciona a lógica de pesquisa
    if (searchTerm != null && searchTerm.isNotEmpty) {
      locations = locations.where((loc) => loc.name.toLowerCase() == searchTerm.toLowerCase()).toList();
    }

    return locations;
  }

  LocationService();
}