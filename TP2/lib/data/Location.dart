class Location {
  final String id;
  final String name;
  final String description;
  final String imagePath;

  Location({
    required this.id,
    required this.name,
    required this.description,
    required this.imagePath,
  });

  factory Location.fromMap(Map<String, dynamic> data, String documentId) {
    return Location(
      id: documentId,
      name: data['name'] ?? '',
      description: data['description'] ?? '',
      imagePath: data['image'] ?? '',
    );
  }
}