import 'dart:math' as Math;

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
    required this.longitude,
  });

  factory Locations.fromMap(Map<String, dynamic> data, String documentId) {
    return Locations(
      id: documentId,
      name: data['name'] ?? '',
      description: data['description'] ?? '',
      imageUri: data['image'] ?? '',
      latitude: (data['latitude'] ?? 0.0) is double ? data['latitude'] : double.parse(data['latitude'] ?? '0'),
      longitude: (data['longitude'] ?? 0.0) is double ? data['longitude'] : double.parse(data['longitude'] ?? '0'),
    );
  }

  static const double earthRadiusKm = 6371.0;

  static double degreesToRadians(double degrees) {
    return degrees * (Math.pi / 180.0);
  }

  static double distanceCalculator(double lat1, double lon1, double lat2, double lon2) {
    double dLat = degreesToRadians(lat2 - lat1);
    double dLon = degreesToRadians(lon2 - lon1);

    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(degreesToRadians(lat1)) *
            Math.cos(degreesToRadians(lat2)) *
            Math.sin(dLon / 2) *
            Math.sin(dLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return earthRadiusKm * c;
  }
}