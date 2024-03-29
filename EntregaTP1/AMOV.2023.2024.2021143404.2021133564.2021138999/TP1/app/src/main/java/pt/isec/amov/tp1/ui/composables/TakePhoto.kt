package pt.isec.amov.tp1.ui.composables

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import pt.isec.amov.tp1.utils.file.FileUtils
import java.io.File

@Composable
fun TakePhoto(
    imagePath: MutableState<String>,
    initImage: MutableState<String?> = mutableStateOf(null),
    modifier: Modifier = Modifier
) {
    val context= LocalContext.current
    var tempFile by remember { mutableStateOf("") }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) {
            imagePath.value = ""
            return@rememberLauncherForActivityResult
        }
        initImage.value = null
        imagePath.value = FileUtils.createFileFromUri(context, uri)
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (!success) {
            imagePath.value = ""
            return@rememberLauncherForActivityResult
        }
        imagePath.value = tempFile
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                tempFile = FileUtils.getTempFilename(context)
                val fileUri = FileProvider.getUriForFile(
                    context,
                    "pt.isec.amov.tp1.android.file-provider",
                    File(tempFile)
                )
                cameraLauncher.launch(fileUri)
            }) {
                // Text(text = stringResource(R.string.take_picture))
                Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = "Take photo")
            }
            Button(onClick = { galleryLauncher.launch(PickVisualMediaRequest()) }) {
                Icon(imageVector = Icons.Default.Photo, contentDescription = "Choose from galary")
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if(initImage.value!=null)
                AsyncImage(
                    model = initImage.value,
                    contentDescription = "Background image",
                    modifier = Modifier.matchParentSize()
                )
            else
                AsyncImage(
                    model = imagePath.value,
                    contentDescription = "Background image",
                    modifier = Modifier.matchParentSize()
                )
        }
    }
}