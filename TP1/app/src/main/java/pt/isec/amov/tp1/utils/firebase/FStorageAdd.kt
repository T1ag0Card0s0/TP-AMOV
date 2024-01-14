package pt.isec.amov.tp1.utils.firebase

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import pt.isec.amov.tp1.data.Category
import pt.isec.amov.tp1.data.Classification
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.data.PlaceOfInterest
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class FStorageAdd {
    companion object {
        private val db by lazy { Firebase.firestore }
        private val storage = Firebase.storage
        private val locationsCollection = db.collection("Locations")
        private val categoriesCollection = db.collection("Categories")
        private val placesOfInterestCollection = db.collection("PlacesOfInterest")
        private val classificationsCollection = db.collection("Classifications")
        fun location(location: Location, onResult: (Throwable?) -> Unit){
            verifyIfExist(
                locationsCollection,
                "name",
                location.name
            ) { v, exp ->
                if (v || exp != null) {
                    onResult(exp)
                } else {
                    if (location.imageName!!.isNotEmpty()) {
                        val file = File(location.imageName!!)
                        val inputStream = FileInputStream(file)
                        uploadFile(
                            inputStream,
                            location.id + "." + file.extension,
                            onSuccess = {
                                Log.i("Image inserted", "Success $it")
                                location.imageUri = it
                                FStorageUpdate.location(location) {

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
                    locationsCollection.document(location.id).set(dataToAdd)
                        .addOnCompleteListener { result ->
                            onResult(result.exception)
                        }
                }
            }
        }
        fun placeOfInterest(
            placeOfInterest: PlaceOfInterest,
            onResult: (Throwable?) -> Unit
        ){
            val existsWith = hashMapOf(
                "locationId" to placeOfInterest.locationId,
                "name" to placeOfInterest.name
            )
            verifyIfExist(
                placesOfInterestCollection,
                existsWith
            ) { v, exp ->
                if (v || exp != null) {
                    onResult(exp)
                } else {
                    if (placeOfInterest.imageName!!.isNotEmpty()) {
                        val file = File(placeOfInterest.imageName!!)
                        val inputStream = FileInputStream(file)
                        uploadFile(
                            inputStream,
                            placeOfInterest.id + "." + file.extension,
                            onSuccess = {
                                placeOfInterest.imageUri = it
                                FStorageUpdate.placeOfInterest(placeOfInterest) {

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
                    placesOfInterestCollection.document(placeOfInterest.id)
                        .set(dataToAdd)
                        .addOnCompleteListener { result ->
                            onResult(result.exception)
                        }
                }
            }
        }
        fun category(category: Category, onResult: (Throwable?) -> Unit){
            verifyIfExist(
                categoriesCollection,
                "name",
                category.name
            ) { v, exp ->
                if (v || exp != null) {
                    onResult(exp)
                } else {
                    val dataToAdd = hashMapOf(
                        "id" to category.id,
                        "name" to category.name,
                        "description" to category.description,
                        "authorEmail" to category.authorEmail,
                        "iconName" to category.iconName,
                    )
                    categoriesCollection.document(category.id).set(dataToAdd)
                        .addOnCompleteListener { result ->
                            onResult(result.exception)
                        }
                }
            }
        }
        fun classification(classification: Classification, onResult:(Throwable?)->Unit){
            val existsWith = hashMapOf(
                "placeOfInterest" to classification.placeOfInterestId,
                "authorEmail" to classification.authorEmail
            )
            verifyIfExist(
                classificationsCollection,
                existsWith
            ){v, exp->
                if (v || exp != null) {
                    onResult(exp)
                }else{
                    if (classification.imageName!!.isNotBlank()) {
                        val file = File(classification.imageName!!)
                        val inputStream = FileInputStream(file)
                        uploadFile(
                            inputStream,
                            classification.id + "." + file.extension,
                            onSuccess = {
                                classification.imageUri = it
                                FStorageUpdate.classification(classification) {

                                }
                            }
                        )
                        classification.imageName = classification.id + "." + file.extension
                    }
                    val dataToAdd = hashMapOf(
                        "id" to classification.id,
                        "authorEmail" to classification.authorEmail,
                        "value" to classification.value,
                        "comment" to classification.comment,
                        "imageName" to classification.imageName,
                        "imageUri" to classification.imageUri,
                        "placeOfInterest" to classification.placeOfInterestId
                    )
                    classificationsCollection.document(classification.id).set(dataToAdd)
                        .addOnCompleteListener { result->
                            onResult(result.exception)
                        }
                }
            }
        }
        fun verifyIfExist(
            collectionReference: CollectionReference,
            field: String,
            value: String,
            onResult: (Boolean, Throwable?) -> Unit
        ) {
            collectionReference
                .whereEqualTo(field, value)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val querySnapshot = task.result
                        val exists = !querySnapshot.isEmpty
                        onResult(exists, null)
                    } else {
                        onResult(false, task.exception)
                    }
                }
        }
        private fun verifyIfExist(
            collectionReference: CollectionReference,
            fieldValues: Map<String, String>,
            onResult: (Boolean, Throwable?) -> Unit
        ) {
            var query: Query = collectionReference

            fieldValues.forEach { (field, value) ->
                query = query.whereEqualTo(field, value)
            }
            query.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val querySnapshot = task.result
                        val exists = !querySnapshot.isEmpty
                        onResult(exists, null)
                    } else {
                        onResult(false, task.exception)
                    }
                }
        }
        fun uploadFile(
            inputStream: InputStream,
            imgFile: String,
            onSuccess: (String) -> Unit
        ) {
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