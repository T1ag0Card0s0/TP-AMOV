package pt.isec.amov.tp1.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.composables.MyCenterAlignedTopAppBarr
import pt.isec.amov.tp1.ui.composables.MyFloatingActionButton
import pt.isec.amov.tp1.ui.screens.addview.AddLocationView
import pt.isec.amov.tp1.ui.screens.addview.AddPlaceOfInterestView
import pt.isec.amov.tp1.ui.screens.detailview.LocationDetailsView
import pt.isec.amov.tp1.ui.screens.detailview.PlaceOfInterestDetailsView
import pt.isec.amov.tp1.ui.screens.login_register.Credits
import pt.isec.amov.tp1.ui.screens.login_register.LoginForm
import pt.isec.amov.tp1.ui.screens.login_register.RegisterForm
import pt.isec.amov.tp1.ui.screens.searchview.SearchLocationView
import pt.isec.amov.tp1.ui.screens.searchview.SearchPlaceOfInterestView
import pt.isec.amov.tp1.ui.viewmodels.AddLocalForm
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.FireBaseViewModel
import pt.isec.amov.tp1.ui.viewmodels.ItemType
import pt.isec.amov.tp1.ui.viewmodels.location.LocalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: AppViewModel,
    locationViewModel: LocalViewModel,
    fireBaseViewModel: FireBaseViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {

    val snackbarHostState = remember { SnackbarHostState() }
    var showDoneIcon by remember { mutableStateOf(false) }
    var showTopBar by remember { mutableStateOf(false) }
    var showArrowBack by remember { mutableStateOf(false) }
    var showMoreVert by remember { mutableStateOf(false) }
    var showFloatingButton by remember { mutableStateOf(false) }
    val currentScreen by navController.currentBackStackEntryAsState()
    navController.addOnDestinationChangedListener { _, destination, _ ->
        showTopBar = Screens.valueOf(destination.route!!).showAppBar
        showDoneIcon = destination.route in listOf(
            Screens.ADD_LOCATIONS.route,
            Screens.ADD_PLACE_OF_INTEREST.route,
            Screens.CHOOSE_COORDINATES.route
        )
        showArrowBack = destination.route in listOf(
            Screens.ADD_LOCATIONS.route,
            Screens.ADD_PLACE_OF_INTEREST.route,
            Screens.SEARCH_PLACES_OF_INTEREST.route,
            Screens.LOCATION_DETAILS.route,
            Screens.PLACE_OF_INTEREST_DETAILS.route,
            Screens.CREDITS.route,
            Screens.CHOOSE_COORDINATES.route,
            Screens.MY_CONTRIBUTIONS.route
        )
        showMoreVert = destination.route in listOf(
            Screens.SEARCH_PLACES_OF_INTEREST.route,
            Screens.SEARCH_LOCATIONS.route
        )
        showFloatingButton = destination.route in listOf(
            Screens.SEARCH_PLACES_OF_INTEREST.route,
            Screens.SEARCH_LOCATIONS.route,
            Screens.MY_CONTRIBUTIONS.route
        )
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            if (showTopBar)
                MyCenterAlignedTopAppBarr(
                    Screens.valueOf(currentScreen!!.destination.route!!).display,
                    Screens.valueOf(currentScreen!!.destination.route!!),
                    navController,
                    viewModel,
                    fireBaseViewModel,
                    showArrowBack,
                    showDoneIcon,
                    showMoreVert
                )
        } ,
        floatingActionButton = {
            if (showFloatingButton) {
                MyFloatingActionButton(
                    viewModel,
                    Screens.valueOf(currentScreen!!.destination.route!!),
                    navController
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screens.LOGIN.route,
            modifier = modifier
                .padding(it)
        ) {
            composable(Screens.LOGIN.route) {
                LoginForm(
                    fireBaseViewModel = fireBaseViewModel,
                    navController = navController
                )
            }
            composable(Screens.REGISTER.route) {
                RegisterForm(
                    fireBaseViewModel = fireBaseViewModel,
                    navController = navController
                )
            }
            composable(Screens.SEARCH_LOCATIONS.route) {
                SearchLocationView(
                    locations = viewModel.getLocations(),
                    onSelect = { location ->
                        viewModel.selectedLocation.value = location
                        navController.navigate(Screens.SEARCH_PLACES_OF_INTEREST.route)
                    },
                    onDetails = { location ->
                        viewModel.selectedLocation.value = location
                        navController.navigate(Screens.LOCATION_DETAILS.route)
                    }
                )
            }
            composable(Screens.SEARCH_PLACES_OF_INTEREST.route) {
                SearchPlaceOfInterestView(
                    placesOfInterest = viewModel.selectedLocation.value!!.placesOfInterest,
                    categories = viewModel.getCategories(),
                    location = viewModel.selectedLocation.value!!,
                    onDetails = { placeOfInterest ->
                        viewModel.selecedPlaceOfInterest.value = placeOfInterest
                        navController.navigate(Screens.PLACE_OF_INTEREST_DETAILS.route)
                    },
                )
            }
            composable(Screens.ADD_LOCATIONS.route) {
                viewModel.addLocalForm = AddLocalForm()
                AddLocationView(
                    appViewModel = viewModel,
                    navController = navController
                )
            }
            composable(Screens.ADD_PLACE_OF_INTEREST.route) {
                viewModel.addLocalForm = AddLocalForm()
                AddPlaceOfInterestView(
                    appViewModel = viewModel,
                    navController = navController
                )
            }
            composable(Screens.LOCATION_DETAILS.route) {
                LocationDetailsView(
                    location = viewModel.selectedLocation.value!!,
                )
            }
            composable(Screens.PLACE_OF_INTEREST_DETAILS.route) {
                PlaceOfInterestDetailsView(
                    placeOfInterest = viewModel.selecedPlaceOfInterest.value!!,
                )
            }
            composable(Screens.MY_CONTRIBUTIONS.route) {
                MyContributionsView()
            }
            composable(Screens.CHOOSE_COORDINATES.route) {
                ChooseCoordinates(
                    locationViewModel = locationViewModel
                )
            }
            composable(Screens.CREDITS.route) {
                Credits()
            }
        }
    }
}