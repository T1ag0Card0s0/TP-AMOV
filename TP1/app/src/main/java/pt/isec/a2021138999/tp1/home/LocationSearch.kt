package pt.isec.a2021138999.tp1.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.a2021138999.tp1.R
import pt.isec.a2021138999.tp1.utils.DropDownMenu

@Composable
fun LocationSearch(){
    Column {
        Text(
            text = stringResource(id = R.string.locations_title),
            fontSize = 48.sp
        )
        OutlinedTextField(
            value = " ",
            onValueChange = {

            },
            label = { Text(text = "Search") },
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Row {
            DropDownMenu("Order by", "Alphabetic", "Distance")
        }
    }
}