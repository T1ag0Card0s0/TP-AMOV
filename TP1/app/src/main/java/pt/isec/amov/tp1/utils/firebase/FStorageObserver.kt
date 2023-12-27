package pt.isec.amov.tp1.utils.firebase

import android.util.Log
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import pt.isec.amov.tp1.data.Category
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.data.PlaceOfInterest

class FStorageObserver {
    companion object {
        private val db by lazy { Firebase.firestore }
        private val storage = Firebase.storage
        private val locationsColletion = db.collection("Locations")
        private val categoriesCollection = db.collection("Categories")
        private val placesOfInterestCollection = db.collection("PlacesOfInterest")
        private var categoryListenerRegistration: ListenerRegistration? = null
        private var placeOfInterestListenerRegistration: ListenerRegistration? = null
        private var locationListenerRegistration: ListenerRegistration? = null
        fun observeLocation(onNewValue: (List<Location>?) -> Unit){
            locationListenerRegistration = locationsColletion
                .addSnapshotListener { querySnapshot, e ->
                    if (e != null) {
                        Log.e("Firestore", "Error listening for locations", e)
                        onNewValue(null)
                        return@addSnapshotListener
                    }
                    val locations: MutableList<Location> = mutableListOf()
                    querySnapshot?.documents?.forEach { document ->

                        val location = Location(
                            document.getString("id") ?: "",
                            document.getString("authorEmail") ?: "",
                            document.getString("name") ?: "",
                            document.getString("description") ?: "",
                            document.getString("imageName") ?: "",
                            document.getString("imageUri"),
                            document.getDouble("latitude") ?: 0.0,
                            document.getDouble("longitude") ?: 0.0,
                            document.getString("user1"),
                            document.getString("user2"),
                        )
                        if(location.user1!=null&&location.user2!=null) location.approved=true
                        locations.add(location)
                    }
                    onNewValue(locations)
                }
        }
        fun observePlaceOfInterest(onNewValue: (List<PlaceOfInterest>?) -> Unit){
            placeOfInterestListenerRegistration = placesOfInterestCollection
                .addSnapshotListener { querySnapshot, e ->
                    if (e != null) {
                        Log.e("Firestore", "Error listening for categories", e)
                        onNewValue(null)
                        return@addSnapshotListener
                    }

                    val placesOfInterest: MutableList<PlaceOfInterest> = mutableListOf()

                    querySnapshot?.documents?.forEach { document ->
                        val placeOfInterest = PlaceOfInterest(
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
                            document.getString("user2"),

                        )
                        if(placeOfInterest.user1!=null&&placeOfInterest.user2!=null) placeOfInterest.approved=true
                        placesOfInterest.add(placeOfInterest)
                    }

                    onNewValue(placesOfInterest)
                }
        }
        fun observeCategory(onNewValue: (List<Category>?) -> Unit){
            categoryListenerRegistration = categoriesCollection
                .addSnapshotListener { querySnapshot, e ->
                    if (e != null) {
                        Log.e("Firestore", "Error listening for categories", e)
                        onNewValue(null)
                        return@addSnapshotListener
                    }

                    val categories: MutableList<Category> = mutableListOf()

                    querySnapshot?.documents?.forEach { document ->
                        val id = document.getString("id") ?: ""
                        val name = document.getString("name") ?: ""
                        val description = document.getString("description") ?: ""
                        val authorEmail = document.getString("authorEmail") ?: ""

                        val category = Category(id, authorEmail, name, description, imageVector = null)
                        categories.add(category)
                    }

                    onNewValue(categories)
                }
        }
        fun stopAllObservers() {
            categoryListenerRegistration?.remove()
            locationListenerRegistration?.remove()
            placeOfInterestListenerRegistration?.remove()
        }
    }
}