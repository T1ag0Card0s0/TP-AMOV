package pt.isec.amov.tp1.ui.screens

import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.screens.home.AddNewLocalView
import pt.isec.amov.tp1.ui.screens.home.LocalDetailView
import pt.isec.amov.tp1.ui.screens.home.SearchView
import pt.isec.amov.tp1.ui.screens.login_register.Login
import pt.isec.amov.tp1.ui.screens.login_register.Register
import pt.isec.amov.tp1.ui.screens.login_register.Credits
import pt.isec.amov.tp1.ui.viewmodels.AddLocalForm
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.ItemType
import pt.isec.amov.tp1.ui.viewmodels.LoginForm
import pt.isec.amov.tp1.ui.viewmodels.RegisterForm
import pt.isec.amov.tp1.ui.viewmodels.SearchForm


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: AppViewModel,
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
            Screens.ADD_PLACE_OF_INTEREST.route
        )
        showArrowBack = destination.route in listOf(
            Screens.ADD_LOCATIONS.route,
            Screens.ADD_PLACE_OF_INTEREST.route,
            Screens.SEARCH_PLACES_OF_INTEREST.route,
            Screens.DETAILS.route,
            Screens.CREDITS.route
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
                                viewModel.addLocal()
                                when (Screens.valueOf(currentScreen!!.destination.route!!)) {
                                    Screens.ADD_PLACE_OF_INTEREST -> navController.navigate(Screens.SEARCH_PLACES_OF_INTEREST.route)
                                    Screens.ADD_LOCATIONS -> navController.navigate(Screens.SEARCH_LOCATIONS.route)
                                    else -> {}
                                }
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
                            DropdownMenu(
                                expanded = isExpanded,
                                onDismissRequest = { isExpanded = false }) {
                                DropdownMenuItem(
                                    text = {
                                        Text(stringResource(R.string.logout) )
                                           },
                                    onClick = {
                                        isExpanded=false
                                        navController.navigate(Screens.LOGIN.route)
                                    }
                                )
                            }
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
                viewModel.loginForm = LoginForm()
                Login(
                    viewModel,
                    stringResource(R.string.app_name),
                    navController
                    )
            }
            composable(Screens.REGISTER.route){
                viewModel.registerForm = RegisterForm()
                Register(
                    viewModel,
                    stringResource(R.string.app_name),
                    navController
                )
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
                AddNewLocalView(appViewModel = viewModel)
                
            }
            composable(Screens.ADD_PLACE_OF_INTEREST.route){
                AddNewLocalView(appViewModel = viewModel)
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

@Preview
@Composable
fun MainPreview(){
}