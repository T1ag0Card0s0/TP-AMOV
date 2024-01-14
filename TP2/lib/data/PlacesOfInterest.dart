class PlaceOfInterest {
  final String id;
  final String authorEmail;
  final String name;
  final String imageName;
  final String categoryId;
  final String locationId;
  final String imageUri;
  final double latitude;
  final double longitude;
  final String? user1;
  final String? user2;

  PlaceOfInterest({
    required this.id,
    required this.authorEmail,
    required this.name,
    required this.imageName,
    required this.categoryId,
    required this.locationId,
    required this.imageUri,
    required this.latitude,
    required this.longitude,
    required this.user1,
    required this.user2
  });

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'authorEmail': authorEmail,
      'imageName': imageName,
      'imageUri': imageUri,
      'latitude': latitude,
      'longitude': longitude,
      'categoryId': categoryId,
      'locationId': locationId,
      'user1' : user1,
      'user2' : user2
    };
  }

  factory PlaceOfInterest.fromJson(Map<String, dynamic> json) {
    return PlaceOfInterest(
      id: json['id'],
      name: json['name'],
      authorEmail: json['authorEmail'],
      imageName: json['imageName'],
      imageUri: json['imageUri'],
      latitude: (json['latitude'] ?? 0.0) is double ? json['latitude'] : double.parse(json['latitude'] ?? '0'),
      longitude: (json['longitude'] ?? 0.0) is double ? json['longitude'] : double.parse(json['longitude'] ?? '0'),
      categoryId: json['categoryId'],
      locationId: json['locationId'],
      user1: json['user1'],
      user2: json['user2']
    );
  }

  static bool isApproved(PlaceOfInterest location){
    return (location.user1 != null  && location.user2 != null);
  }
}
