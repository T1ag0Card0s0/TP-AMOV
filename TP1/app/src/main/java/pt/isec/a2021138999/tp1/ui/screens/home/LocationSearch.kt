package pt.isec.a2021138999.tp1.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.a2021138999.tp1.R
import pt.isec.a2021138999.tp1.data.AppData
import pt.isec.a2021138999.tp1.ui.composables.DropDownMenu
import pt.isec.a2021138999.tp1.ui.composables.ListItems
import pt.isec.a2021138999.tp1.ui.screens.Screens
import pt.isec.a2021138999.tp1.ui.viewmodels.AppViewModel


@Composable
fun LocationSearch(
    appData: AppData,
    navHostController: NavHostController?,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.locations_title),
                fontSize = 40.sp
            )
            OutlinedTextField(
                value = " ",
                onValueChange = {

                },
                label = {
                    Text(
                        text = stringResource(R.string.search)
                    )
                },
                modifier = modifier
                    .fillMaxWidth()
            )
            Spacer(
                modifier.height(16.dp)
            )
            Row {
                DropDownMenu(
                    stringResource(R.string.orderBy),
                    stringResource(R.string.alphabetic),
                    stringResource(R.string.distance)
                )
            }
            Spacer(modifier = modifier.height(20.dp))
            ListItems(locals = appData.getLocations(), onSelected = {
                appData.selectedLocal.intValue = it
                navHostController?.navigate(Screens.PLACE_OF_INTEREST_SEARCH.route)
            })
        }
        Column (
            verticalArrangement = Arrangement.Bottom,
            modifier = modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth(0.3f)
                .fillMaxHeight(0.75f)
        ) {
            Button(
                onClick = { navHostController?.navigate(Screens.LOGIN.route)},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(160,160,160)
                ),
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.logout))
            }
        }
    }

}