
import 'package:flutter/material.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:tp2/DetailsScreen.dart';
import 'PlacesOfInterestScreen.dart';
import 'RecentPlacesScreen.dart';
import 'data/Locations.dart';

class LocationService {
  Future<List<Locations>> getLocations(String ?orderBy, String ?searchTerm) async {
    var db = FirebaseFirestore.instance;

    QuerySnapshot<Map<String, dynamic>> collection = await db.collection('Locations').get();

    // Adiciona a lógica de ordenação
    if (orderBy == 'Alphabetical Asc (A -> Z)') {
      collection = await db.collection('Locations').orderBy('name').get();
    } else if (orderBy == 'Alphabetical Desc (Z -> A)') {
      collection = await db.collection('Locations').orderBy('name', descending: true).get();
    } else if (orderBy == 'Distance') {
      // Lógica para ordenar por distância, se aplicável
    }

    List<Locations> locations = [];
    for (var doc in collection.docs) {
      Map<String, dynamic> data = doc.data();
      // Adiciona a lógica de pesquisa
      if (searchTerm == null || searchTerm.isEmpty || data['name'].toString().toLowerCase() == searchTerm.toLowerCase()) {
        locations.add(Locations(
            id: doc.id,
            name: data['name'],
            description: data['description'],
            imageUri: data['imageUri'],
            latitude: data['latitude'],
            longitude: data['longitude']
        ));
      }
    }
    return locations;
  }
}

class ListScreen extends StatefulWidget {
  const ListScreen({super.key});

  static const String routeName = '/ListScreen';

  @override
  State<ListScreen> createState() => _ListScreenState();
}

class _ListScreenState extends State<ListScreen> {
  static const List<String> orderOptions = [
    'Alphabetical Asc (A -> Z)',
    'Alphabetical Desc (Z -> A)',
    'Distance',
  ];

  final LocationService _locationService = LocationService();
  final TextEditingController _searchController = TextEditingController();
  String? orderByValue;
  String? searchTerm;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Hero(tag: "btnSecond", child: Text('Localizações')),
      ),
      body: Column(
        children: [
          // Barra de Pesquisa
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextField(
              controller: _searchController,
              decoration: InputDecoration(
                labelText: 'Pesquisar Localização',
                suffixIcon: IconButton(
                  icon: const Icon(Icons.search),
                  onPressed: () {
                    setState(() {
                      searchTerm = _searchController.text;
                      _updateLocations();
                    });
                  },
                ),
              ),
            ),
          ),
          Row(
            // Dropdown para selecionar a ordenação
              children:[
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: DropdownButton<String>(
                    hint: const Text("Order by ..."),
                    value: orderByValue,
                    icon: const Icon(Icons.arrow_downward),
                    elevation: 16,
                    style: const TextStyle(color: Colors.black),
                    underline: Container(
                      height: 2,
                      color: Colors.amber,
                    ),
                    onChanged: (String? value) {
                      // Atualiza o valor do Dropdown e chama a função de atualização
                      setState(() {
                        orderByValue = value!;
                        _updateLocations();
                      });
                    },
                    items: orderOptions.map<DropdownMenuItem<String>>((String value) {
                      return DropdownMenuItem<String>(
                        value: value,
                        child: Text(value),
                      );
                    }).toList(),
                  ),
                ),
                const SizedBox(width: 50.0),
                Hero(
                    tag: "btnRecents",
                    child: ElevatedButton(
                        onPressed: () async {
                          await Navigator.pushNamed(
                              context, RecentPlacesScreen.routeName
                          );
                        },
                        child: const Text('Ver Recentes')
                    ),
                ),
              ]
          ),
          // Lista de Localizações
          Expanded(
            child: FutureBuilder<List<Locations>>(
              future: _locationService.getLocations(orderByValue, searchTerm),
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting) {
                  return const CircularProgressIndicator();
                } else if (snapshot.hasError) {
                  return Text('Error: ${snapshot.error}');
                } else {
                  List<Locations> locations = snapshot.data!;
                  return ListView(
                    children: locations.map((location) => Card(
                      child: GestureDetector(
                        onTap: () {
                          // Navegar para uma tela ao tocar em qualquer lugar da Card, exceto nos três pontinhos
                          Navigator.pushNamed(
                            context,
                            PlacesOfInterestScreen.routeName,
                            arguments: location,
                          );
                        },
                        child: ListTile(
                          title: Column(
                            children: [
                              // Image widget placed above the Text
                              Image.network(
                                location.imageUri,
                                width: double.infinity,
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
                            ],
                          ),
                          subtitle: Row(
                            children: [
                              Padding(
                                padding: const EdgeInsets.all(8.0),
                                child: Text(location.name, style: const TextStyle(
                                  fontSize: 20.0,
                                  fontWeight: FontWeight.bold,
                                )),
                              ),
                              const Spacer(),
                              Hero(
                                tag: "btnIcon",
                                child: IconButton(
                                  icon: const Icon(Icons.more_vert), // Ícone de três pontos,
                                  onPressed: () async {
                                    await Navigator.pushNamed(
                                        context, DetailsScreen.routeName,
                                        arguments: location
                                    );
                                  },
                                ),
                              )
                            ],
                          ),
                        ),
                      ),
                    )).toList(),
                  );
                }
              },
            ),
          ),
        ],
      ),
    );
  }

  // Função para atualizar a lista de localizações com base no Dropdown e na pesquisa
  void _updateLocations() {
    _locationService.getLocations(orderByValue, searchTerm);
  }
}





