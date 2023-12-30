package pt.isec.amov.tp1.utils.firebase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import pt.isec.amov.tp1.data.Category
import pt.isec.amov.tp1.data.Classification
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.data.PlaceOfInterest

class FStorageRemove {
    companion object {
        private val db by lazy { Firebase.firestore }
        private val storage = Firebase.storage
        private val locationsCollection = db.collection("Locations")
        private val categoriesCollection = db.collection("Categories")
        private val placesOfInterestCollection = db.collection("PlacesOfInterest")
        private val classificationsCollection = db.collection("Classifications")
        fun location(location: Location, onResult: (Throwable?) -> Unit){
            val dataToRemove = locationsCollection.document(location.id)
            removeFile(location.imageName!!)
            dataToRemove.delete()
                .addOnCompleteListener { onResult(it.exception) }
        }
        fun placeOfInterest(
            placeOfInterest: PlaceOfInterest,
            onResult: (Throwable?) -> Unit
        ){
            val dataToRemove = placesOfInterestCollection.document(placeOfInterest.id)
            removeFile(placeOfInterest.imageName!!)
            dataToRemove.delete()
                .addOnCompleteListener {

                    onResult(it.exception)
                }
        }
        fun category(category: Category, onResult: (Throwable?) -> Unit){
            val dataToRemove = categoriesCollection.document(category.id)
            dataToRemove.delete()
                .addOnCompleteListener { onResult(it.exception) }
        }
        fun classification(c: Classification, onResult: (Throwable?) -> Unit) {
            val dataToRemove = classificationsCollection.document(c.id)
            dataToRemove.delete()
                .addOnCompleteListener{
                    if(!c.imageName.isNullOrEmpty())
                        removeFile(c.imageName!!)
                    onResult(it.exception)
                }
        }
        private fun removeFile(
            imgFile: String
        ) {
            val ref1 = storage.reference
            val ref2 = ref1.child("images")
            val ref3 = ref2.child(imgFile)
            ref3.delete()
        }


    }
}