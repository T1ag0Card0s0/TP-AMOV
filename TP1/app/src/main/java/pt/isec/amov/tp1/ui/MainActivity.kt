package pt.isec.amov.tp1.ui

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration
import pt.isec.amov.tp1.App
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.screens.MainScreen
import pt.isec.amov.tp1.ui.theme.TP1Theme
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.AppViewModelFactory
import pt.isec.amov.tp1.ui.viewmodels.location.LocalViewModel
import pt.isec.amov.tp1.ui.viewmodels.location.LocalViewModelFactory

class MainActivity : ComponentActivity() {
    private val app by lazy{
        application as App
    }
    private val appViewModel : AppViewModel by viewModels{
        AppViewModelFactory(app.appData)
    }
    private val locationViewModel : LocalViewModel by viewModels{
        LocalViewModelFactory(app.locationHandler)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(
            this,
            PreferenceManager.getDefaultSharedPreferences(this)
        )
        setContent {
            TP1Theme {
                MainScreen(appViewModel,locationViewModel)
            }
        }
        verifyPermissions()
        if(
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )!= PackageManager.PERMISSION_GRANTED||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )!= PackageManager.PERMISSION_GRANTED||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            )!= PackageManager.PERMISSION_GRANTED

        ) {
            verifyMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
            )
        }

        if(
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU&&
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_MEDIA_IMAGES)!= PackageManager.PERMISSION_GRANTED){
            verifySinglePermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }
    }
    private val verifyMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){

    }
    private val verifySinglePermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){

    }
    override fun onResume() {
        super.onResume()
        locationViewModel.startLocationUpdates()
    }

    private fun verifyPermissions() : Boolean{
        locationViewModel.coarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        locationViewModel.fineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            locationViewModel.backgroundLocationPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else
            locationViewModel.backgroundLocationPermission = locationViewModel.coarseLocationPermission || locationViewModel.fineLocationPermission

        if (!locationViewModel.coarseLocationPermission && !locationViewModel.fineLocationPermission) {
            basicPermissionsAuthorization.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return false
        } else
            verifyBackgroundPermission()
        return true
    }

    private val basicPermissionsAuthorization = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        locationViewModel.coarseLocationPermission = results[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        locationViewModel.fineLocationPermission = results[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        locationViewModel.startLocationUpdates()
        verifyBackgroundPermission()
    }

    private fun verifyBackgroundPermission() {
        if (!(locationViewModel.coarseLocationPermission || locationViewModel.fineLocationPermission))
            return

        if (!locationViewModel.backgroundLocationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            ) {
                val dlg = AlertDialog.Builder(this)
                    .setTitle("Background Location")
                    .setMessage(
                        "This application needs your permission to use location while in the background.\n" +
                                "Please choose the correct option in the following screen" +
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                                    " (\"${packageManager.backgroundPermissionOptionLabel}\")."
                                else
                                    "."
                    )
                    .setPositiveButton("Ok") { _, _ ->
                        backgroundPermissionAuthorization.launch(
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
                    }
                    .create()
                dlg.show()
            }
        }
    }

    private val backgroundPermissionAuthorization = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        locationViewModel.backgroundLocationPermission = result
        Toast.makeText(this,"Background location enabled: $result", Toast.LENGTH_LONG).show()
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.hello_name) + name,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TP1Theme {
        Greeting("Android")
    }
}