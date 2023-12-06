package pt.isec.amov.tp1.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.composables.ListItems
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.ItemType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    appViewModel: AppViewModel,
    navHostController: NavHostController?,
    modifier: Modifier = Modifier
) {
    var isExpandedOrderby by remember {
        mutableStateOf(false)
    }
    var isExpandedCategories by remember {
        mutableStateOf(false)
    }
    val options =listOf(stringResource(R.string.alphabetic), stringResource(R.string.distance))
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

        if(appViewModel.searchForm!!.itemType==ItemType.PLACE_OF_INTEREST)
            Text(
                text = "In ${appViewModel.getSelectedLocalName()}",
                fontSize = 30.sp
            )
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = modifier.fillMaxWidth()
        ){
            /*ExposedDropdownMenuBox(
                expanded = isExpandedOrderby,
                onExpandedChange = {
                    isExpandedOrderby = !isExpandedOrderby
                },
                modifier = modifier.fillMaxWidth(0.5f)
            ) {
                TextField(
                    value = appViewModel.searchForm!!.orderByOption.value,
                    placeholder = { Text(stringResource(R.string.orderBy))},
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedOrderby) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = isExpandedOrderby,
                    onDismissRequest = { isExpandedOrderby = false }
                ) {
                    for (itemName in options)
                        DropdownMenuItem(
                            text = {
                                Text(text = itemName)
                            },
                            onClick = {
                                appViewModel.searchForm!!.orderByOption.value = itemName
                                isExpandedOrderby = false
                            }
                        )
                }
            }*/
            if(appViewModel.searchForm!!.itemType==ItemType.PLACE_OF_INTEREST){
                Spacer(modifier = modifier.width(8.dp))
                ExposedDropdownMenuBox(
                    expanded = isExpandedCategories,
                    onExpandedChange = {
                        isExpandedCategories = !isExpandedCategories
                    },
                ) {
                    TextField(
                        value = appViewModel.searchForm!!.categoryOption.value,
                        onValueChange = {},
                        placeholder = { Text(stringResource(R.string.categories))},
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
                                    appViewModel.searchForm!!.categoryOption.value = item.name
                                    isExpandedCategories = false
                                }
                            )
                    }
                }
            }
        }
        when(appViewModel.searchForm!!.itemType){
            ItemType.LOCATION-> {
                ListItems(
                    locals = appViewModel.getLocations(),
                    appViewModel,
                    navHostController = navHostController,
                    onSelected = {
                        appViewModel.selectedLocationId.value = it
                        navHostController?.navigate(Screens.SEARCH_PLACES_OF_INTEREST.route)
                    }
                )
            }
            ItemType.PLACE_OF_INTEREST ->{
                ListItems(
                    locals = appViewModel.getPlaceOfInterest(),
                    appViewModel,
                    navHostController = navHostController,
                    onSelected = {}
                )
            }
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Button(
            onClick = {
                      when(appViewModel.searchForm!!.itemType){
                          ItemType.LOCATION-> navHostController?.navigate(Screens.ADD_LOCATIONS.route)
                          ItemType.PLACE_OF_INTEREST ->navHostController?.navigate(Screens.ADD_PLACE_OF_INTEREST.route)
                      }
            },
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            modifier = modifier.align(Alignment.BottomEnd).size(50.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add"
            )
        }
    }
}