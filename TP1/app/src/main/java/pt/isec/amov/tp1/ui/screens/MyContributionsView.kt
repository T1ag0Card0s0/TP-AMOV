package pt.isec.amov.tp1.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.data.Category
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.data.PlaceOfInterest
import pt.isec.amov.tp1.ui.composables.ListCategories
import pt.isec.amov.tp1.ui.composables.ListLocals
import pt.isec.amov.tp1.ui.composables.MyExposedDropDownMenu
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.FireBaseViewModel
import java.util.UUID

@Composable
fun MyContributionsView(
    appViewModel: AppViewModel,
    fireBaseViewModel: FireBaseViewModel,
    viewModel: AppViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var myCategories = appViewModel.getCategories().observeAsState()
    var isExpanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf("") }
    var descriptionInput by remember { mutableStateOf("") }
    var isExpandedLocationMoreVert by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        MyExposedDropDownMenu(
            isExpanded = isExpanded,
            options = listOf(
                stringResource(R.string.locations),
                stringResource(R.string.places_of_interest),
                stringResource(R.string.categories),
            ),
            selectedOption = selectedOption,
            placeholder = stringResource(R.string.select_one_option),
            label = stringResource(R.string.my_contributions),
            onExpandChange = { isExpanded = !isExpanded },
            onDismissRequest = { isExpanded = false },
            onClick = {
                isExpanded = false
                selectedOption = it
            },
            modifier = modifier.fillMaxWidth()
        )
        when (selectedOption) {
            stringResource(R.string.locations) -> {
                Box(modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp) ){
                    Column {
                        ListLocals(
                            locals = viewModel.getMyLocations(),
                            onSelected = {

                            },
                            onDetails = {location->
                                isExpandedLocationMoreVert = true
                                viewModel.selectedLocation.value = location as Location
                            }
                        )
                        DropdownMenu(
                            expanded = isExpandedLocationMoreVert,
                            onDismissRequest = { isExpandedLocationMoreVert = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.add_place_of_interest)) },
                                onClick = {
                                    isExpandedLocationMoreVert = false
                                    navController.navigate(Screens.ADD_PLACE_OF_INTEREST.route)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.details)) },
                                onClick = {
                                    isExpandedLocationMoreVert = false
                                    navController.navigate(Screens.LOCATION_DETAILS.route)
                                }
                            )
                        }
                    }

                    Button(
                        onClick = {
                            navController.navigate(Screens.ADD_LOCATIONS.route)
                        },
                        modifier = modifier.align(Alignment.BottomEnd)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    }
                }

            }

            stringResource(R.string.places_of_interest) -> {
                Box(modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp) ){
                    ListLocals(
                        locals = viewModel.getMyPlacesOfInterest(),
                        onSelected = {},
                        onDetails = {placeOfInterest->
                            viewModel.selecedPlaceOfInterest.value = placeOfInterest as PlaceOfInterest
                            navController.navigate(Screens.PLACE_OF_INTEREST_DETAILS.route)
                        }
                    )
                }
            }

            stringResource(R.string.categories) -> {
                Box(modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp) ){
                    ListCategories(
                        categories = myCategories.value!!.filter { it.authorEmail == fireBaseViewModel.user.value!!.email },
                        onSelected = {},
                        onDetails = {}
                    )
                    Button(
                        onClick = {
                            showDialog=true
                        },
                        modifier = modifier.align(Alignment.BottomEnd)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    }
                }
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialog = false
                        },
                        title = {
                            Text(text = "Enter Category Details")
                        },
                        text = {
                            Column {
                                TextField(
                                    value = nameInput,
                                    onValueChange = { nameInput = it },
                                    label = { Text("Category Name") }
                                )
                                TextField(
                                    value = descriptionInput,
                                    onValueChange = { descriptionInput = it },
                                    label = { Text("Other Input") }
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    fireBaseViewModel.addCategoryToFireStore(
                                        Category(
                                            UUID.randomUUID().toString(),
                                            viewModel.appData.user.value!!.email,
                                            nameInput,
                                            descriptionInput
                                        )
                                    )
                                    showDialog = false
                                }
                            ) {
                                Text("Add Category")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    showDialog = false
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        }
    }
}