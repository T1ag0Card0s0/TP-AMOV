
import 'package:cloud_firestore/cloud_firestore.dart';

import 'data/Categories.dart';

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
        iconName: data['iconName'],
      ));
    }
    return categories;
  }

}