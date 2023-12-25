import 'package:shared_preferences/shared_preferences.dart';

class PreferenceManager {
  static Future<SharedPreferences> _getPrefs() async {
    return await SharedPreferences.getInstance();
  }

  static Future<bool?> getLikeStatus(String placeId) async {
    final SharedPreferences prefs = await _getPrefs();
    return prefs.getBool('$placeId-liked');
  }

  static Future<bool> setLikeStatus(String placeId, bool liked) async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    return await prefs.setBool('$placeId-liked', liked);
  }
}