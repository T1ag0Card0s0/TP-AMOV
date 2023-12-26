import 'dart:convert';

import 'package:shared_preferences/shared_preferences.dart';

import 'data/PlacesOfInterest.dart';

class RecentLocationsManager {
  static const _maxRecentLocations = 10;

  static Future<void> addRecentLocation(PlaceOfInterest location) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();

    // Obter a lista atual de locais recentes do SharedPreferences
    List<String>? recentLocationsJson = prefs.getStringList('recentLocations');

    // Converter a lista de JSON para objetos PlaceOfInterest
    List<PlaceOfInterest> recentLocations = recentLocationsJson?.map((json) {
      Map<String, dynamic> map = jsonDecode(json);
      return PlaceOfInterest.fromJson(map);
    }).toList() ?? [];

    // Verificar se o local já está na lista
    if (recentLocations.any((element) => element.id == location.id)) {
      return; // Não adiciona se o local já estiver presente
    }

    // Adicionar o novo local à lista
    recentLocations.insert(0, location);

    // Garantir que não ultrapasse o limite de 10 locais recentes
    if (recentLocations.length > _maxRecentLocations) {
      recentLocations = recentLocations.sublist(0, _maxRecentLocations);
    }

    // Converter a lista de objetos PlaceOfInterest para JSON
    List<String> recentLocationsJsonUpdated =
    recentLocations.map((location) => jsonEncode(location.toJson())).toList();

    // Salvar a lista atualizada no SharedPreferences
    prefs.setStringList('recentLocations', recentLocationsJsonUpdated);
  }

  static Future<List<PlaceOfInterest>> getRecentLocations() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();

    // Obter a lista atual de locais recentes do SharedPreferences
    List<String>? recentLocationsJson = prefs.getStringList('recentLocations');

    // Converter a lista de JSON para objetos PlaceOfInterest
    List<PlaceOfInterest> recentLocations = recentLocationsJson?.map((json) {
      Map<String, dynamic> map = jsonDecode(json);
      return PlaceOfInterest.fromJson(map);
    }).toList() ?? [];

    return recentLocations;
  }
}
