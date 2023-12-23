
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
        title: Text('Detalhes'),
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Card(
            elevation: 8.0,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                // Imagem da localização AINDA EXEMPLO
                Image.network(
                  'https://habitability.com.br/wp-content/uploads/2022/04/Cidadade-compacta-680x405.png',
                  height: 200.0,
                  fit: BoxFit.cover,
                ),
                // Nome da localização
                Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Text(
                    location.name,
                    style: TextStyle(
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
