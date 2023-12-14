package pt.isec.amov.tp1.ui.screens.searchview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.ui.composables.ListItems

@Composable
fun SearchLocationView(
    locations: List<Location>,
    modifier: Modifier = Modifier,
    onSelect: (Location)-> Unit,
    onDetails: (Location)-> Unit
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.inversePrimary
                )
            ) {
                Row {
                    Text(text = "A")
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
                }
            }
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.inversePrimary
                )
            ) {
                Row {
                    Text(text = "A")
                    Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "")
                }
            }
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.inversePrimary
                )
            ) {
                Row {
                    Text(text = "Km")
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
                }
            }
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.inversePrimary
                )
            ) {
                Row {
                    Text(text = "Km")
                    Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "")
                }
            }

        }

        ListItems(
            locals = locations,
            onSelected = {
                onSelect(it as Location)
            },
            onDetails = {
                onDetails(it as Location)
            }
        )

    }
}