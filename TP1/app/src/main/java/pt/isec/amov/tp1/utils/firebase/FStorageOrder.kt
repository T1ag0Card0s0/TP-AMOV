package pt.isec.amov.tp1.utils.firebase

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.data.PlaceOfInterest
import kotlin.math.*;

class FStorageOrder {
    companion object {
        private val db by lazy { Firebase.firestore }
        private val locationsColletion = db.collection("Locations")
        private val placesOfInterestCollection = db.collection("PlacesOfInterest")

        fun locationByNameAscending(onNewValue: (List<Location>?) -> Unit) {
            orderLocations("name", Query.Direction.ASCENDING, onNewValue)
        }

        fun locationByNameDescending(onNewValue: (List<Location>?) -> Unit) {
            orderLocations("name", Query.Direction.DESCENDING, onNewValue)
        }

        fun placeOfInterestByNameAscending(onNewValue: (List<PlaceOfInterest>?) -> Unit) {
            orderPlacesOfInterest("name", Query.Direction.ASCENDING, onNewValue)
        }

        fun placeOfInterestByNameDescending(onNewValue: (List<PlaceOfInterest>?) -> Unit) {
            orderPlacesOfInterest("name", Query.Direction.DESCENDING, onNewValue)
        }

        private fun orderLocations(
            field: String,
            direction: Query.Direction,
            onNewValue: (List<Location>?) -> Unit
        ) {
            locationsColletion.orderBy(field, direction)
                .addSnapshotListener { querySnapshot, e ->
                    handleSnapshot(querySnapshot, e, onNewValue) { document ->
                        Location(
                            document.getString("id") ?: "",
                            document.getString("authorEmail") ?: "",
                            document.getString("name") ?: "",
                            document.getString("description") ?: "",
                            document.getString("imageName") ?: "",
                            document.getString("imageUri"),
                            document.getDouble("latitude") ?: 0.0,
                            document.getDouble("longitude") ?: 0.0,
                            document.getString("user1"),
                            document.getString("user2")
                        )
                    }
                }
        }

        private fun orderPlacesOfInterest(
            field: String,
            direction: Query.Direction,
            onNewValue: (List<PlaceOfInterest>?) -> Unit
        ) {
            placesOfInterestCollection.orderBy(field, direction)
                .addSnapshotListener { querySnapshot, e ->
                    handleSnapshot(querySnapshot, e, onNewValue) { document ->
                        PlaceOfInterest(
                            document.getString("id") ?: "",
                            document.getString("authorEmail") ?: "",
                            document.getString("name") ?: "",
                            document.getString("description") ?: "",
                            document.getString("imageName") ?: "",
                            document.getString("categoryId") ?: "",
                            document.getString("locationId") ?: "",
                            document.getString("imageUri"),
                            document.getDouble("latitude") ?: 0.0,
                            document.getDouble("longitude") ?: 0.0,
                            document.getString("user1"),
                            document.getString("user2")
                        )
                    }
                }
        }

        private inline fun <T> handleSnapshot(
            querySnapshot: QuerySnapshot?,
            e: FirebaseFirestoreException?,
            onNewValue: (List<T>?) -> Unit,
            mapper: (DocumentSnapshot) -> T
        ) {
            if (e != null) {
                Log.e("Firestore", "Error listening", e)
                onNewValue(null)
                return
            }

            val list: MutableList<T> = mutableListOf()
            querySnapshot?.documents?.forEach { document ->
                list.add(mapper(document))
            }

            onNewValue(list)
        }
        fun locationsByDistanceAscending(
            latitude: Double,
            longitude: Double,
            onNewValue: (List<Location>?) -> Unit
        ) {
            orderLocationsByDistance(latitude, longitude, Query.Direction.ASCENDING, onNewValue)
        }

        fun locationsByDistanceDescending(
            latitude: Double,
            longitude: Double,
            onNewValue: (List<Location>?) -> Unit
        ) {
            orderLocationsByDistance(latitude, longitude, Query.Direction.DESCENDING, onNewValue)
        }

        fun placesOfInterestByDistanceAscending(
            latitude: Double,
            longitude: Double,
            onNewValue: (List<PlaceOfInterest>?) -> Unit
        ) {
            orderPlacesOfInterestByDistance(latitude, longitude, Query.Direction.ASCENDING, onNewValue)
        }

        fun placesOfInterestByDistanceDescending(
            latitude: Double,
            longitude: Double,
            onNewValue: (List<PlaceOfInterest>?) -> Unit
        ) {
            orderPlacesOfInterestByDistance(latitude, longitude, Query.Direction.DESCENDING, onNewValue)
        }

        private fun orderLocationsByDistance(
            latitude: Double,
            longitude: Double,
            direction: Query.Direction,
            onNewValue: (List<Location>?) -> Unit
        ) {
            locationsColletion
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val sortedLocations = querySnapshot.documents
                        .map { document ->
                            Location(
                                document.getString("id") ?: "",
                                document.getString("authorEmail") ?: "",
                                document.getString("name") ?: "",
                                document.getString("description") ?: "",
                                document.getString("imageName") ?: "",
                                document.getString("imageUri"),
                                document.getDouble("latitude") ?: 0.0,
                                document.getDouble("longitude") ?: 0.0,
                                document.getString("user1"),
                                document.getString("user2")
                            )
                        }
                        .sortedBy { location ->
                            calculateDistance(latitude, longitude, location.latitude, location.longitude)
                        }
                        .let { if (direction == Query.Direction.DESCENDING) it.reversed() else it }

                    onNewValue(sortedLocations)
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error getting locations", e)
                    onNewValue(null)
                }
        }

        private fun orderPlacesOfInterestByDistance(
            latitude: Double,
            longitude: Double,
            direction: Query.Direction,
            onNewValue: (List<PlaceOfInterest>?) -> Unit
        ) {
            placesOfInterestCollection
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val sortedPlacesOfInterest = querySnapshot.documents
                        .map { document ->
                            PlaceOfInterest(
                                document.getString("id") ?: "",
                                document.getString("authorEmail") ?: "",
                                document.getString("name") ?: "",
                                document.getString("description") ?: "",
                                document.getString("imageName") ?: "",
                                document.getString("categoryId") ?: "",
                                document.getString("locationId") ?: "",
                                document.getString("imageUri"),
                                document.getDouble("latitude") ?: 0.0,
                                document.getDouble("longitude") ?: 0.0,
                                document.getString("user1"),
                                document.getString("user2")
                            )
                        }
                        .sortedBy { placeOfInterest ->
                            calculateDistance(latitude, longitude, placeOfInterest.latitude, placeOfInterest.longitude)
                        }
                        .let { if (direction == Query.Direction.DESCENDING) it.reversed() else it }

                    onNewValue(sortedPlacesOfInterest)
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error getting places of interest", e)
                    onNewValue(null)
                }
        }
        private fun calculateDistance(
            lat1: Double,
            lon1: Double,
            lat2: Double,
            lon2: Double
        ): Double {
            val R = 6371 // Radius of the earth in km
            val dLat = deg2rad(lat2 - lat1)
            val dLon = deg2rad(lon2 - lon1)
            val a =
                sin(dLat / 2) * sin(dLat / 2) +
                        cos(deg2rad(lat1)) * cos(deg2rad(lat2)) *
                        sin(dLon / 2) * sin(dLon / 2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            return R * c // Distance in km
        }

        private fun deg2rad(deg: Double): Double {
            return deg * (PI / 180)
        }
    }
}
