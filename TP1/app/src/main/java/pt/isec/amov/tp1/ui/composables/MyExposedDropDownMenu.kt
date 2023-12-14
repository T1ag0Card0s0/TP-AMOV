package pt.isec.amov.tp1.ui.composables

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import pt.isec.amov.tp1.R

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
        onExpandedChange = {
                           onExpandChange()
        },
        modifier = modifier
    ) {
        TextField(
            value = selectedOption,
            onValueChange = {},
            placeholder = { Text(placeholder) },
            label = {
                Text(
                    label,
                    textAlign = TextAlign.Center
                )
            },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onDismissRequest()}
        ) {
            for (option in options)
                DropdownMenuItem(
                    text = {
                        Text(text = option)
                    },
                    onClick = {
                        onClick(option)
                       /* optCategory = item.name
                        isExpandedCategories = false
                        appViewModel.addLocalForm!!.category.value=item*/
                    }
                )
        }
    }
}