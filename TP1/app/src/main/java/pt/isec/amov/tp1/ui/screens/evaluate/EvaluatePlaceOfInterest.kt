package pt.isec.amov.tp1.ui.screens.evaluate

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.composables.MyTextField
import pt.isec.amov.tp1.ui.composables.TakePhoto
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.EvaluateForm

@Composable
fun EvaluatePlaceOfInterest(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    var selectedRating by remember { mutableStateOf(0) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        MyTextField(
            value = viewModel.evaluateForm!!.comment.value,
            onChange = { viewModel.evaluateForm!!.comment.value = it } ,
            placeholder = stringResource(R.string.enter_comment),
            label = stringResource(R.string.comment) ,
            icon = Icons.Default.Abc
        )
        Row {
            // Star rating system
            (1..3).forEach { index ->
                IconButton(
                    onClick = {
                        selectedRating = if (selectedRating == index) {
                            0
                        } else {
                            index
                        }
                        viewModel.evaluateForm?.value?.value = index
                    }
                ) {
                    if (index <= selectedRating) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.Yellow
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.StarBorder,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            }
        }
        Box(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .padding(8.dp)
        ) {
            TakePhoto(imagePath = viewModel.evaluateForm!!.imagePath)
        }
    }
}