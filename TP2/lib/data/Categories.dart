
import 'package:cloud_firestore/cloud_firestore.dart';

class Categories {
  final String id;
  final String authorEmail;
  final String name;
  final String description;
  final String iconName;

  Categories({
    required this.id,
    required this.name,
    required this.description,
    required this.authorEmail,
    required this.iconName,
  });

  factory Categories.fromMap(Map<String, dynamic> data, String documentId) {
    return Categories(
      id: documentId,
      name: data['name'] ?? '',
      iconName: data['iconName'] ?? '',
      description: data['description'] ?? '',
      authorEmail: data['authorEmail'] ?? '',
    );
  }
}

