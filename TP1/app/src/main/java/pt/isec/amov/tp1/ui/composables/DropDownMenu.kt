package pt.isec.amov.tp1.ui.composables

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(
    title: String,
    vararg options: String,
    modifier: Modifier = Modifier
){
    //TODO:ESTAS VARIAVEIS DEVEM SER RETIRADAS E COLOCADAS POR EXEMPLO NUMA VIEW MODEL
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var opt by remember {
        mutableStateOf("")
    }
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { newValue ->
            isExpanded = newValue
        }
    ) {
        OutlinedTextField(
            value = opt,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            placeholder = {
                Text(text = title)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            for (itemName in options)
                DropdownMenuItem(
                    text = {
                        Text(text = itemName)
                    },
                    onClick = {
                        opt = itemName
                        isExpanded = false
                    }
                )
        }
    }

}