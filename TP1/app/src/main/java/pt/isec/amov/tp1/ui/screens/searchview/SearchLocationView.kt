package pt.isec.amov.tp1.ui.screens.searchview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.data.Location
import pt.isec.amov.tp1.ui.composables.ListLocals
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.location.LocalViewModel

@Composable
fun SearchLocationView(
    viewModel: AppViewModel,
    locationViewModel: LocalViewModel,
    modifier: Modifier = Modifier,
    onSelect: (Location)-> Unit,
    onDetails: (Location)-> Unit,
    onEdit: (Location)->Unit
) {
    val currentCoordinates = locationViewModel.currentLocation.observeAsState()
    val locations = viewModel.locations.observeAsState()

    var alphabeticOrderByAsc by remember { mutableStateOf(true) }
    var distanceOrderByAsc by remember { mutableStateOf(true) }


    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                alphabeticOrderByAsc = !alphabeticOrderByAsc
                viewModel.locationsOrderByAlphabetically(alphabeticOrderByAsc)
            }) {
                Text(if (alphabeticOrderByAsc) "${stringResource(R.string.name)}: ${stringResource(R.string.ascendent)} "
                        else "${stringResource(R.string.name)}: ${stringResource(R.string.descendent)}")
            }

            Button(onClick = {
                distanceOrderByAsc = !distanceOrderByAsc
                viewModel.locationsOrderByDistance(
                    currentCoordinates.value!!.latitude,
                    currentCoordinates.value!!.longitude,
                    distanceOrderByAsc
                )
            }) {
                Text(if (distanceOrderByAsc) "${stringResource(R.string.distance)}: ${stringResource(R.string.ascendent)}"
                else "${stringResource(R.string.distance)}: ${stringResource(R.string.descendent)}")
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
                },
                onEdit = { local->
                    onEdit(local as Location)
                }
            )

    }
}