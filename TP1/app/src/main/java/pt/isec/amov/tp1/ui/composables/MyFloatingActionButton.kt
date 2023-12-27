package pt.isec.amov.tp1.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.res.stringResource
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
    var tempSelectedEmoji by remember { mutableStateOf("") }
    var showDialogEmojiSelector by remember { mutableStateOf(false) }

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
                    showDialog=true
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
                    showMoreAddOptions=!showMoreAddOptions
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
                        TextField(
                            value = tempSelectedEmoji,
                            onValueChange = { tempSelectedEmoji = it },
                            label = { Text("Selected Emoji") },
                            readOnly = true, // Altere para true para impedir a edição direta
                            modifier = Modifier.clickable {
                                showDialogEmojiSelector = true
                            }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.addCategory(nameInput, descriptionInput, tempSelectedEmoji)
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

        if (showDialogEmojiSelector) {
            EmojiPickerView()
        }
    }
}