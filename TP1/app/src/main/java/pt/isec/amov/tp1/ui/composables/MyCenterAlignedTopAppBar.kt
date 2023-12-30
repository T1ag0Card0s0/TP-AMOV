package pt.isec.amov.tp1.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCenterAlignedTopAppBarr(
    title: String,
    currentScreen: Screens,
    navController:NavHostController,
    viewModel: AppViewModel,
    showArrowBack:Boolean,
    showDoneIcon: Boolean,
    showMoreVert: Boolean
){
    var isExpanded by remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            if (showArrowBack) {
                IconButton(onClick = {
                    when (currentScreen) {
                        Screens.SEARCH_PLACES_OF_INTEREST -> {
                            navController.navigate(
                                Screens.SEARCH_LOCATIONS.route
                            )
                        }
                        else -> navController.navigateUp()
                    }
                }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (showDoneIcon) {
                IconButton(onClick = {
                    navController.navigateUp()
                    when (currentScreen) {
                        Screens.ADD_PLACE_OF_INTEREST -> {
                            viewModel.addPlaceOfInterest()
                        }
                        Screens.ADD_LOCATIONS -> {
                            viewModel.addLocation()
                        }
                        Screens.EVALUATE_PLACE_OF_INTEREST->{
                            viewModel.addClassification()
                        }
                        Screens.LOCATION_EDIT->{
                            viewModel.editLocation()
                        }
                        Screens.PLACE_OF_INTEREST_EDIT->{
                            viewModel.editPlaceOfInterest()
                        }
                        else -> {}
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done"
                    )
                }
            }
            if (showMoreVert) {
                IconButton(onClick = {
                    isExpanded = !isExpanded
                }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "More Vert"
                    )
                }

                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }) {
                    DropdownMenuItem(
                        text = {
                            if(!viewModel.isMyContributions.value)
                                Text(stringResource(R.string.my_contributions))
                            else
                                Text(stringResource(R.string.show_all_content))
                        },
                        onClick = {
                            viewModel.isMyContributions.value = !viewModel.isMyContributions.value
                            isExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                               Text(stringResource(R.string.credits_title))
                        },
                        onClick = {
                            navController.navigate(Screens.CREDITS.route)
                            isExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.logout))
                        },
                        onClick = {
                            isExpanded = false
                            viewModel.signOut()
                            navController.navigate(Screens.LOGIN.route)

                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.inversePrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.inversePrimary,
            actionIconContentColor = MaterialTheme.colorScheme.inversePrimary
        ),
    )
}