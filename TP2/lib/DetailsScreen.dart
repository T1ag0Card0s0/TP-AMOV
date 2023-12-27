import 'package:flutter/material.dart';
import 'package:flutter_osm_plugin/flutter_osm_plugin.dart';
import 'data/Locations.dart';

class DetailsScreen extends StatefulWidget {
  const DetailsScreen({super.key});

  static const String routeName = '/DetailsScreen';

  @override
  State<DetailsScreen> createState() => _DetailsScreenState();
}

class _DetailsScreenState extends State<DetailsScreen> {
  late MapController mapController;

  @override
  Widget build(BuildContext context) {
    final Locations location = ModalRoute.of(context)!.settings.arguments as Locations;

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

    // Add a marker to the location
    mapController.addMarker(
      GeoPoint(
        latitude: location.latitude,
        longitude: location.longitude,
      ),
      markerIcon: const MarkerIcon(
        icon: Icon(
          Icons.place,
          color: Colors.red, // Set your desired color
          size: 76,
        ),
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
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 16.0),
                      child: Text(
                        location.description,
                        style: const TextStyle(
                          fontSize: 20.0,
                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.all(16.0),
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
