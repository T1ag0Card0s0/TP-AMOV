
import 'package:cloud_firestore/cloud_firestore.dart';

class PlaceOfInterest{
  final String id;
  final String authorEmail;
  final String name;
  final String imageName;
  final String categoryId;
  final String locationId;
  final String imageUri;
  final double latitude;
  final double longitude;

  PlaceOfInterest({
    required this.id,
    required this.authorEmail,
    required this.name,
    required this.imageName,
    required this.categoryId,
    required this.locationId,
    required this.imageUri,
    required this.latitude,
    required this.longitude
});

  factory PlaceOfInterest.fromMap(Map<String, dynamic> data, String documentId) {
    return PlaceOfInterest(
        id: documentId,
        name: data['name'] ?? '',
        authorEmail: data['authorEmail'] ?? '',
        imageName: data['imageName'] ?? '',
        categoryId: data['categoryId'] ?? '',
        locationId: data['locationId'] ?? '',
        imageUri: data['imageUri'] ?? '',
        latitude: data['latitude'] ?? '',
        longitude: data['longitude'] ?? ''
    );
  }
}

