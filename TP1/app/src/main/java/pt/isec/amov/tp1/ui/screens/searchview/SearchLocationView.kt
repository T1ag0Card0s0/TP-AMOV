package pt.isec.amov.tp1.ui.screens.searchview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.ui.composables.ListLocals
import pt.isec.amov.tp1.ui.composables.MyExposedDropDownMenu
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.location.LocalViewModel

@Composable
fun SearchLocationView(
    viewModel: AppViewModel,
    locationViewModel: LocalViewModel,
    modifier: Modifier = Modifier,
    onSelect: (Location)-> Unit,
    onDetails: (Location)-> Unit
) {
    val currentCoordinates = locationViewModel.currentLocation.observeAsState()
    val locations = viewModel.locations.observeAsState()
    var isAlphabetiOrderByExpanded by remember {mutableStateOf(false)}
    var alphabeticOrderBy by remember { mutableStateOf("") }
    var isDistanceOrderByExpanded by remember {mutableStateOf(false)}
    var distanceOrderBy by remember { mutableStateOf("") }
    var orderByOptions = listOf(
        stringResource(R.string.ascendent),
        stringResource(R.string.descendent)
    )
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = modifier.fillMaxWidth()
        ) {
            MyExposedDropDownMenu(
                isExpanded = isAlphabetiOrderByExpanded,
                options =orderByOptions,
                selectedOption = alphabeticOrderBy,
                placeholder = stringResource(R.string.orderBy),
                label = stringResource(R.string.alphabetic),
                onExpandChange = { isAlphabetiOrderByExpanded= !isAlphabetiOrderByExpanded },
                onDismissRequest = { isAlphabetiOrderByExpanded = false },
                onClick = {
                    isAlphabetiOrderByExpanded = false
                    alphabeticOrderBy=orderByOptions[it]
                    viewModel.locationsOrderByAlphabetically(it==0)
                },
                modifier = modifier
                    .weight(1f)
                    .padding(start = 3.dp, end = 3.dp)
            )
            MyExposedDropDownMenu(
                isExpanded = isDistanceOrderByExpanded,
                options = orderByOptions,
                selectedOption = distanceOrderBy,
                placeholder = stringResource(R.string.orderBy),
                label = stringResource(R.string.distance),
                onExpandChange = { isDistanceOrderByExpanded=!isDistanceOrderByExpanded },
                onDismissRequest = { isDistanceOrderByExpanded=false },
                onClick = {
                    isDistanceOrderByExpanded = false
                    distanceOrderBy= orderByOptions[it]
                    viewModel.locationsOrderByDistance(
                        currentCoordinates.value!!.latitude,
                        currentCoordinates.value!!.longitude,
                        it==0
                    )
                },
                modifier = modifier
                    .weight(1f)
                    .padding(start = 3.dp, end = 3.dp)
            )
        }
        if(locations.value!=null)
            ListLocals(
                locals =
                if(viewModel.user.value!=null&&viewModel.isMyContributions.value)
                    locations.value!!.filter { it.authorEmail == viewModel.user.value!!.email }
                else
                    locations.value!!,
                userEmail = viewModel.user.value?.email,
                onSelected = {
                    onSelect(it as Location)
                },
                onDetails = {
                    onDetails(it as Location)
                },
                onRemove={
                    viewModel.removeLocation(it as Location)
                }
            )

    }
}