package pt.isec.amov.tp1.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import pt.isec.amov.tp1.ui.composables.MyDropDownMenu
import pt.isec.amov.tp1.ui.screens.home.AddNewLocalView
import pt.isec.amov.tp1.ui.screens.home.ChooseCoordinates
import pt.isec.amov.tp1.ui.screens.home.LocalDetailView
import pt.isec.amov.tp1.ui.screens.home.SearchView
import pt.isec.amov.tp1.ui.screens.login_register.Credits
import pt.isec.amov.tp1.ui.screens.login_register.LoginForm
import pt.isec.amov.tp1.ui.screens.login_register.RegisterForm
import pt.isec.amov.tp1.ui.viewmodels.AddLocalForm
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.FireBaseViewModel
import pt.isec.amov.tp1.ui.viewmodels.ItemType
import pt.isec.amov.tp1.ui.viewmodels.location.LocalViewModel
import pt.isec.amov.tp1.ui.viewmodels.SearchForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: AppViewModel,
    locationViewModel: LocalViewModel,
    fireBaseViewModel: FireBaseViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
){
    val context = LocalContext.current
    val snackbarHostState = remember{ SnackbarHostState() }
    var isExpanded by remember{ mutableStateOf(false) }
    var showDoneIcon by remember { mutableStateOf(false) }
    var showTopBar by remember { mutableStateOf(false) }
    var showArrowBack by remember{ mutableStateOf(false) }
    var showMoreVert by remember{ mutableStateOf(false) }
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
            Screens.DETAILS.route,
            Screens.CREDITS.route,
            Screens.CHOOSE_COORDINATES.route
        )
        showMoreVert = destination.route in listOf(
            Screens.SEARCH_PLACES_OF_INTEREST.route,
            Screens.SEARCH_LOCATIONS.route
        )
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            if(showTopBar)
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = Screens.valueOf(currentScreen!!.destination.route!!).display)
                    },
                    navigationIcon = {
                        if(showArrowBack){
                            IconButton(onClick = {
                                when(Screens.valueOf(currentScreen!!.destination.route!!)){
                                    Screens.SEARCH_PLACES_OF_INTEREST->  navController.navigate(Screens.SEARCH_LOCATIONS.route)
                                    else -> navController.navigateUp()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    actions = {
                        if(showDoneIcon) {
                            IconButton(onClick = {
                                onDoneAction(
                                    currentScreen?.destination?.route,
                                    navController,
                                    viewModel,
                                    context)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done"
                                )
                            }
                        }
                        if(showMoreVert){
                            IconButton(onClick = {
                                isExpanded = !isExpanded
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.MoreVert,
                                    contentDescription = "More Vert"
                                )
                            }
                            MyDropDownMenu(
                                isExpanded = isExpanded,
                                options =  listOf(stringResource(R.string.logout)),
                                onClick = {
                                    if(it.isEmpty())
                                        isExpanded = false
                                    else {
                                        isExpanded=false
                                        fireBaseViewModel.signOut()
                                        navController.navigate(Screens.LOGIN.route)
                                    }
                                }
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor =MaterialTheme.colorScheme.inversePrimary,
                        navigationIconContentColor =MaterialTheme.colorScheme.inversePrimary,
                        actionIconContentColor =MaterialTheme.colorScheme.inversePrimary
                    ),
                )
        },
    ){
        NavHost(
            navController = navController,
            startDestination = Screens.LOGIN.route,
            modifier = modifier
                .padding(it)
        ){
            composable(Screens.LOGIN.route){
                LoginForm(
                    fireBaseViewModel,
                    navController
                ){
                    navController.navigate(Screens.SEARCH_LOCATIONS.route)
                }
            }
            composable(Screens.REGISTER.route){
                RegisterForm(
                    fireBaseViewModel,
                    navController
                ){
                    navController.navigate(Screens.SEARCH_LOCATIONS.route)
                }
            }
            composable(Screens.SEARCH_LOCATIONS.route){
                viewModel.searchForm = SearchForm(itemType = ItemType.LOCATION)
                viewModel.addLocalForm= AddLocalForm(ItemType.LOCATION)
                SearchView(
                    viewModel,
                    navController
                )
            }
            composable(Screens.SEARCH_PLACES_OF_INTEREST.route){
                viewModel.searchForm = SearchForm(ItemType.PLACE_OF_INTEREST)
                viewModel.addLocalForm= AddLocalForm(ItemType.PLACE_OF_INTEREST)
                SearchView(
                    viewModel,
                    navController
                )
            }
            composable(Screens.ADD_LOCATIONS.route){
                AddNewLocalView(appViewModel = viewModel,navController)
                
            }
            composable(Screens.ADD_PLACE_OF_INTEREST.route){
                AddNewLocalView(appViewModel = viewModel,navController)
            }
            composable(Screens.CHOOSE_COORDINATES.route){
                ChooseCoordinates(locationViewModel)
            }
            composable(Screens.DETAILS.route){
                LocalDetailView()
            }
            composable(Screens.CREDITS.route){
                Credits()
            }
        }
    }
}

fun onDoneAction(
    currentScreen: String?,
    navController: NavHostController,
    viewModel: AppViewModel,
    context: Context
){
    when (currentScreen) {
        Screens.ADD_PLACE_OF_INTEREST.route -> {
            if(viewModel.addLocal()) {
                navController.navigate(Screens.SEARCH_PLACES_OF_INTEREST.route)
            }else{
                Toast.makeText(context, "Failed to add", Toast.LENGTH_LONG).show()
            }
        }
        Screens.ADD_LOCATIONS.route ->{
            if(viewModel.addLocal()) {
                navController.navigate(Screens.SEARCH_LOCATIONS.route)
            }else{
                Toast.makeText(context, "Failed to add", Toast.LENGTH_LONG).show()
            }
        }
        Screens.CHOOSE_COORDINATES.route->{

        }
        else->{}
    }
}