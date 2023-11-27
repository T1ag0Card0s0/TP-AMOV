package pt.isec.amov.tp1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import pt.isec.amov.tp1.App
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.screens.home.AddNewLocalViews
import pt.isec.amov.tp1.ui.screens.home.SearchViews
import pt.isec.amov.tp1.ui.screens.login_register.Login
import pt.isec.amov.tp1.ui.screens.login_register.Register
import pt.isec.amov.tp1.ui.screens.login_register.Credits
import pt.isec.amov.tp1.ui.viewmodels.AddLocalForm
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.AppViewModelFactory
import pt.isec.amov.tp1.ui.viewmodels.ItemType
import pt.isec.amov.tp1.ui.viewmodels.LoginForm
import pt.isec.amov.tp1.ui.viewmodels.RegisterForm
import pt.isec.amov.tp1.ui.viewmodels.SearchForm


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()

){
    val context = LocalContext.current
    val app = context.applicationContext as App

    var appViewModel: AppViewModel= viewModel(factory = AppViewModelFactory(app.appData))
    val snackbarHostState = remember{ SnackbarHostState() }
    var showDoneIcon by remember { mutableStateOf(false) }
    var showTopBar by remember { mutableStateOf(false) }
    var showBottomBar by remember{ mutableStateOf(false) }
    val currentScreen by navController.currentBackStackEntryAsState()
    navController.addOnDestinationChangedListener { _, destination, _ ->
        showTopBar = Screens.valueOf(destination.route!!).showAppBar
        showBottomBar = destination.route in listOf(
           Screens.ADD_LOCATIONS.route,
           Screens.ADD_PLACE_OF_INTEREST.route,
           Screens.SEARCH_LOCATIONS.route,
           Screens.SEARCH_PLACES_OF_INTEREST.route
        )
        showDoneIcon = destination.route in listOf(
            Screens.ADD_LOCATIONS.route,
            Screens.ADD_PLACE_OF_INTEREST.route
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
                    actions = {
                              if(showDoneIcon)
                                  IconButton(onClick = {
                                      appViewModel.addLocal()
                                      when(Screens.valueOf(currentScreen!!.destination.route!!).route){
                                          Screens.ADD_PLACE_OF_INTEREST.route-> navController.navigate(Screens.SEARCH_PLACES_OF_INTEREST.route)
                                          Screens.ADD_LOCATIONS.route-> navController.navigate(Screens.SEARCH_LOCATIONS.route)
                                      }
                                  }) {
                                      Icon(
                                          imageVector = Icons.Filled.Done,
                                          contentDescription = "Done"
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
        bottomBar = {
            if(showBottomBar)
                BottomAppBar(modifier = Modifier.height(50.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        IconButton(onClick = { navController.navigate(Screens.SEARCH_LOCATIONS.route)}) {
                            Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home")
                        }
                        IconButton(onClick = {
                            when(Screens.valueOf(currentScreen!!.destination.route!!).route){
                                Screens.ADD_PLACE_OF_INTEREST.route-> navController.navigate(Screens.SEARCH_PLACES_OF_INTEREST.route)
                                Screens.ADD_LOCATIONS.route-> navController.navigate(Screens.SEARCH_LOCATIONS.route)
                            }
                        }) {
                            Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search")
                        }
                        IconButton(onClick = {
                            when(Screens.valueOf(currentScreen!!.destination.route!!).route){
                                Screens.SEARCH_PLACES_OF_INTEREST.route-> navController.navigate(Screens.ADD_PLACE_OF_INTEREST.route)
                                Screens.SEARCH_LOCATIONS.route-> navController.navigate(Screens.ADD_LOCATIONS.route)
                            }
                        }) {
                            Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add")
                        }
                        IconButton(onClick = {

                        }) {
                            Icon(imageVector = Icons.Outlined.Person, contentDescription = "Profile")
                        }
                    }
                }
        }
    ){
        NavHost(
            navController = navController,
            startDestination = Screens.LOGIN.route,
            modifier = modifier
                .padding(it)
        ){
            composable(Screens.LOGIN.route){
                appViewModel.loginForm = LoginForm()
                Login(
                    appViewModel,
                    stringResource(R.string.app_name),
                    navController
                    )
            }
            composable(Screens.REGISTER.route){
                appViewModel.registerForm = RegisterForm()
                Register(
                    appViewModel,
                    stringResource(R.string.app_name),
                    navController
                )
            }
            composable(Screens.SEARCH_LOCATIONS.route){
                appViewModel.searchForm = SearchForm(ItemType.LOCATION)
                appViewModel.addLocalForm= AddLocalForm(ItemType.LOCATION)
                SearchViews(
                    appViewModel,
                    navController
                )
            }
            composable(Screens.SEARCH_PLACES_OF_INTEREST.route){
                appViewModel.searchForm = SearchForm(ItemType.PLACE_OF_INTEREST)
                appViewModel.addLocalForm= AddLocalForm(ItemType.PLACE_OF_INTEREST)
                SearchViews(
                    appViewModel,
                    navController
                )
            }
            composable(Screens.ADD_LOCATIONS.route){
                AddNewLocalViews(appViewModel = appViewModel)
                
            }
            composable(Screens.ADD_PLACE_OF_INTEREST.route){
                AddNewLocalViews(appViewModel = appViewModel)
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
    MainScreen()
}