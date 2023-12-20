package pt.isec.amov.tp1.utils.firebase

import android.content.res.AssetManager
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Source
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
        fun addOrUpdateLocationToFirestore(location: Location, onResult: (Throwable?) -> Unit){
            val db = Firebase.firestore
            val dataFromFS = db.collection("Locations").document(location.name)

            db.runTransaction { transaction ->
                val doc = transaction.get(dataFromFS)
                if (doc.exists()) {
                    transaction.update(dataFromFS, "name", location.name)
                    transaction.update(dataFromFS, "description", location.description)
                    transaction.update(dataFromFS, "imagePath", location.imagePath)
                    transaction.update(dataFromFS, "authorEmail", location.authorEmail)
                    null
                } else{
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

            }.addOnCompleteListener { result ->
                onResult(result.exception)
            }
        }
        fun addOrUpdatePlaceOfInterestToFirestore(placeOfInterest: PlaceOfInterest, onResult: (Throwable?) -> Unit){
            val db = Firebase.firestore
            val dataFromFS = db.collection("PlacesOfInterest").document(placeOfInterest.name)

            db.runTransaction { transaction ->
                val doc = transaction.get(dataFromFS)
                if (doc.exists()) {
                    transaction.update(dataFromFS, "name", placeOfInterest.name)
                    transaction.update(dataFromFS, "description", placeOfInterest.description)
                    transaction.update(dataFromFS, "categoryId", placeOfInterest.categoryId)
                    transaction.update(dataFromFS, "locationId", placeOfInterest.locationId)
                    transaction.update(dataFromFS, "imagePath", placeOfInterest.imagePath)
                    transaction.update(dataFromFS, "authorEmail", placeOfInterest.authorEmail)
                    null
                } else{
                    val dataToAdd = hashMapOf(
                        "id" to placeOfInterest.id,
                        "name" to placeOfInterest.name,
                        "description" to placeOfInterest.description,
                        "categoryId" to placeOfInterest.categoryId,
                        "locationId" to placeOfInterest.locationId,
                        "imagePath" to placeOfInterest.imagePath,
                        "authorEmail" to placeOfInterest.authorEmail
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

            }.addOnCompleteListener { result ->
                onResult(result.exception)
            }
        }

        fun addOrUpdateCategories(category: Category, onResult: (Throwable?) -> Unit){
            val db = Firebase.firestore
            val dataFromFS = db.collection("Categories").document(category.name)

            db.runTransaction { transaction ->
                val doc = transaction.get(dataFromFS)
                if (doc.exists()) {
                    transaction.update(dataFromFS, "name", category.name)
                    transaction.update(dataFromFS, "description", category.description)
                    null
                } else{
                    val dataToAdd = hashMapOf(
                        "id" to category.id,
                        "authorEmail" to category.authorEmail,
                        "name" to category.name,
                        "description" to category.description
                    )
                    db.collection("Categories").document(category.name).set(dataToAdd)
                        .addOnCompleteListener { result ->
                            onResult(result.exception)
                        }
                }

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

        fun stopObserver() {
            listenerRegistration?.remove()
        }

// Storage

        fun getFileFromAsset(assetManager: AssetManager, strName: String): InputStream? {
            var istr: InputStream? = null
            try {
                istr = assetManager.open(strName)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return istr
        }

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