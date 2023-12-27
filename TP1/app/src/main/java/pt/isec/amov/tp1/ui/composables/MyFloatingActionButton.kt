package pt.isec.amov.tp1.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.viewmodels.AddLocalForm
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel

@Composable
fun MyFloatingActionButton(
    viewModel: AppViewModel,
    navController: NavHostController,
    currentScreen: Screens
){
    var showMoreAddOptions by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf("") }
    var descriptionInput by remember { mutableStateOf("") }
    var selectedImageVector by remember { mutableStateOf<ImageVector?>(null) }

    Column(
        verticalArrangement = Arrangement.SpaceAround
    ) {
        DropdownMenu(
            expanded = showMoreAddOptions,
            onDismissRequest = { showMoreAddOptions = false }) {
            DropdownMenuItem(
                text = {
                    Text(stringResource(R.string.categories))
                },
                onClick = {
                    showMoreAddOptions = false
                    showDialog = true
                }
            )
            DropdownMenuItem(
                text = {
                    Text(stringResource(R.string.places_of_interest))
                },
                onClick = {
                    showMoreAddOptions = false
                    viewModel.addLocalForm = AddLocalForm()
                    navController.navigate(Screens.ADD_PLACE_OF_INTEREST.route)
                }
            )
        }
        FloatingActionButton(onClick = {
            when(currentScreen){
                Screens.SEARCH_LOCATIONS->{
                    viewModel.addLocalForm = AddLocalForm()
                    navController.navigate(Screens.ADD_LOCATIONS.route)
                }
                Screens.SEARCH_PLACES_OF_INTEREST->{
                    showMoreAddOptions = !showMoreAddOptions
                }
                else->{}
            }
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
        // Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Text(text = "Enter Category Details")
                },
                text = {
                    Column {
                        TextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text("Category Name") }
                        )
                        TextField(
                            value = descriptionInput,
                            onValueChange = { descriptionInput = it },
                            label = { Text("Other Input") }
                        )
                        // Select icon using IconGrid
                        Text(text = stringResource(R.string.select_an_icon_for_the_category))
                        IconGrid { selectedIcon ->
                            selectedImageVector = selectedIcon
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.addCategory(nameInput, descriptionInput, selectedImageVector!!)
                            showDialog = false
                        }
                    ) {
                        Text("Add Category")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun IconGrid(onIconSelected: (ImageVector) -> Unit) {
    val imageVectors = listOf(Icons.Default.Add, Icons.Default.Person, Icons.Default.Home)

    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(imageVectors) { icon ->
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        onIconSelected(icon)
                    }
                    .padding(8.dp)
            )
        }
    }
}

