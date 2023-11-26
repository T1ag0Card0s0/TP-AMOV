package pt.isec.amov.tp1.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.composables.ListItems
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel

enum class BackgroundType{
    LOCATION,
    PLACE_OF_INTEREST,
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchViews(
    appViewModel: AppViewModel,
    type: BackgroundType,
    navHostController: NavHostController?,
    vararg options: String,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var opt by remember {
        mutableStateOf("")
    }
    Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxSize()
    ) {
        if(type==BackgroundType.PLACE_OF_INTEREST)
            Text(
                text = "In ${appViewModel.appData.getSelectedLocalName()}",
                fontSize = 30.sp
            )
        OutlinedTextField(
            value = appViewModel.search.value,
            onValueChange = {
                appViewModel.search.value = it },
            label = {
                Text(text = stringResource(R.string.search))
                    },
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
                }
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
                    modifier = modifier.menuAnchor()
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
        Spacer(modifier = modifier.height(20.dp))
        when(type){
            BackgroundType.LOCATION-> {
                ListItems(locals = appViewModel.appData.getLocations(), onSelected = {
                    appViewModel.appData.selectedLocal.intValue = it
                    navHostController?.navigate(Screens.PLACE_OF_INTEREST_SEARCH.route)
                })
            }
            BackgroundType.PLACE_OF_INTEREST ->{
                ListItems(locals = appViewModel.appData.getPlaceOfInterest(), onSelected = {
                    //TODO:vai para uma pagina de descrição mais promenorizada relacionada com o item selecionado
                })
            }
        }
    }
}