package pt.isec.amov.tp1.utils.firebase

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import pt.isec.amov.tp1.data.Category
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.data.PlaceOfInterest

class FStorageUpdate {
    companion object {
        private val db by lazy { Firebase.firestore }
        private val storage = Firebase.storage
        private val locationsColletion = db.collection("Locations")
        private val categoriesCollection = db.collection("Categories")
        private val placesOfInterestCollection = db.collection("PlacesOfInterest")
        fun location(location: Location, onResult: (Throwable?) -> Unit){
            val dataToUpdate = locationsColletion.document(location.id)
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
        fun placeOfInterest(
            placeOfInterest: PlaceOfInterest,
            onResult: (Throwable?) -> Unit
        ){
            val dataToUpdate = placesOfInterestCollection.document(placeOfInterest.id)

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
        fun category(category: Category, onResult: (Throwable?) -> Unit){
            val dataToUpdate = categoriesCollection.document(category.id)

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
    }
}