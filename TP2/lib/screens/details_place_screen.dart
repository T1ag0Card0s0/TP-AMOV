
import 'package:flutter/material.dart';
import 'package:flutter_osm_plugin/flutter_osm_plugin.dart';
import 'package:tp2/data/PlacesOfInterest.dart';

class DetailsPlaceScreen extends StatefulWidget {
  const DetailsPlaceScreen({super.key});

  static const String routeName = '/DetailsPlaceScreen';

  @override
  State<DetailsPlaceScreen> createState() => _DetailsPlaceScreenState();
}

class _DetailsPlaceScreenState extends State<DetailsPlaceScreen> {
  late MapController mapController;
  @override
  Widget build(BuildContext context) {
    // Recupera os argumentos passados
    final PlaceOfInterest location = ModalRoute.of(context)!.settings.arguments as PlaceOfInterest;
    mapController = MapController(
      initPosition: GeoPoint(
        latitude: location.latitude,
        longitude: location.longitude,
      ),
      areaLimit: BoundingBox(
        east: 10.4922941,
        north: 47.8084648,
        south: 45.817995,
        west: 5.9559113,
      ),
    );
    return Scaffold(
      appBar: AppBar(
        title: const Text('Details'),
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
            Card(
            elevation: 8, // Set the elevation as desired
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                // Imagem da localização
                Image.network(
                  location.imageUri,
                  height: 200.0,
                  fit: BoxFit.cover,
                  loadingBuilder: (BuildContext context, Widget child, ImageChunkEvent? loadingProgress) {
                    if (loadingProgress == null) {
                      return child;
                    } else {
                      return Center(
                        child: CircularProgressIndicator(
                          value: loadingProgress.expectedTotalBytes != null
                              ? loadingProgress.cumulativeBytesLoaded / (loadingProgress.expectedTotalBytes ?? 1)
                              : null,
                        ),
                      );
                    }
                  },
                ),
                // Nome da localização
                Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Text(
                    location.name,
                    style: const TextStyle(
                      fontSize: 24.0,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                // Descrição da localização
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 16.0),
                  child: Text("Author: ${location.authorEmail}",
                    style: const TextStyle(
                      fontSize: 16.0,
                    ),
                  ),
                ),
                // Latitude e longitude
                Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 16.0),
                  child: Text(
                    'Latitude: ${location.latitude.toStringAsFixed(6)}, Longitude: ${location.longitude.toStringAsFixed(6)}',
                    style: const TextStyle(fontSize: 16.0),
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(
            height: 20,
          ),
          SizedBox(
            height: 400,
            child: Stack(
              children: [
                OSMFlutter(
                  controller: mapController,
                  osmOption: OSMOption(
                    zoomOption: const ZoomOption(
                      initZoom: 12,
                      minZoomLevel: 3,
                      maxZoomLevel: 19,
                      stepZoom: 1.0,
                    ),
                    roadConfiguration: const RoadOption(
                      roadColor: Colors.yellowAccent,
                    ),
                    markerOption: MarkerOption(
                      defaultMarker: const MarkerIcon(
                        icon: Icon(
                          Icons.person_pin_circle,
                          color: Colors.blue,
                          size: 56,
                        ),
                      ),
                    ),
                  ),
                  mapIsLoading: const Center(
                    child: CircularProgressIndicator(),
                  ),
                  onMapIsReady: (isReady) async {
                    if (isReady) {
                      await mapController.addMarker(
                          GeoPoint(
                              latitude: location.latitude,
                              longitude: location.longitude),
                          markerIcon: const MarkerIcon(
                            icon: Icon(
                              Icons.location_on,
                              color: Colors.blue,
                              size: 56, // Ajuste o tamanho conforme necessário
                            ),
                          )
                      );
                    }
                  }
                ),
                // Zoom buttons
                Positioned(
                  bottom: 16,
                  right: 16,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.end,
                    children: [
                      ElevatedButton(
                        onPressed: () {
                          mapController.zoomIn();
                        },
                        child: const Icon(Icons.add),
                      ),
                      ElevatedButton(
                        onPressed: () {
                          mapController.zoomOut();
                        },
                        child: const Icon(Icons.remove),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
            ],
          ),
        ),
      ),
    );
  }
}