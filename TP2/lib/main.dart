
import 'package:flutter/material.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:tp2/DetailsScreen.dart';
import 'package:tp2/ListScreen.dart';
import 'package:tp2/PlacesOfInterestScreen.dart';
import 'package:tp2/RecentPlacesScreen.dart';
import 'DetailsPlaceScreen.dart';
import 'firebase_options.dart';

void initFirebase() async {
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );
}

void main() {
  // Ensure that Flutter is initialized before Firebase
  WidgetsFlutterBinding.ensureInitialized();
  initFirebase();
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'TP2',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.white),
        useMaterial3: true,
      ),
      initialRoute: LoginPage.routeName,
      routes: {
        LoginPage.routeName : (context) => const LoginPage(),
        ListScreen.routeName : (context) => const ListScreen(),
        DetailsScreen.routeName : (context) => const DetailsScreen(),
        DetailsPlaceScreen.routeName : (context) => const DetailsPlaceScreen(),
        RecentPlacesScreen.routeName : (context) => const RecentPlacesScreen(),
        PlacesOfInterestScreen.routeName : (context) => const PlacesOfInterestScreen()
      },
    );
  }
}

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  static const String routeName = '/';

  @override
  State<LoginPage> createState() => _LoginPageState();
}
class _LoginPageState extends State<LoginPage> {
  @override
  Widget build(BuildContext context) {

    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.background,
      ),
      body: Center(

        child: Column(

          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            SizedBox(height:200, child: Image.asset('images/amov.png')),
            Hero(
              tag: "btnSecond",
              child: ElevatedButton(
                  onPressed: () async {
                    await Navigator.pushNamed(
                        context, ListScreen.routeName
                    );
                  },
                  child: const Text('LOGIN')
              ),
            ),
          ],
        ),
      ),

    );
  }
}