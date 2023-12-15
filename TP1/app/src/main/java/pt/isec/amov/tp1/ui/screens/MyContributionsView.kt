package pt.isec.amov.tp1.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.composables.MyExposedDropDownMenu

@Composable
fun MyContributionsView(
    modifier: Modifier = Modifier
){
    var isExpanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf( "") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        MyExposedDropDownMenu(
            isExpanded = isExpanded,
            options = listOf(
                stringResource(R.string.locations),
                stringResource(R.string.places_of_interest),
                stringResource(R.string.categories),
            ) ,
            selectedOption = selectedOption,
            placeholder = stringResource(R.string.select_one_option) ,
            label = stringResource(R.string.my_contributions),
            onExpandChange = { isExpanded= !isExpanded },
            onDismissRequest = { isExpanded = false },
            onClick = {
                isExpanded = false
                selectedOption = it
            },
            modifier = modifier.fillMaxWidth()
        )

    }
}