package pt.isec.amov.tp1.utils.firebase

import android.content.res.AssetManager
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
import java.io.IOException
import java.io.InputStream


class FStorageUtil {
    companion object {
        fun addLocationToFirestore(location: Location, onResult: (Throwable?) -> Unit){
            val db = Firebase.firestore
            val dataToAdd = hashMapOf(
                "id" to location.id,
                "name" to location.name,
                "description" to location.description,
                "imagePath" to location.imagePath,
                "authorEmail" to location.authorEmail
            )
            if (location.imagePath!!.isNotEmpty()) {
                val file = File(location.imagePath)
                val inputStream = FileInputStream(file)
                uploadFile(inputStream, file.name)
            }
            db.collection("Locations").document(location.name).set(dataToAdd)
                .addOnCompleteListener { result ->
                    onResult(result.exception)
                }
        }
        fun updateLocationInFirestore(location: Location, onResult: (Throwable?) -> Unit){
            val db = Firebase.firestore
            val dataToUpdate = db.collection("Locations").document(location.name)

            db.runTransaction { transaction ->
                val doc = transaction.get(dataToUpdate)
                if (doc.exists()) {
                    transaction.update(dataToUpdate, "name", location.name)
                    transaction.update(dataToUpdate, "description", location.description)
                    transaction.update(dataToUpdate, "imagePath", location.imagePath)
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
        fun addPlaceOfInterestToFirestore(placeOfInterest: PlaceOfInterest, onResult: (Throwable?) -> Unit){
            val db = Firebase.firestore
            val dataToAdd = hashMapOf(
                "id" to placeOfInterest.id,
                "name" to placeOfInterest.name,
                "description" to placeOfInterest.description,
                "imagePath" to placeOfInterest.imagePath,
                "authorEmail" to placeOfInterest.authorEmail,
                "locationId" to placeOfInterest.locationId,
                "categoryId" to placeOfInterest.categoryId
            )
            if (placeOfInterest.imagePath!!.isNotEmpty()) {
                val file = File(placeOfInterest.imagePath)
                val inputStream = FileInputStream(file)
                uploadFile(inputStream, file.name)
            }
            db.collection("PlacesOfInterest").document(placeOfInterest.name).set(dataToAdd)
                .addOnCompleteListener { result ->
                    onResult(result.exception)
                }
        }
        fun updatePlaceOfInterestInFirestore(placeOfInterest: PlaceOfInterest, onResult: (Throwable?) -> Unit){
            val db = Firebase.firestore
            val dataToUpdate = db.collection("PlacesOfInterest").document(placeOfInterest.name)

            db.runTransaction { transaction ->
                val doc = transaction.get(dataToUpdate)
                if (doc.exists()) {
                    transaction.update(dataToUpdate, "name", placeOfInterest.name)
                    transaction.update(dataToUpdate, "description", placeOfInterest.description)
                    transaction.update(dataToUpdate, "imagePath", placeOfInterest.imagePath)
                    transaction.update(dataToUpdate, "locationId", placeOfInterest.locationId)
                    transaction.update(dataToUpdate, "categoryId", placeOfInterest.categoryId)
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
        fun addCategoryToFirestore(category: Category, onResult: (Throwable?) -> Unit){
            val db = Firebase.firestore
            val dataToAdd = hashMapOf(
                "id" to category.id,
                "name" to category.name,
                "description" to category.description,
                "authorEmail" to category.authorEmail
            )
            db.collection("Categories").document(category.name).set(dataToAdd)
                .addOnCompleteListener { result ->
                    onResult(result.exception)
                }
        }
        fun updateCategoryInFirestore(category: Category, onResult: (Throwable?) -> Unit){
            val db = Firebase.firestore
            val dataToUpdate = db.collection("Categories").document(category.name)

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
        fun removeCategoryFromFireStone(category: Category, onResult: (Throwable?) -> Unit){
            val db = Firebase.firestore
            val dataToRemove = db.collection("Categories").document(category.name)

            dataToRemove.delete()
                .addOnCompleteListener { onResult(it.exception) }
        }
        fun removeLocationFromFireStone(location: Location, onResult: (Throwable?) -> Unit){
            val db = Firebase.firestore
            val dataToRemove = db.collection("Locations").document(location.name)

            dataToRemove.delete()
                .addOnCompleteListener { onResult(it.exception) }
        }
        fun removePlaceOfInterestFromFireStone(placeOfInterest: PlaceOfInterest, onResult: (Throwable?) -> Unit){
            val db = Firebase.firestore
            val dataToRemove = db.collection("PlacesOfInterest").document(placeOfInterest.name)

            dataToRemove.delete()
                .addOnCompleteListener { onResult(it.exception) }
        }

        private var listenerRegistration: ListenerRegistration? = null

        fun startObserver(onNewValues: (Long, Long) -> Unit) {
            stopObserver()
            val db = Firebase.firestore
            listenerRegistration = db.collection("Scores").document("Level1")
                .addSnapshotListener { docSS, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    if (docSS != null && docSS.exists()) {
                        val nrgames = docSS.getLong("nrgames") ?: 0
                        val topscore = docSS.getLong("topscore") ?: 0
                        Log.i("Firestore", "$nrgames : $topscore")
                        onNewValues(nrgames, topscore)
                    }
                }
        }
        fun startCategoryObserver(onNewValue: (List<Category>?) -> Unit) {
            val db = Firebase.firestore
            val collectionReference = db.collection("Categories")

            listenerRegistration = collectionReference
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

                        val category = Category(id,authorEmail, name, description)
                        categories.add(category)
                    }

                    onNewValue(categories)
                }
        }
        fun startLocationObserver(onNewValue: (List<Location>?) -> Unit) {
            val db = Firebase.firestore
            val collectionReference = db.collection("Locations")

            listenerRegistration = collectionReference
                .addSnapshotListener { querySnapshot, e ->
                    if (e != null) {
                        Log.e("Firestore", "Error listening for categories", e)
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
                            document.getString("imagePath")?:""
                            )
                        locations.add(location)
                    }

                    onNewValue(locations)
                }
        }
        fun startPlacesOfInterestObserver(onNewValue: (List<PlaceOfInterest>?) -> Unit) {
            val db = Firebase.firestore
            val collectionReference = db.collection("Locations")

            listenerRegistration = collectionReference
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
                            document.getString("imagePath")?:"",
                            document.getString("categoryId")?:"",
                            document.getString("locationId")?:""
                        )
                        placesOfInterest.add(placeOfInterest)
                    }

                    onNewValue(placesOfInterest)
                }
        }
        fun stopObserver() {
            listenerRegistration?.remove()
        }
// Storage

//https://firebase.google.com/docs/storage/android/upload-files

        fun uploadFile(inputStream: InputStream, imgFile: String) {
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
                    println(downloadUri.toString())
                } else {
                    // Handle failures
                    // ...
                }
            }


        }



    }
}