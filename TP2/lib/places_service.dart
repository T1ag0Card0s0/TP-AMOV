
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:location/location.dart';

import 'data/Locations.dart';
import 'data/PlacesOfInterest.dart';

class PlacesService {

  // get places correspondentes ao id da localizacao passado
  Future<List<PlaceOfInterest>> getPlacesWithoutOrder(String locationId) async {
    var db = FirebaseFirestore.instance;

    QuerySnapshot<Map<String, dynamic>> collection = await db.collection(
        'PlacesOfInterest').get();

    List<PlaceOfInterest> locations = [];
    for (var doc in collection.docs) {
      Map<String, dynamic> data = doc.data();
      if (data['locationId'] == locationId) {
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
    return locations;
  }

 // order by ..
  Future<List<PlaceOfInterest>> getPlaces(String? orderBy, String? category, String locationId, LocationData location) async {
    var db = FirebaseFirestore.instance;

    QuerySnapshot<Map<String, dynamic>> collection = await db.collection('PlacesOfInterest').get();

    List<PlaceOfInterest> places = collection.docs.map((doc) {
      Map<String, dynamic> data = doc.data();
      return PlaceOfInterest(
        id: doc.id,
        name: data['name'],
        authorEmail: data['authorEmail'],
        imageName: data['imageName'],
        imageUri: data['imageUri'],
        latitude: data['latitude'],
        longitude: data['longitude'],
        categoryId: data['categoryId'],
        locationId: data['locationId'],
      );
    }).toList();

    // Adiciona a lógica de ordenação
    if (orderBy == 'Alphabetical Asc (A -> Z)') {
      places.sort((a, b) => a.name.compareTo(b.name));
    } else if (orderBy == 'Alphabetical Desc (Z -> A)') {
      places.sort((a, b) => b.name.compareTo(a.name));
    } else if (orderBy == 'Distance') {
      places.sort((a, b) {
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

    // Adiciona a lógica de filtragem por categoria e localização
    places = places.where((place) => place.locationId == locationId).toList();
    if (category != null) {
      places = places.where((place) => place.categoryId == category).toList();
    }

    return places;
  }

}