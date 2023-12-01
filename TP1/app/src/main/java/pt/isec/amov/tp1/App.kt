package pt.isec.amov.tp1

import android.app.Application
import com.google.android.gms.location.LocationServices
import pt.isec.amov.tp1.data.AppData
import pt.isec.amov.tp1.utils.location.FusedLocationHandler
import pt.isec.amov.tp1.utils.location.LocationHandler
import java.io.File

class App : Application() {
    companion object{

    }
    val locationHandler: LocationHandler by lazy {
        val locationProvider = LocationServices.getFusedLocationProviderClient(this)
        FusedLocationHandler(locationProvider)
    }

    val appData by lazy {
        AppData()
    }

    override fun onCreate(){
        super.onCreate()
    }
}