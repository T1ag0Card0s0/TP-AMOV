
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:location/location.dart';

import 'data/Locations.dart';
import 'data/PlacesOfInterest.dart';

class PlacesOrderService {
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
        double distanceA = Locations.distanceCalculator(
          location.latitude!,
          location.longitude!,
          a['latitude'],
          a['longitude'],
        );

        double distanceB = Locations.distanceCalculator(
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