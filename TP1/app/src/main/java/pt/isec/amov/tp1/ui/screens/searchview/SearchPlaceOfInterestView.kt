package pt.isec.amov.tp1.ui.screens.searchview

import CategoryDropDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Cottage
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.data.PlaceOfInterest
import pt.isec.amov.tp1.ui.composables.ListLocals
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.location.LocalViewModel

@Composable
fun SearchPlaceOfInterestView(
    viewModel: AppViewModel,
    locationViewModel: LocalViewModel,
    location: Location,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onDetails: (PlaceOfInterest) -> Unit,
    onEdit: (PlaceOfInterest) -> Unit
) {
    val currentCoordinates = locationViewModel.currentLocation.observeAsState()
    val placesOfInterest = viewModel.placesOfInterest.observeAsState()
    val categories = viewModel.categories.observeAsState()

    var alphabeticOrderByAsc by remember { mutableStateOf(true) }
    var distanceOrderByAsc by remember { mutableStateOf(true) }

    var isExpandedCategories by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxSize()
    ) {
        Box(
            modifier = modifier.fillMaxWidth()
        ) {

            Text(
                text = "In ${location.name}",
                fontSize = 30.sp,
                modifier = modifier.align(Alignment.Center)
            )
            IconButton(
                onClick = {
                    navController.navigate(Screens.PLACES_OF_INTEREST_MAP.route)
                },
                modifier = modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = "Map of placesOfInterest"
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                alphabeticOrderByAsc = !alphabeticOrderByAsc
                viewModel.placesOfInterestOrderByAlphabetically(alphabeticOrderByAsc)
            }) {
                Text(
                    if (alphabeticOrderByAsc) "${stringResource(R.string.name)}: ${stringResource(R.string.ascendant)} "
                    else "${stringResource(R.string.name)}: ${stringResource(R.string.descendant)}"
                )
            }

            Button(onClick = {
                distanceOrderByAsc = !distanceOrderByAsc
                viewModel.placesOfInterestOrderByDistance(
                    currentCoordinates.value!!.latitude,
                    currentCoordinates.value!!.longitude,
                    distanceOrderByAsc
                )
            }) {
                Text(
                    if (distanceOrderByAsc) "${stringResource(R.string.distance)}: ${
                        stringResource(
                            R.string.ascendant
                        )
                    }"
                    else "${stringResource(R.string.distance)}: ${stringResource(R.string.descendant)}"
                )
            }
        }

        CategoryDropDown(
            isExpanded = isExpandedCategories,
            options = categories.value?.map { it.name } ?: emptyList(),
            selectedOption = selectedCategory,
            icons = categories.value?.map { getIconByName(it.iconName ?: "") } ?: emptyList(),
            placeholder = stringResource(R.string.select_categories),
            label = stringResource(R.string.categories),
            onExpandChange = { isExpandedCategories = !isExpandedCategories },
            onDismissRequest = { isExpandedCategories = false },
            onClick = {
                isExpandedCategories = false
                selectedCategory = categories.value!![it].name
                selectedCategoryId = categories.value!![it].id
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 6.dp, start = 3.dp, end = 3.dp)
        )


        if (placesOfInterest.value != null) {
            val filteredPlacesOfInterest = if (selectedCategory.isNotEmpty()) {
                // Filtra os locais de interesse com base na categoria selecionada
                placesOfInterest.value!!.filter {
                    it.categoryId == selectedCategoryId &&
                            (it.locationId == viewModel.selectedLocationId.value) &&
                            (!viewModel.isMyContributions.value || it.authorEmail == viewModel.user.value!!.email)
                }
            } else {
                // Caso nenhuma categoria seja selecionada, exibe todos os locais de interesse
                if (!viewModel.isMyContributions.value) {
                    placesOfInterest.value!!.filter { it.locationId == viewModel.selectedLocationId.value }
                } else {
                    placesOfInterest.value!!.filter {
                        it.locationId == viewModel.selectedLocationId.value &&
                                it.authorEmail == viewModel.user.value!!.email
                    }
                }
            }

            ListLocals(
                locals = filteredPlacesOfInterest,
                userEmail = viewModel.user.value!!.email,
                onSelected = {},
                onDetails = {
                    onDetails(it as PlaceOfInterest)
                },
                onRemove = {
                    viewModel.removePlaceOfInterest(it as PlaceOfInterest)
                },
                onEdit = {local->
                    onEdit(local as PlaceOfInterest)
                }
            )
        }
    }
}

fun getIconByName(iconName: String): ImageVector {
    return when (iconName) {
        "Filled.BeachAccess"-> Icons.Default.BeachAccess
        "Filled.Cottage" -> Icons.Default.Cottage
        "Filled.Museum" -> Icons.Default.Museum
        "Filled.School" -> Icons.Default.School
        "Filled.Home" -> Icons.Default.Home
        "Filled.Person" -> Icons.Default.Person
        "Filled.Hotel" -> Icons.Default.Hotel
        "Filled.Nature" -> Icons.Default.Nature
        "Filled.LocalHospital" -> Icons.Default.LocalHospital
        "Filled.Sports" -> Icons.Default.Sports
        "Filled.Landscape" -> Icons.Default.Landscape
        else -> Icons.Default.Error
    }
}
