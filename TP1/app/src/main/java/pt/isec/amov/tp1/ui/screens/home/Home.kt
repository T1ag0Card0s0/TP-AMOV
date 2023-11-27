package pt.isec.amov.tp1.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
fun SearchViews(
    appViewModel: AppViewModel,
    navHostController: NavHostController?,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var opt by remember {
        mutableStateOf("")
    }
    val options =(
            when(appViewModel.searchForm!!.itemType){
                ItemType.LOCATION-> listOf(
                    stringResource(R.string.alphabetic),
                    stringResource(R.string.distance)
                )
                ItemType.PLACE_OF_INTEREST -> listOf(
                    stringResource(R.string.alphabetic),
                    stringResource(R.string.distance),
                    stringResource(R.string.categories)
                )
            }
            )
    Column(
            modifier = modifier
                .padding(8.dp)
                .fillMaxSize()
    ) {
        if(appViewModel.searchForm!!.itemType==ItemType.PLACE_OF_INTEREST)
            Text(
                text = "In ${appViewModel.appData.getSelectedLocalName()}",
                fontSize = 30.sp
            )
        OutlinedTextField(
            value = appViewModel.searchForm!!.name.value,
            onValueChange = {
                appViewModel.searchForm!!.name.value = it },
            label = {
                Text(text = stringResource(R.string.search))
                    },
            shape = RoundedCornerShape(20.dp),
            modifier = modifier
                .fillMaxWidth()
        )
        Spacer(
            modifier.height(8.dp)
        )
        Row {
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { newValue ->
                    isExpanded = newValue
                },
            ) {
                OutlinedTextField(
                    value = opt,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.orderBy))
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = modifier.menuAnchor(),
                    shape = RoundedCornerShape(20.dp)
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = {
                        isExpanded = false
                    }
                ) {
                    for (itemName in options)
                        DropdownMenuItem(
                            text = {
                                Text(text = itemName)
                            },
                            onClick = {
                                opt = itemName
                                isExpanded = false
                            }
                        )
                }
            }
        }
        Spacer(modifier = modifier.height(8.dp))
        when(appViewModel.searchForm!!.itemType){
            ItemType.LOCATION-> {
                ListItems(locals = appViewModel.appData.getLocations(), onSelected = {
                    appViewModel.appData.selectedLocal.intValue = it
                    navHostController?.navigate(Screens.SEARCH_PLACES_OF_INTEREST.route)
                })
            }
            ItemType.PLACE_OF_INTEREST ->{
                ListItems(locals = appViewModel.appData.getPlaceOfInterest(), onSelected = {
                    //TODO:vai para uma pagina de descrição mais promenorizada relacionada com o item selecionado
                })
            }
        }
    }
}