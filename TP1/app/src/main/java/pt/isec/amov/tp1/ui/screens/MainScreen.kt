package pt.isec.amov.tp1.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost;
import androidx.navigation.compose.composable
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.data.AppData
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
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
){
    val app = AppData()

    val snackbarHostState = remember{ SnackbarHostState() }
    var showTopBar by remember { mutableStateOf(false) }
    navController.addOnDestinationChangedListener { controller, destination, arguments ->
        showTopBar = Screens.valueOf(destination.route!!).showAppBar
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            if(showTopBar)
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.app_name),
                        ) },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
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
                    navController,
                    Screens.REGISTER.route,
                    Screens.CREDITS.route
                    )
            }
            composable(Screens.REGISTER.route){
                Register(
                    stringResource(R.string.app_name),
                    navController,
                    Screens.LOGIN.route,
                    Screens.CREDITS.route
                )
            }
            composable(Screens.LOCATION_SEARCH.route){
                Home(app,BackgroundType.LOCATION,navController)
            }
            composable(Screens.PLACE_OF_INTEREST_SEARCH.route){
                Home(app,BackgroundType.PLACE_OF_INTEREST,navController)
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
    MainScreen();
}