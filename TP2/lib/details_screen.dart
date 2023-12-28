
import 'package:flutter/material.dart';
import 'package:flutter_osm_plugin/flutter_osm_plugin.dart';
import 'package:tp2/places_service.dart';
import 'data/Locations.dart';
import 'data/PlacesOfInterest.dart';
import 'details_place_screen.dart';

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
    late List<PlaceOfInterest> places ;
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
                      mapIsLoading: const Center(
                        child: CircularProgressIndicator(),
                      ),
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
                              size: 40, // Ajuste o tamanho conforme necessário
                            ),
                          ),
                        ),
                      ),
                      onMapIsReady: (isReady) async {
                        if(isReady){
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

                          places = await PlacesOrderService().getPlacesWithoutOrder(location.id);

                          for( PlaceOfInterest poi in places){
                            await mapController.addMarker(
                              GeoPoint(
                                latitude: poi.latitude,
                                longitude: poi.longitude,
                              ),
                              markerIcon: const MarkerIcon(
                                icon: Icon(
                                  Icons.place,
                                  color: Colors.green,
                                  size: 40, // Ajuste o tamanho conforme necessário
                                ),
                              ),
                            );
                          }
                        }
                      },
                      onGeoPointClicked: (geoPoint) async {
                        PlaceOfInterest? poi = getPlaceOfInterestForGeoPoint(geoPoint, places);
                        if (poi != null) {
                          showModalBottomSheet(context: context, builder: (
                              context) {
                            return Card(
                              child: Padding(
                                padding: const EdgeInsets.all(8),
                                child: Row(
                                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  mainAxisSize: MainAxisSize.min,
                                  children: [
                                    Expanded(
                                      child: Column(
                                        children: [
                                          const Text(
                                            'Place Information',
                                            style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                                          ),
                                          const SizedBox(height: 8),
                                          Text('Name: ${poi.name}',style: const TextStyle(fontSize: 16)),
                                          Text('Latitude: ${poi.latitude}', style: const TextStyle(fontSize: 16)),
                                          Text('Longitude: ${poi.longitude}', style: const TextStyle(fontSize: 16)),
                                          const SizedBox(height: 50),
                                          Image.network(
                                              poi.imageUri,
                                              height: 200,
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
                                          const SizedBox(height: 50),
                                          ElevatedButton(
                                              onPressed: () {
                                                setState(() {
                                                  Navigator.pushNamed(
                                                    context,
                                                    DetailsPlaceScreen.routeName,
                                                    arguments: poi,
                                                  );
                                                });
                                              },
                                              style: ButtonStyle(
                                                backgroundColor: MaterialStateProperty.all<Color>(Colors.cyan),
                                                foregroundColor: MaterialStateProperty.all<Color>(Colors.white)
                                              ),
                                              child: const Text('See Place')
                                          )
                                        ],
                                      ),
                                    ),
                                    GestureDetector(
                                      onTap: () => Navigator.pop(context),
                                      child: const Icon(Icons.clear),
                                    )
                                  ],
                                ),
                              ),
                            );
                          });
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

  PlaceOfInterest? getPlaceOfInterestForGeoPoint(GeoPoint geoPoint, List<PlaceOfInterest> places) {
    for( PlaceOfInterest poi in places){
      if(poi.latitude == geoPoint.latitude && poi.longitude == geoPoint.longitude) {
        return poi;
      }
    }
    return null;
  }

}
