package pt.isec.amov.tp1.ui.screens.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.composables.TakePhoto
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.ItemType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewLocalView(
    appViewModel: AppViewModel,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
){
    var optCategory by remember {
        mutableStateOf("")
    }
    var isExpandedCategories by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .align(Alignment.Center)
        ) {
            OutlinedTextField(
                value = appViewModel.addLocalForm!!.name.value,
                onValueChange = { appViewModel.addLocalForm!!.name.value = it },
                label = {
                    Text(text = stringResource(R.string.name))
                }
            )
            Spacer(modifier = modifier.height(24.dp))
            OutlinedTextField(
                value = appViewModel.addLocalForm!!.descrition.value,
                onValueChange = { appViewModel.addLocalForm!!.descrition.value = it },
                label = {
                    Text(text = stringResource(R.string.description))
                }
            )
            if(appViewModel.searchForm!!.itemType==ItemType.PLACE_OF_INTEREST) {
                Spacer(modifier = modifier.height(24.dp))
                ExposedDropdownMenuBox(
                    expanded = isExpandedCategories,
                    onExpandedChange = {
                        isExpandedCategories = !isExpandedCategories
                    },
                ) {
                    TextField(
                        value = optCategory,
                        onValueChange = {},
                        placeholder = { Text(stringResource(R.string.select_categories)) },
                        label = {
                                Text(
                                    stringResource(R.string.categories),
                                    textAlign = TextAlign.Center
                                )
                                },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedCategories) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isExpandedCategories,
                        onDismissRequest = { isExpandedCategories = false }
                    ) {
                        for (item in appViewModel.getCategories())
                            DropdownMenuItem(
                                text = {
                                    Text(text = item.name)
                                },
                                onClick = {
                                    optCategory = item.name
                                    isExpandedCategories = false
                                    appViewModel.addLocalForm!!.category.value=item
                                }
                            )
                    }
                }
            }
            Spacer(modifier = modifier.height(24.dp))
            IconButton(onClick = { navHostController.navigate(Screens.CHOOSE_COORDINATES.route) }) {
                Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "Location")
            }
            Spacer(modifier = modifier.height(24.dp))
            Box(
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                    .padding(8.dp)
            ) {
                TakePhoto(imagePath = appViewModel.addLocalForm!!.imagePath)
            }
        }
    }
}