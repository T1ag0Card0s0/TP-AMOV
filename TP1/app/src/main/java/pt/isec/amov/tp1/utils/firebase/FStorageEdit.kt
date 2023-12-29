package pt.isec.amov.tp1.utils.firebase

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.data.PlaceOfInterest
import java.io.File
import java.io.FileInputStream

class FStorageEdit {
    companion object {
        private val db by lazy { Firebase.firestore }
        private val locationsCollection = db.collection("Locations")
        private val categoriesCollection = db.collection("Categories")
        private val placesOfInterestCollection = db.collection("PlacesOfInterest")
        private val classificationsCollection = db.collection("Classifications")

        fun location(l: Location,updateImage:Boolean, onResult:(Throwable?)->Unit){
            FStorageAdd.verifyIfExist(locationsCollection,"id",l.id){
                v,exp->
                if(v||exp!=null){
                    if (updateImage) {
                        val file = File(l.imageName!!)
                        val inputStream = FileInputStream(file)
                        FStorageAdd.uploadFile(
                            inputStream,
                            l.id + "." + file.extension,
                            onSuccess = {
                                Log.i("Image inserted", "Success $it")
                                l.imageUri = it
                                FStorageUpdate.location(l) {

                                }
                            }
                        )
                        l.imageName = l.id + "." + file.extension
                    }
                    FStorageUpdate.location(l){e->
                        onResult(e)
                    }
                }
            }
        }
        fun placeOfInterest(p:PlaceOfInterest,updateImage:Boolean, onResult: (Throwable?) -> Unit){
            FStorageAdd.verifyIfExist(placesOfInterestCollection,"id",p.id){
                v,exp->
                if(v||exp!=null){
                    if (updateImage) {
                        val file = File(p.imageName!!)
                        val inputStream = FileInputStream(file)
                        FStorageAdd.uploadFile(
                            inputStream,
                            p.id + "." + file.extension,
                            onSuccess = {
                                Log.i("Image inserted", "Success $it")
                                p.imageUri = it
                                FStorageUpdate.placeOfInterest(p) {

                                }
                            }
                        )
                        p.imageName = p.id + "." + file.extension
                    }
                    FStorageUpdate.placeOfInterest(p){e->
                        onResult(e)
                    }
                }
            }
        }
    }
}