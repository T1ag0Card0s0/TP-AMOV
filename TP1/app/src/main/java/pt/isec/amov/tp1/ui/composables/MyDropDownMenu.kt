package pt.isec.amov.tp1.ui.composables

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MyDropDownMenu(
    isExpanded: Boolean,
    options: List<String>,
    onClick: (String)->Unit,
    modifier: Modifier = Modifier
){
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { onClick("")}) {
        for(option in options)
            DropdownMenuItem(
                text = {
                    Text(option)
                },
                onClick = {
                   onClick(option)
                }
            )
    }
}