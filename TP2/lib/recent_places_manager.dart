import 'dart:convert';

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'data/PlacesOfInterest.dart';

class RecentLocationsManager {
  static const _maxRecentLocations = 10;

  static Future<void> addRecentLocation(PlaceOfInterest location) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();

    List<String>? recentLocationsJson = prefs.getStringList('recentLocations');

    List<PlaceOfInterest> recentLocations = recentLocationsJson?.map((json) {
      Map<String, dynamic> map = jsonDecode(json);
      return PlaceOfInterest.fromJson(map);
    }).toList() ?? [];

    if (recentLocations.any((element) => element.id == location.id)) {
      return;
    }

    recentLocations.insert(0, location);

    if (recentLocations.length > _maxRecentLocations) {
      recentLocations = recentLocations.sublist(0, _maxRecentLocations);
    }

    List<String> recentLocationsJsonUpdated =
    recentLocations.map((location) => jsonEncode(location.toJson())).toList();

    prefs.setStringList('recentLocations', recentLocationsJsonUpdated);
  }

  static Future<List<PlaceOfInterest>> getRecentLocations() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();

    List<String>? recentLocationsJson = prefs.getStringList('recentLocations');

    List<PlaceOfInterest> recentLocations = recentLocationsJson?.map((json) {
      Map<String, dynamic> map = jsonDecode(json);
      return PlaceOfInterest.fromJson(map);
    }).toList() ?? [];

    await checkIfLocationsExistOnFirebase(recentLocations);

    await prefs.setStringList('recentLocations', recentLocations
        .map((location) => jsonEncode(location.toJson()))
        .toList());

    return recentLocations;
  }
  static Future<void> checkIfLocationsExistOnFirebase(List<PlaceOfInterest> locations) async
  {
    CollectionReference placesCollection = FirebaseFirestore.instance.collection('PlacesOfInterest');

    for (int i = 0; i < locations.length; i++) {
      PlaceOfInterest location = locations[i];
      QuerySnapshot querySnapshot = await placesCollection
          .where('name', isEqualTo: location.name)
          .get();

      if (querySnapshot.docs.isEmpty) {
        locations.removeAt(i);
      }
    }
  }
}
