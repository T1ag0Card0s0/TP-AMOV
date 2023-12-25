package pt.isec.amov.tp1.ui.screens.searchview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.data.PlaceOfInterest
import pt.isec.amov.tp1.ui.composables.ListLocals
import pt.isec.amov.tp1.ui.composables.MyExposedDropDownMenu
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel

@Composable
fun SearchPlaceOfInterestView(
    viewModel: AppViewModel,
    location: Location,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onDetails: (PlaceOfInterest) -> Unit
) {
    val placesOfInterest = viewModel.placesOfInterest.observeAsState()
    val categories = viewModel.categories.observeAsState()
    var isExpandedCategories by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var isAlphabetiOrderByExpanded by remember { mutableStateOf(false) }
    var alphabeticOrderBy by remember { mutableStateOf("") }
    var isDistanceOrderByExpanded by remember { mutableStateOf(false) }
    var distanceOrderBy by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxSize()
    ) {
        Box(
            modifier = modifier.fillMaxWidth()
        ) {

            Text(
                text = "In ${location.name}",
                fontSize = 30.sp,
                modifier = modifier.align(Alignment.Center)
            )
            IconButton(
                onClick = {
                    navController.navigate(Screens.PLACES_OF_INTEREST_MAP.route)
                },
                modifier = modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = "Map of placesOfInterest"
                )
            }


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

        if (placesOfInterest.value != null)
            ListLocals(
                locals =
                if (!viewModel.isMyContributions.value)
                    placesOfInterest.value!!.filter { it.locationId == viewModel.selectedLocation.value!!.id }
                else
                    placesOfInterest.value!!.filter {
                        it.locationId == viewModel.selectedLocation.value!!.id &&
                                it.authorEmail == viewModel.user.value!!.email
                    },
                userEmail = viewModel.user.value!!.email,
                onSelected = {},
                onDetails = {
                    onDetails(it as PlaceOfInterest)
                },
                onRemove = {
                    viewModel.removePlaceOfInterest(it as PlaceOfInterest)
                }
            )
    }
}