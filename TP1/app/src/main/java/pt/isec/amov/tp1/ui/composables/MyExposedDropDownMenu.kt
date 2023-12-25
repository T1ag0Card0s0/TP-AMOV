package pt.isec.amov.tp1.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyExposedDropDownMenu(
    isExpanded: Boolean,
    options: List<String>,
    selectedOption: String,
    placeholder: String,
    label: String,
    onExpandChange: ()->Unit,
    onDismissRequest: ()->Unit,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
){
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { onExpandChange() },
        modifier = modifier
    ) {
        TextField(
            value = selectedOption,
            onValueChange = {},
            placeholder = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(placeholder)
                } },
            label = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        label,
                        textAlign = TextAlign.Center
                    )
                }

            },
            textStyle = TextStyle(textAlign = TextAlign.Center),
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            modifier = modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onDismissRequest()},
        ) {
            for (option in options)
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement =Arrangement.Center ,
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Text(text = option)
                        }

                    },
                    onClick = {
                        onClick(option)
                    },
                )
        }
    }
}