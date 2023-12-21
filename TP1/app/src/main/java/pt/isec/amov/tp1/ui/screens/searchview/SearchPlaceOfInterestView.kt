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

@Composable
fun SearchPlaceOfInterestView(
    placesOfInterest: List<PlaceOfInterest>,
    categories: List<Category>,
    location: Location,
    modifier: Modifier = Modifier,
    onDetails: (PlaceOfInterest) -> Unit
) {

    var isExpandedCategories by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var isAlphabetiOrderByExpanded by remember { mutableStateOf(false) }
    var alphabeticOrderBy by remember { mutableStateOf("") }
    var isDistanceOrderByExpanded by remember { mutableStateOf(false) }
    var distanceOrderBy by remember { mutableStateOf("") }
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
            options = categories.map { it.name },
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

        Text(
            text = "In ${location.name}",
            fontSize = 30.sp
        )
        ListLocals(
            locals = placesOfInterest,
            onSelected = {},
            onDetails = {
                onDetails(it as PlaceOfInterest)
            }
        )
    }
}