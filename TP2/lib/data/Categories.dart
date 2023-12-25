
import 'package:cloud_firestore/cloud_firestore.dart';

class Categories {
  final String id;
  final String authorEmail;
  final String name;
  final String description;

  Categories({
    required this.id,
    required this.name,
    required this.description,
    required this.authorEmail,
  });

  factory Categories.fromMap(Map<String, dynamic> data, String documentId) {
    return Categories(
      id: documentId,
      name: data['name'] ?? '',
      description: data['description'] ?? '',
      authorEmail: data['authorEmail'] ?? '',
    );
  }
}


class CategoryService {
  Future<List<Categories>> getCategories() async {
    var db = FirebaseFirestore.instance;

    QuerySnapshot<Map<String, dynamic>> collection =
    await db.collection('Categories').get();

    List<Categories> categories = [];
    for (var doc in collection.docs) {
      Map<String, dynamic> data = doc.data();
      categories.add(Categories(
        id: doc.id,
        name: data['name'],
        description: data['description'],
        authorEmail: data['authorEmail'],
      ));
    }
    return categories;
  }
}