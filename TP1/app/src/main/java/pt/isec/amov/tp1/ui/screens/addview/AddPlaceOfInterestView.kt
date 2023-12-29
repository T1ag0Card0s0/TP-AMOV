package pt.isec.amov.tp1.ui.screens.addview

import android.location.Location
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.data.Category
import pt.isec.amov.tp1.ui.composables.MyExposedDropDownMenu
import pt.isec.amov.tp1.ui.composables.MyTextField
import pt.isec.amov.tp1.ui.composables.TakePhoto
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.viewmodels.AddLocalForm

@Composable
fun AddPlaceOfInterestView(
    addLocalForm: AddLocalForm,
    currLocation: LiveData<Location>,
    categoriesLiveData: LiveData<List<Category>?>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val location = currLocation.observeAsState()
    val categories = categoriesLiveData.observeAsState()
    var optCategory by remember { mutableStateOf("") }
    var isExpandedCategories by remember { mutableStateOf(false) }
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
                value = addLocalForm.name.value,
                onChange = { addLocalForm.name.value = it },
                placeholder = stringResource(R.string.enter_name),
                label = stringResource(R.string.name),
                icon = Icons.Default.Abc
            )

            Spacer(modifier = modifier.height(24.dp))
            MyTextField(
                value = addLocalForm.description.value,
                onChange = { addLocalForm.description.value = it },
                placeholder = stringResource(R.string.enter_description),
                label = stringResource(R.string.description),
                icon = Icons.Default.Abc
            )
            Spacer(modifier = modifier.height(24.dp))
            MyExposedDropDownMenu(
                isExpanded = isExpandedCategories,
                options = categories.value!!.map { it.name },
                selectedOption = optCategory,
                placeholder = stringResource(R.string.select_categories),
                label = stringResource(R.string.categories),
                onExpandChange = { isExpandedCategories = !isExpandedCategories },
                onDismissRequest = {
                    isExpandedCategories = false
                },
                onClick = {
                    optCategory = categories.value!![it].name
                    isExpandedCategories = false
                    addLocalForm.category.value =
                        categories.value!!.find { c -> c.name == optCategory }!!.id
                },
                modifier = modifier.padding(start = 6.dp, end = 6.dp)
            )
            Spacer(modifier = modifier.height(24.dp))
            Row {
                Button(onClick = {
                    addLocalForm.latitude.value = location.value!!.latitude
                    addLocalForm.longitude.value = location.value!!.longitude
                }) {
                    Text(text = stringResource(R.string.current_location))
                }
                Spacer(modifier = modifier.width(8.dp))
                Button(
                    onClick = {
                        navController.navigate(Screens.CHOOSE_PLACE_OF_INTEREST_COORDINATES.route)
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
                TakePhoto(
                    imagePath = addLocalForm.imagePath,
                    initImage = addLocalForm.imageUri
                )
            }
        }
    }
}