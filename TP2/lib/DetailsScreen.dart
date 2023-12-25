
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'data/Location.dart';

class DetailsScreen extends StatefulWidget {
  const DetailsScreen({super.key});

  static const String routeName = '/DetailsScreen';

  @override
  State<DetailsScreen> createState() => _DetailsScreenState();
}

class _DetailsScreenState extends State<DetailsScreen> {
  static const routeName = '/details';

  @override
  Widget build(BuildContext context) {
    // Recupera os argumentos passados
    final Location location = ModalRoute.of(context)!.settings.arguments as Location;

    return Scaffold(
      appBar: AppBar(
        title: Text('Details'),
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Card(
            elevation: 8.0,
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
                  child: Text(
                    location.description,
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
                    style: TextStyle(fontSize: 16.0),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
