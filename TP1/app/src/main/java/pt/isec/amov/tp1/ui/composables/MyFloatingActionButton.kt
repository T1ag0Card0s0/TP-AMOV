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
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Cottage
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var selectedIconName by remember { mutableStateOf("") }
    var showErrorSnackbar by remember { mutableStateOf(false) }
    val categoryIcons = listOf(
        Icons.Default.BeachAccess,
        Icons.Default.Cottage,
        Icons.Default.Museum,
        Icons.Default.School,
        Icons.Default.Home,
        Icons.Default.Person,
        Icons.Default.Hotel,
        Icons.Default.Nature,
        Icons.Default.LocalHospital,
        Icons.Default.Sports,
        Icons.Default.Landscape
    )

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
            Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add))
        }
        // Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Text(text = stringResource(R.string.enter_category_details))
                },
                text = {
                    Column {
                        TextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text(stringResource(R.string.category_name)) }
                        )
                        TextField(
                            value = descriptionInput,
                            onValueChange = { descriptionInput = it },
                            label = { Text(stringResource(R.string.other_input)) }
                        )
                        // Select icon using IconGrid
                        Text(text = stringResource(R.string.select_an_icon_for_the_category))
                        IconGrid(categoryIcons) { selectedIcon ->
                            selectedIconName = selectedIcon.name
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (nameInput.isNotBlank() && descriptionInput.isNotBlank() && selectedIconName.isNotBlank()) {
                                // Todos os dados estão preenchidos, adicione a categoria
                                viewModel.addCategory(nameInput, descriptionInput, selectedIconName)
                                showDialog = false
                            } else {
                                // Mostrar Snackbar com mensagem de erro
                                showErrorSnackbar = true
                            }
                        }
                    ) {
                        Text(stringResource(R.string.add_category))
                    }
                    if (showErrorSnackbar) {
                        Text(
                            text = stringResource(R.string.please_complete_every_field),
                            color = Color.Red,
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}

@Composable
fun IconGrid(
    icons: List<ImageVector>,
    onIconSelected: (ImageVector) -> Unit
) {
    val selectedIcon = remember { mutableStateOf<ImageVector?>(null) }

    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(icons) { icon ->
            val isSelected = icon == selectedIcon.value

            val tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        selectedIcon.value = icon
                        onIconSelected(icon)
                    }
                    .padding(8.dp)
                    .padding(4.dp),
                tint = tint
            )
        }
    }
}