package pt.isec.amov.tp1.ui.screens.searchview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
            Button(onClick = { viewModel.locationsOrderByAlphabetically(true) }) {
                Text("A")
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                Text("Z")
            }
            Button(onClick = { viewModel.locationsOrderByAlphabetically(false) }) {
                Text("Z")
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                Text("A")
            }
            Button(onClick = { viewModel.locationsOrderByDistance(
                currentCoordinates.value!!.latitude,
                currentCoordinates.value!!.longitude,
                true
            ) }) {
                Text("KM")
                Icon(imageVector = Icons.Default.Remove, contentDescription = null)
            }
            Button(onClick = { viewModel.locationsOrderByDistance(
                currentCoordinates.value!!.latitude,
                currentCoordinates.value!!.longitude,
                false
            ) }) {
                Text("KM")
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
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