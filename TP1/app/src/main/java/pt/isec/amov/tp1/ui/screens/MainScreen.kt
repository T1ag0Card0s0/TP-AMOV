package pt.isec.amov.tp1.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pt.isec.amov.tp1.App
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.screens.home.BackgroundType
import pt.isec.amov.tp1.ui.screens.home.Home
import pt.isec.amov.tp1.ui.screens.login_register.Login
import pt.isec.amov.tp1.ui.screens.login_register.Register
import pt.isec.amov.tp1.ui.screens.login_register.Credits
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.AppViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()

){
    val context = LocalContext.current
    val app = context.applicationContext as App

    var appViewModel: AppViewModel?
    val snackbarHostState = remember{ SnackbarHostState() }

    var isExpanded by remember { mutableStateOf(false) }
    var pageTitle by remember { mutableStateOf("") }
    var showTopBar by remember { mutableStateOf(false) }
    var showArrowBack by remember{ mutableStateOf(false) }
    var showMoreVert by remember{ mutableStateOf(false) }
    navController.addOnDestinationChangedListener { _, destination, _ ->
        pageTitle=Screens.valueOf(destination.route!!).display
        showTopBar = Screens.valueOf(destination.route!!).showAppBar
        showArrowBack = destination.route in listOf(
            Screens.PLACE_OF_INTEREST_SEARCH.route,
            Screens.CREDITS.route
        )
        showMoreVert = destination.route in listOf(
            Screens.PLACE_OF_INTEREST_SEARCH.route,
            Screens.LOCATION_SEARCH.route
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
                        Text(
                            text = pageTitle
                        ) },
                    navigationIcon = {
                        if(showArrowBack)
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    Icons.Filled.ArrowBack,
                                    contentDescription = "Back")
                            }

                    },
                    actions = {
                        if(showMoreVert)
                              IconButton(onClick = {isExpanded = !isExpanded}) {
                                  Icon(
                                      Icons.Filled.MoreVert,
                                      contentDescription = "More actions"
                                  )
                              }
                        DropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false },
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text("Logout")
                                },
                                onClick = {
                                    isExpanded=false
                                    navController.navigate(Screens.LOGIN.route)
                                          },
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
        }
    ){
        NavHost(
            navController = navController,
            startDestination = Screens.LOGIN.route,
            modifier = modifier
                .padding(it)
        ){
            composable(Screens.LOGIN.route){
                Login(
                    stringResource(R.string.app_name),
                    navController
                    )
            }
            composable(Screens.REGISTER.route){
                Register(
                    stringResource(R.string.app_name),
                    navController
                )
            }
            composable(Screens.LOCATION_SEARCH.route){

                appViewModel = viewModel(factory= AppViewModelFactory(app.appData))
                Home(
                    appViewModel!!,
                    BackgroundType.LOCATION,
                    navController,
                    stringResource(R.string.alphabetic),
                    stringResource(R.string.distance)
                )
            }
            composable(Screens.PLACE_OF_INTEREST_SEARCH.route){
                appViewModel = viewModel(factory= AppViewModelFactory(app.appData))
                Home(
                    appViewModel!!,
                    BackgroundType.PLACE_OF_INTEREST,
                    navController,
                    stringResource(R.string.alphabetic),
                    stringResource(R.string.distance),
                    stringResource(R.string.categories)
                )
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