package pt.isec.amov.tp1.ui.screens.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.data.AppData
import pt.isec.amov.tp1.ui.composables.TakePhoto
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel

@Composable
fun AddNewLocalViews(
    appViewModel: AppViewModel,
    typeToCreate: String,
    modifier: Modifier = Modifier
){
    var name by remember{
        mutableStateOf(" ")
    }
    var description by remember{
        mutableStateOf(" ")
    }
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .align(Alignment.Center)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = {
                    Text(text = stringResource(R.string.name_label))
                }
            )
            Spacer(modifier = modifier.height(24.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = {
                    Text(text = stringResource(R.string.description))
                }
            )
            Spacer(modifier = modifier.height(24.dp))
            TakePhoto(imagePath = appViewModel.imagePath)
            Button(
                onClick = { /*TODO*/ },
            ) {
                Text(text = stringResource(R.string.submit))
            }
        }
    }
}