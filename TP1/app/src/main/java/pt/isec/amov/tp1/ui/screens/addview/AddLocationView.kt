package pt.isec.amov.tp1.ui.screens.addview

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.composables.MyTextField
import pt.isec.amov.tp1.ui.composables.TakePhoto
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.location.LocalViewModel

@Composable
fun AddLocationView(
    appViewModel: AppViewModel,
    locationViewModel: LocalViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    val location = locationViewModel.currentLocation.observeAsState()
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .align(Alignment.Center)
        ) {
            MyTextField(
                value = appViewModel.addLocalForm!!.name.value,
                onChange = { appViewModel.addLocalForm!!.name.value = it },
                placeholder = stringResource(R.string.enter_name),
                label = stringResource(R.string.name),
                icon = Icons.Default.Abc
            )

            Spacer(modifier = modifier.height(24.dp))
            MyTextField(
                value = appViewModel.addLocalForm!!.descrition.value,
                onChange = { appViewModel.addLocalForm!!.descrition.value = it },
                placeholder = stringResource(R.string.enter_description),
                label = stringResource(R.string.description),
                icon = Icons.Default.Abc
            )
            Spacer(modifier = modifier.height(24.dp))
            Row {
                Button(onClick = {
                    appViewModel.addLocalForm!!.latitude.value=location.value!!.latitude
                    appViewModel.addLocalForm!!.longitude.value=location.value!!.longitude
                }) {
                    Text(text = stringResource(R.string.current_location))
                }
                Spacer(modifier = modifier.width(8.dp))
                Button(
                    onClick = {
                        navController.navigate(Screens.CHOOSE_LOCATION_COORDINATES.route)
                    }
                ) {
                    Text(stringResource(R.string.choose_location))
                }
            }
            Spacer(modifier = modifier.height(24.dp))
            Box(
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .padding(8.dp)
            ) {
                TakePhoto(imagePath = appViewModel.addLocalForm!!.imagePath)
            }
        }
    }
}