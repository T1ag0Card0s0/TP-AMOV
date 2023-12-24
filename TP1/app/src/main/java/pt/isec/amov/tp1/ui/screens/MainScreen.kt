package pt.isec.amov.tp1.ui.screens


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import pt.isec.amov.tp1.data.Local
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
import pt.isec.amov.tp1.ui.viewmodels.location.LocalViewModel

@Composable
fun MainScreen(
    viewModel: AppViewModel,
    locationViewModel: LocalViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    viewModel.startObserver()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDoneIcon by remember { mutableStateOf(false) }
    var showTopBar by remember { mutableStateOf(false) }
    var showArrowBack by remember { mutableStateOf(false) }
    var showMoreVert by remember { mutableStateOf(false) }
    var showFloatingActionButton by remember { mutableStateOf(false)}
    val currentScreen by navController.currentBackStackEntryAsState()
    navController.addOnDestinationChangedListener { _, destination, _ ->
        showTopBar = Screens.valueOf(destination.route!!).showAppBar
        showDoneIcon = destination.route in listOf(
            Screens.ADD_LOCATIONS.route,
            Screens.ADD_PLACE_OF_INTEREST.route,
            Screens.CHOOSE_LOCATION_COORDINATES.route,
            Screens.CHOOSE_PLACE_OF_INTEREST_COORDINATES.route
        )
        showArrowBack = destination.route in listOf(
            Screens.ADD_LOCATIONS.route,
            Screens.ADD_PLACE_OF_INTEREST.route,
            Screens.SEARCH_PLACES_OF_INTEREST.route,
            Screens.LOCATION_DETAILS.route,
            Screens.PLACE_OF_INTEREST_DETAILS.route,
            Screens.CREDITS.route,
            Screens.CHOOSE_LOCATION_COORDINATES.route,
            Screens.CHOOSE_PLACE_OF_INTEREST_COORDINATES.route
        )
        showMoreVert = destination.route in listOf(
            Screens.SEARCH_PLACES_OF_INTEREST.route,
            Screens.SEARCH_LOCATIONS.route
        )
        showFloatingActionButton = destination.route in listOf(
            Screens.SEARCH_LOCATIONS.route,
            Screens.SEARCH_PLACES_OF_INTEREST.route
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
                    showArrowBack,
                    showDoneIcon,
                    showMoreVert
                )
        },
        floatingActionButton = {
            if(showFloatingActionButton)
                MyFloatingActionButton(
                    viewModel = viewModel,
                    navController = navController,
                    currentScreen = Screens.valueOf(currentScreen!!.destination.route!!)
                )
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
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable(Screens.REGISTER.route) {
                RegisterForm(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable(Screens.SEARCH_LOCATIONS.route) {
                SearchLocationView(
                    viewModel = viewModel,
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
                    viewModel = viewModel,
                    location = viewModel.selectedLocation.value!!,
                    onDetails = { placeOfInterest ->
                        viewModel.selecedPlaceOfInterest.value = placeOfInterest
                        navController.navigate(Screens.PLACE_OF_INTEREST_DETAILS.route)
                    },
                )
            }
            composable(Screens.ADD_LOCATIONS.route) {
                AddLocationView(
                    appViewModel = viewModel,
                    navController = navController,
                    locationViewModel = locationViewModel
                )
            }
            composable(Screens.ADD_PLACE_OF_INTEREST.route) {
                AddPlaceOfInterestView(
                    appViewModel = viewModel,
                    navController = navController,
                    locationViewModel = locationViewModel
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
            composable(Screens.CHOOSE_LOCATION_COORDINATES.route) {
                val locations = viewModel.locations.observeAsState()
                ChooseCoordinates(
                    locationViewModel = locationViewModel,
                    viewModel = viewModel,
                    locals = locations.value!!
                )
            }
            composable(Screens.CHOOSE_PLACE_OF_INTEREST_COORDINATES.route){
                val placesOfInterest = viewModel.placesOfInterest.observeAsState()
                ChooseCoordinates(
                    locationViewModel = locationViewModel,
                    viewModel = viewModel,
                    locals = placesOfInterest.value!!
                )
            }
            composable(Screens.CREDITS.route) {
                Credits()
            }
        }
    }
}