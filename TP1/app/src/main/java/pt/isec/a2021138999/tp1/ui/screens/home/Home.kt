package pt.isec.a2021138999.tp1.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import pt.isec.a2021138999.tp1.ui.screens.Screens

enum class BackgroundType{
    LOCATION,
    PLACE_OF_INTEREST,
}
@Composable
fun Home(
    navController: NavHostController?
) {
    Box (
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        LocationSearch()

        Column (
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth(0.3f)
                .fillMaxHeight(0.75f)
        ) {
            Button(
                onClick = { navController?.navigate(Screens.LOGIN.route)},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(160,160,160)
                )
            ) {
                Text(text = "Logout")
            }
        }
    }
}

@Preview
@Composable
fun HomePreview(){
    Home(null)
}