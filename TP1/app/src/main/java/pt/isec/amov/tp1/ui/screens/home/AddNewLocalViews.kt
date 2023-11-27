package pt.isec.amov.tp1.ui.screens.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.composables.TakePhoto
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel

@Composable
fun AddNewLocalViews(
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier
){
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
                value = appViewModel.addLocalForm!!.name.value,
                onValueChange = { appViewModel.addLocalForm!!.name.value = it },
                label = {
                    Text(text = stringResource(R.string.name_label))
                }
            )
            Spacer(modifier = modifier.height(24.dp))
            OutlinedTextField(
                value = appViewModel.addLocalForm!!.descrition.value,
                onValueChange = { appViewModel.addLocalForm!!.descrition.value = it },
                label = {
                    Text(text = stringResource(R.string.description))
                }
            )
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