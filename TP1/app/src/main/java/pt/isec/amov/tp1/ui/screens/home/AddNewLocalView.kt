package pt.isec.amov.tp1.ui.screens.home

import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.composables.MyExposedDropDownMenu
import pt.isec.amov.tp1.ui.composables.MyTextField
import pt.isec.amov.tp1.ui.composables.TakePhoto
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.ItemType

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
            MyTextField(
                value = appViewModel.addLocalForm!!.name.value,
                onChange =  { appViewModel.addLocalForm!!.name.value = it },
                placeholder = stringResource(R.string.enter_name),
                label = stringResource(R.string.name),
                icon = Icons.Default.Abc
            )

            Spacer(modifier = modifier.height(24.dp))
            MyTextField(
                value = appViewModel.addLocalForm!!.descrition.value,
                onChange =  { appViewModel.addLocalForm!!.descrition.value = it },
                placeholder = stringResource(R.string.enter_description),
                label = stringResource(R.string.description),
                icon = Icons.Default.Abc
            )
            if(appViewModel.searchForm!!.itemType==ItemType.PLACE_OF_INTEREST) {
                Spacer(modifier = modifier.height(24.dp))
                MyExposedDropDownMenu(
                    isExpanded = isExpandedCategories,
                    options = appViewModel.getCategories().map { it.name },
                    selectedOption = optCategory,
                    placeholder = stringResource(R.string.select_categories) ,
                    label = stringResource(R.string.categories) ,
                    onExpandChange = { /*TODO*/isExpandedCategories = !isExpandedCategories },
                    onDismissRequest = { /*TODO*/
                        isExpandedCategories = false },
                    onClick = {
                        optCategory = it
                        isExpandedCategories = false
                        appViewModel.addLocalForm!!.category.value= appViewModel.getCategories().find { c-> c.name == optCategory }
                    }
                )
            }
            Spacer(modifier = modifier.height(24.dp))
            Row{
                Button(onClick = { /*TODO*/ }) {
                    Text(text = stringResource(R.string.current_location))
                }
                Spacer(modifier = modifier.width(8.dp))
                Button(
                    onClick = {
                        navHostController.navigate(Screens.CHOOSE_COORDINATES.route)
                    }
                ) {
                    Text(stringResource(R.string.choose_location))
                }
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