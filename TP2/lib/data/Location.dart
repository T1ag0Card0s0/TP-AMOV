
class Location {
  final String id;
  final String name;
  final String description;
  final String imageUri;
  final double latitude;
  final double longitude;

  Location({
    required this.id,
    required this.name,
    required this.description,
    required this.imageUri,
    required this.latitude,
    required this.longitude
  });

  factory Location.fromMap(Map<String, dynamic> data, String documentId) {
    return Location(
      id: documentId,
      name: data['name'] ?? '',
      description: data['description'] ?? '',
      imageUri: data['image'] ?? '',
      latitude: data['latitude'] ?? '',
      longitude: data['longitude'] ?? ''
    );
  }
}