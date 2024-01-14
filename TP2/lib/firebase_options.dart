// File generated by FlutterFire CLI.
// ignore_for_file: lines_longer_than_80_chars, avoid_classes_with_only_static_members
import 'package:firebase_core/firebase_core.dart' show FirebaseOptions;
import 'package:flutter/foundation.dart'
    show defaultTargetPlatform, kIsWeb, TargetPlatform;

/// Default [FirebaseOptions] for use with your Firebase apps.
///
/// Example:
/// ```dart
/// import 'firebase_options.dart';
/// // ...
/// await Firebase.initializeApp(
///   options: DefaultFirebaseOptions.currentPlatform,
/// );
/// ```
class DefaultFirebaseOptions {
  static FirebaseOptions get currentPlatform {
    if (kIsWeb) {
      return web;
    }
    switch (defaultTargetPlatform) {
      case TargetPlatform.android:
        return android;
      case TargetPlatform.iOS:
        return ios;
      case TargetPlatform.macOS:
        return macos;
      case TargetPlatform.windows:
        throw UnsupportedError(
          'DefaultFirebaseOptions have not been configured for windows - '
          'you can reconfigure this by running the FlutterFire CLI again.',
        );
      case TargetPlatform.linux:
        throw UnsupportedError(
          'DefaultFirebaseOptions have not been configured for linux - '
          'you can reconfigure this by running the FlutterFire CLI again.',
        );
      default:
        throw UnsupportedError(
          'DefaultFirebaseOptions are not supported for this platform.',
        );
    }
  }

  static const FirebaseOptions web = FirebaseOptions(
    apiKey: 'AIzaSyBz4qodPNnfnGs5vknu4MO7sgGdml-u9o0',
    appId: '1:1021416092308:web:fed4cff35ebb1ea930ba81',
    messagingSenderId: '1021416092308',
    projectId: 'tp1-amov-2b4bb',
    authDomain: 'tp1-amov-2b4bb.firebaseapp.com',
    storageBucket: 'tp1-amov-2b4bb.appspot.com',
    measurementId: 'G-TS0X6Q6W17',
  );

  static const FirebaseOptions android = FirebaseOptions(
    apiKey: 'AIzaSyCPNjeDa8N5_YZ_zL9VZWwVEXpDonFeIec',
    appId: '1:1021416092308:android:9b75bccd3405bba530ba81',
    messagingSenderId: '1021416092308',
    projectId: 'tp1-amov-2b4bb',
    storageBucket: 'tp1-amov-2b4bb.appspot.com',
  );

  static const FirebaseOptions ios = FirebaseOptions(
    apiKey: 'AIzaSyDAfPijRzLHp76uIsYuAGH78R3iv3vnTD4',
    appId: '1:1021416092308:ios:193b153e0905b8b230ba81',
    messagingSenderId: '1021416092308',
    projectId: 'tp1-amov-2b4bb',
    storageBucket: 'tp1-amov-2b4bb.appspot.com',
    iosBundleId: 'com.example.tp2',
  );

  static const FirebaseOptions macos = FirebaseOptions(
    apiKey: 'AIzaSyDAfPijRzLHp76uIsYuAGH78R3iv3vnTD4',
    appId: '1:1021416092308:ios:4cb51b9bd957dcba30ba81',
    messagingSenderId: '1021416092308',
    projectId: 'tp1-amov-2b4bb',
    storageBucket: 'tp1-amov-2b4bb.appspot.com',
    iosBundleId: 'com.example.tp2.RunnerTests',
  );
}
