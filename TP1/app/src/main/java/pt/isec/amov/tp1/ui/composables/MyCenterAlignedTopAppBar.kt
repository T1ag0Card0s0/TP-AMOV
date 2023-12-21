package pt.isec.amov.tp1.ui.composables

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.data.PlaceOfInterest
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.FireBaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCenterAlignedTopAppBarr(
    title: String,
    currentScreen: Screens,
    navController:NavHostController,
    viewModel: AppViewModel,
    fireBaseViewModel: FireBaseViewModel,
    showArrowBack:Boolean,
    showDoneIcon: Boolean,
    showMoreVert: Boolean
){
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            if (showArrowBack) {
                IconButton(onClick = {
                    when (currentScreen) {
                        Screens.SEARCH_PLACES_OF_INTEREST, Screens.MY_CONTRIBUTIONS -> {
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
                    when (currentScreen) {
                        Screens.ADD_PLACE_OF_INTEREST -> {
                            fireBaseViewModel.addPlaceOfInterestToFireStore(
                                viewModel.buildPlaceOfInterestFromAddForm()!!
                            )
                        }
                        Screens.ADD_LOCATIONS -> {
                            fireBaseViewModel.addLocationToFireStore(
                                viewModel.buildLocationFromAddForm()!!
                            )
                        }
                        Screens.CHOOSE_COORDINATES -> {
                            navController.navigateUp()
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
                            Text(stringResource(R.string.my_contributions))
                        },
                        onClick = {
                            isExpanded = false
                            navController.navigate(Screens.MY_CONTRIBUTIONS.route)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.logout))
                        },
                        onClick = {
                            isExpanded = false
                            fireBaseViewModel.signOut()
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