package pt.isec.amov.tp1.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import pt.isec.amov.tp1.App
import pt.isec.amov.tp1.ui.screens.MainScreen
import pt.isec.amov.tp1.ui.theme.TP1Theme
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.AppViewModelFactory

class MainActivity : ComponentActivity() {
    private val app by lazy{application as App}
    private val viewModel : AppViewModel by viewModels{
        AppViewModelFactory(app.appData)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TP1Theme {
                MainScreen(viewModel);
            }
        }
        if(
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )!= PackageManager.PERMISSION_GRANTED||
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )!= PackageManager.PERMISSION_GRANTED||
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            )!= PackageManager.PERMISSION_GRANTED

        ) {
            verifyMultiplePermissions.launch(
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
                )
            )
        }

        if(
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU&&
            ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_MEDIA_IMAGES)!= PackageManager.PERMISSION_GRANTED){
            verifySinglePermission.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
        }
    }
    val verifyMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){

    }
    val verifySinglePermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){

    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
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