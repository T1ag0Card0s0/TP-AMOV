import 'dart:math' as Math;
import 'dart:math';

class Locations {
  final String id;
  final String name;
  final String description;
  final String imageUri;
  final double latitude;
  final double longitude;
  final String? user1;
  final String? user2;

  Locations({
    required this.id,
    required this.name,
    required this.description,
    required this.imageUri,
    required this.latitude,
    required this.longitude,
    required this.user1,
    required this.user2
  });

  factory Locations.fromMap(Map<String, dynamic> data, String documentId) {
    return Locations(
      id: documentId,
      name: data['name'] ?? '',
      description: data['description'] ?? '',
      imageUri: data['image'] ?? '',
      latitude: (data['latitude'] ?? 0.0) is double ? data['latitude'] : double.parse(data['latitude'] ?? '0'),
      longitude: (data['longitude'] ?? 0.0) is double ? data['longitude'] : double.parse(data['longitude'] ?? '0'),
      user1: data['user1'],
      user2: data['user2']
    );
  }

  static bool isApproved(Locations location){
    return (location.user1 != null  && location.user2 != null);
  }

  static const double earthRadiusKm = 6371.0;

  static double degreesToRadians(double degrees) {
    return degrees * (Math.pi / 180.0);
  }

  static double distanceCalculator(double lat1, double lon1, double lat2, double lon2) {
    const earthRadius = 6371.0; // Raio da Terra em quilômetros

    // Converte as latitudes e longitudes de graus para radianos
    final lat1Rad = degreesToRadians(lat1);
    final lon1Rad = degreesToRadians(lon1);
    final lat2Rad = degreesToRadians(lat2);
    final lon2Rad = degreesToRadians(lon2);

    // Calcula as diferenças de latitude e longitude
    final dLat = lat2Rad - lat1Rad;
    final dLon = lon2Rad - lon1Rad;

    // Fórmula de Haversine para calcular a distância
    final a = (sin(dLat / 2) * sin(dLat / 2)) +
        cos(lat1Rad) * cos(lat2Rad) * (sin(dLon / 2) * sin(dLon / 2));
    final c = 2 * atan2(sqrt(a), sqrt(1 - a));
    final distance = earthRadius * c;

    return distance;
  }



}

