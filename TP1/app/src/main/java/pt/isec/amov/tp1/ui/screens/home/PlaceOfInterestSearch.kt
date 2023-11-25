package pt.isec.amov.tp1.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.data.AppData
import pt.isec.amov.tp1.data.Local
import pt.isec.amov.tp1.ui.composables.DropDownMenu
import pt.isec.amov.tp1.ui.composables.ListItems
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel


@Composable
fun PlaceOfInterestSearch(
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.place_of_interest_title),
            fontSize = 40.sp
        )
        Text(
            text = "In ${appViewModel.appData.getSelectedLocalName()}",
            fontSize = 30.sp
        )
        OutlinedTextField(
            value = appViewModel.search.value,
            onValueChange = {
                appViewModel.search.value=it
            },
            label = {
                Text(
                    text = stringResource(R.string.search)
                ) },
            modifier = modifier
                .fillMaxWidth()
        )
        Spacer(
            modifier.height(16.dp)
        )
        Row {
            DropDownMenu(
                stringResource(R.string.orderBy),
                appViewModel.orderByOpt,
                stringResource(R.string.categories),
                stringResource(R.string.alphabetic),
                stringResource(R.string.distance)
            )
        }
        Spacer(
            modifier.height(16.dp)
        )
        ListItems(locals = appViewModel.appData.getPlaceOfInterest(), onSelected = {})
    }
}