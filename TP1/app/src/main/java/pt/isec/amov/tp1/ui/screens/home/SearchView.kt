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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.composables.ListItems
import pt.isec.amov.tp1.ui.composables.MyExposedDropDownMenu
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.ItemType

@Composable
fun SearchView(
    appViewModel: AppViewModel,
    navHostController: NavHostController?,
    modifier: Modifier = Modifier
) {

    var isExpandedCategories by remember {
        mutableStateOf(false)
    }
    var selectedCategory by remember {
        mutableStateOf("")
    }
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
        if(appViewModel.searchForm!!.itemType==ItemType.PLACE_OF_INTEREST) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxWidth()
            ) {
                MyExposedDropDownMenu(
                    isExpanded = isExpandedCategories,
                    options = appViewModel.getCategories().map { it.name },
                    selectedOption = selectedCategory,
                    placeholder = stringResource(R.string.select_categories),
                    label = stringResource(R.string.categories),
                    onExpandChange = { /*TODO*/isExpandedCategories = !isExpandedCategories },
                    onDismissRequest = { /*TODO*/isExpandedCategories = false },
                    onClick = {
                        isExpandedCategories = false
                        selectedCategory = it
                    }
                )
            }
            Text(
                text = "In ${appViewModel.getSelectedLocalName()}",
                fontSize = 30.sp
            )
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
            modifier = modifier
                .align(Alignment.BottomEnd)
                .size(50.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add"
            )
        }
    }
}