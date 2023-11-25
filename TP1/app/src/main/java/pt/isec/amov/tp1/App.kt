package pt.isec.amov.tp1

import android.app.Application
import pt.isec.amov.tp1.data.AppData
import java.io.File

class App : Application() {
    companion object{

    }

    val appData by lazy {
        AppData()
    }

    override fun onCreate(){
        super.onCreate()
        //Log.i("Sketches","Result: "+sketchesFile.delete())
    }
}