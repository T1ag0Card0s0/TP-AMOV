package pt.isec.a2021138999.tp1

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost;
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch

enum class Screens (val display: String, val showAppBar: Boolean){
    LOGIN("Sign in",false),
    REGISTER("Sign up",false),
    CREDITS("Credits",true);
    val route : String
        get() = this.toString()
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController = rememberNavController()){
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
            modifier = Modifier
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