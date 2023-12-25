package pt.isec.amov.tp1.utils.firebase


import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import pt.isec.amov.tp1.data.Category
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.data.PlaceOfInterest
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


class FStorageUtil {
    companion object {
        //Adds
        fun addLocationToFirestore(location: Location, onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore
            if (location.imageName!!.isNotEmpty()) {
                val file = File(location.imageName!!)
                val inputStream = FileInputStream(file)
                uploadFile(
                    inputStream,
                    location.id + "." + file.extension,
                    onSuccess = {
                        Log.i("Image inserted", "Success $it")
                        location.imageUri = it
                        updateLocationInFirestore(location) {

                        }
                    }
                )
                location.imageName = location.id + "." + file.extension
            }

            val dataToAdd = hashMapOf(
                "id" to location.id,
                "name" to location.name,
                "description" to location.description,
                "imageName" to location.imageName,
                "authorEmail" to location.authorEmail,
                "imageUri" to location.imageUri,
                "latitude" to location.latitude,
                "longitude" to location.longitude
            )
            db.collection("Locations").document(location.id).set(dataToAdd)
                .addOnCompleteListener { result ->
                    onResult(result.exception)
                }
        }


        fun addPlaceOfInterestToFirestore(
            placeOfInterest: PlaceOfInterest,
            onResult: (Throwable?) -> Unit
        ) {
            val db = Firebase.firestore
            if (placeOfInterest.imageName!!.isNotEmpty()) {
                val file = File(placeOfInterest.imageName!!)
                val inputStream = FileInputStream(file)
                uploadFile(
                    inputStream,
                    placeOfInterest.id + "." + file.extension,
                    onSuccess = {
                        placeOfInterest.imageUri = it
                        updatePlaceOfInterestInFirestore(placeOfInterest) {

                        }
                    }
                )
                placeOfInterest.imageName = placeOfInterest.id + "." + file.extension
            }
            val dataToAdd = hashMapOf(
                "id" to placeOfInterest.id,
                "name" to placeOfInterest.name,
                "description" to placeOfInterest.description,
                "imageName" to placeOfInterest.imageName,
                "authorEmail" to placeOfInterest.authorEmail,
                "locationId" to placeOfInterest.locationId,
                "categoryId" to placeOfInterest.categoryId,
                "imageUri" to placeOfInterest.imageUri,
                "latitude" to placeOfInterest.latitude,
                "longitude" to placeOfInterest.longitude
            )
            db.collection("PlacesOfInterest").document(placeOfInterest.id).set(dataToAdd)
                .addOnCompleteListener { result ->
                    onResult(result.exception)
                }
        }


        fun addCategoryToFirestore(category: Category, onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore
            val dataToAdd = hashMapOf(
                "id" to category.id,
                "name" to category.name,
                "description" to category.description,
                "authorEmail" to category.authorEmail
            )
            db.collection("Categories").document(category.id).set(dataToAdd)
                .addOnCompleteListener { result ->
                    onResult(result.exception)
                }
        }
        //Updates

        fun updateLocationInFirestore(location: Location, onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore
            val dataToUpdate = db.collection("Locations").document(location.id)

            db.runTransaction { transaction ->
                val doc = transaction.get(dataToUpdate)
                if (doc.exists()) {
                    transaction.update(dataToUpdate, "name", location.name)
                    transaction.update(dataToUpdate, "description", location.description)
                    transaction.update(dataToUpdate, "imageName", location.imageName)
                    transaction.update(dataToUpdate, "imageUri", location.imageUri)
                    transaction.update(dataToUpdate, "latitude", location.latitude)
                    transaction.update(dataToUpdate, "longitude", location.longitude)

                    null
                } else
                    throw FirebaseFirestoreException(
                        "Doesn't exist",
                        FirebaseFirestoreException.Code.UNAVAILABLE
                    )
            }.addOnCompleteListener { result ->
                onResult(result.exception)
            }
        }

        fun updatePlaceOfInterestInFirestore(
            placeOfInterest: PlaceOfInterest,
            onResult: (Throwable?) -> Unit
        ) {
            val db = Firebase.firestore
            val dataToUpdate = db.collection("PlacesOfInterest").document(placeOfInterest.id)

            db.runTransaction { transaction ->
                val doc = transaction.get(dataToUpdate)
                if (doc.exists()) {
                    transaction.update(dataToUpdate, "name", placeOfInterest.name)
                    transaction.update(dataToUpdate, "description", placeOfInterest.description)
                    transaction.update(dataToUpdate, "imageName", placeOfInterest.imageName)
                    transaction.update(dataToUpdate, "locationId", placeOfInterest.locationId)
                    transaction.update(dataToUpdate, "categoryId", placeOfInterest.categoryId)
                    transaction.update(dataToUpdate, "imageUri", placeOfInterest.imageUri)
                    transaction.update(dataToUpdate, "latitude", placeOfInterest.latitude)
                    transaction.update(dataToUpdate, "longitude", placeOfInterest.longitude)
                    null
                } else
                    throw FirebaseFirestoreException(
                        "Doesn't exist",
                        FirebaseFirestoreException.Code.UNAVAILABLE
                    )
            }.addOnCompleteListener { result ->
                onResult(result.exception)
            }
        }

        fun updateCategoryInFirestore(category: Category, onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore
            val dataToUpdate = db.collection("Categories").document(category.id)

            db.runTransaction { transaction ->
                val doc = transaction.get(dataToUpdate)
                if (doc.exists()) {
                    transaction.update(dataToUpdate, "name", category.name)
                    transaction.update(dataToUpdate, "description", category.description)
                    null
                } else
                    throw FirebaseFirestoreException(
                        "Doesn't exist",
                        FirebaseFirestoreException.Code.UNAVAILABLE
                    )
            }.addOnCompleteListener { result ->
                onResult(result.exception)
            }
        }

        //Removes
        fun removeCategoryFromFireStone(category: Category, onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore
            val dataToRemove = db.collection("Categories").document(category.id)

            dataToRemove.delete()
                .addOnCompleteListener { onResult(it.exception) }
        }

        fun removeLocationFromFireStone(location: Location, onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore
            val dataToRemove = db.collection("Locations").document(location.id)
            removeFile(location.imageName!!)
            dataToRemove.delete()
                .addOnCompleteListener { onResult(it.exception) }
        }

        fun removePlaceOfInterestFromFireStone(
            placeOfInterest: PlaceOfInterest,
            onResult: (Throwable?) -> Unit
        ) {
            val db = Firebase.firestore
            val dataToRemove = db.collection("PlacesOfInterest").document(placeOfInterest.id)
            removeFile(placeOfInterest.imageName!!)
            dataToRemove.delete()
                .addOnCompleteListener {

                    onResult(it.exception)
                }
        }

        //Observers
        private var categoryListenerRegistration: ListenerRegistration? = null
        private var placeOfInterestListenerRegistration: ListenerRegistration? = null
        private var locationListenerRegistration: ListenerRegistration? = null

        fun startCategoryObserver(onNewValue: (List<Category>?) -> Unit) {
            val db = Firebase.firestore
            val collectionReference = db.collection("Categories")

            categoryListenerRegistration = collectionReference
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

                        val category = Category(id, authorEmail, name, description)
                        categories.add(category)
                    }

                    onNewValue(categories)
                }
        }

        fun startLocationObserver(onNewValue: (List<Location>?) -> Unit) {
            val db = Firebase.firestore
            val collectionReference = db.collection("Locations")
            locationListenerRegistration = collectionReference
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
                            document.getDouble("longitude") ?: 0.0
                        )

                        locations.add(location)
                    }

                    onNewValue(locations)
                }
        }

        fun startPlacesOfInterestObserver(onNewValue: (List<PlaceOfInterest>?) -> Unit) {
            val db = Firebase.firestore
            val collectionReference = db.collection("PlacesOfInterest")

            placeOfInterestListenerRegistration = collectionReference
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
                            document.getDouble("longitude") ?: 0.0

                        )
                        placesOfInterest.add(placeOfInterest)
                    }

                    onNewValue(placesOfInterest)
                }
        }

        fun stopObserver() {
            categoryListenerRegistration?.remove()
            locationListenerRegistration?.remove()
            placeOfInterestListenerRegistration?.remove()
        }

        // Storage
        private fun removeFile(
            imgFile: String
        ){
            val storage = Firebase.storage
            val ref1 = storage.reference
            val ref2 = ref1.child("images")
            val ref3 = ref2.child(imgFile)
            ref3.delete()
        }
        private fun uploadFile(
            inputStream: InputStream,
            imgFile: String,
            onSuccess: (String) -> Unit
        ) {
            val storage = Firebase.storage
            val ref1 = storage.reference
            val ref2 = ref1.child("images")
            val ref3 = ref2.child(imgFile)

            val uploadTask = ref3.putStream(inputStream)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref3.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    onSuccess(downloadUri.toString())
                } else {
                    Log.i("Image insert", "Failed")
                    // Handle failures
                    // ...
                }
            }


        }


    }
}