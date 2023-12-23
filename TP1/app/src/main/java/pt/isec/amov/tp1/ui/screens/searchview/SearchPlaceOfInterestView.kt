package pt.isec.amov.tp1.ui.screens.searchview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.data.Category
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.data.PlaceOfInterest
import pt.isec.amov.tp1.ui.composables.ListLocals
import pt.isec.amov.tp1.ui.composables.MyExposedDropDownMenu
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel

@Composable
fun SearchPlaceOfInterestView(
    viewModel: AppViewModel,
    location: Location,
    modifier: Modifier = Modifier,
    onDetails: (PlaceOfInterest) -> Unit
) {
    val placesOfInterest = viewModel.placesOfInterest.observeAsState()
    val categories= viewModel.categories.observeAsState()
    var isExpandedCategories by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var isAlphabetiOrderByExpanded by remember { mutableStateOf(false) }
    var alphabeticOrderBy by remember { mutableStateOf("") }
    var isDistanceOrderByExpanded by remember { mutableStateOf(false) }
    var distanceOrderBy by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .padding(start=8.dp, end=8.dp)
            .fillMaxSize()
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxWidth()
        ){
            Text(
                text = "In ${location.name}",
                fontSize = 30.sp
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = modifier.fillMaxWidth()
        ) {
            MyExposedDropDownMenu(
                isExpanded = isAlphabetiOrderByExpanded,
                options = listOf(
                    stringResource(R.string.ascendent),
                    stringResource(R.string.descendent)
                ),
                selectedOption = alphabeticOrderBy,
                placeholder = stringResource(R.string.orderBy),
                label = stringResource(R.string.alphabetic),
                onExpandChange = { isAlphabetiOrderByExpanded = !isAlphabetiOrderByExpanded },
                onDismissRequest = { isAlphabetiOrderByExpanded = false },
                onClick = {
                    isAlphabetiOrderByExpanded = false
                    alphabeticOrderBy = it
                },
                modifier = modifier
                    .weight(1f)
                    .padding(start = 3.dp, end = 3.dp)
            )
            MyExposedDropDownMenu(
                isExpanded = isDistanceOrderByExpanded,
                options = listOf(
                    stringResource(R.string.ascendent),
                    stringResource(R.string.descendent)
                ),
                selectedOption = distanceOrderBy,
                placeholder = stringResource(R.string.orderBy),
                label = stringResource(R.string.distance),
                onExpandChange = { isDistanceOrderByExpanded = !isDistanceOrderByExpanded },
                onDismissRequest = { isDistanceOrderByExpanded = false },
                onClick = {
                    isDistanceOrderByExpanded = false
                    distanceOrderBy = it
                },
                modifier = modifier
                    .weight(1f)
                    .padding(start = 3.dp, end = 3.dp)
            )
        }
        MyExposedDropDownMenu(
            isExpanded = isExpandedCategories,
            options = categories.value!!.map { it.name },
            selectedOption = selectedCategory,
            placeholder = stringResource(R.string.select_categories),
            label = stringResource(R.string.categories),
            onExpandChange = { isExpandedCategories = !isExpandedCategories },
            onDismissRequest = { isExpandedCategories = false },
            onClick = {
                isExpandedCategories = false
                selectedCategory = it
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 6.dp, start = 3.dp, end = 3.dp)
        )

        if(placesOfInterest.value!=null)
            ListLocals(
                locals =
                if(!viewModel.isMyContributions.value)
                    placesOfInterest.value!!.filter { it.locationId == viewModel.selectedLocation.value!!.id }
                else
                    placesOfInterest.value!!.filter {
                        it.locationId == viewModel.selectedLocation.value!!.id &&
                        it.authorEmail == viewModel.user.value!!.email
                                                    } ,
                onSelected = {},
                onDetails = {
                    onDetails(it as PlaceOfInterest)
                }
            )
    }
}