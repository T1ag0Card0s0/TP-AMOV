
class Locations {
  final String id;
  final String name;
  final String description;
  final String imageUri;
  final double latitude;
  final double longitude;

  Locations({
    required this.id,
    required this.name,
    required this.description,
    required this.imageUri,
    required this.latitude,
    required this.longitude
  });

  factory Locations.fromMap(Map<String, dynamic> data, String documentId) {
    return Locations(
      id: documentId,
      name: data['name'] ?? '',
      description: data['description'] ?? '',
      imageUri: data['image'] ?? '',
      latitude: data['latitude'] ?? '',
      longitude: data['longitude'] ?? ''
    );
  }
}