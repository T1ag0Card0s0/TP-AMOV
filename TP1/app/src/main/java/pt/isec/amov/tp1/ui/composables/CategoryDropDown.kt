import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDown(
    isExpanded: Boolean,
    options: List<String>,
    selectedOption: String,
    icons: List<ImageVector>, // Nova lista de ícones
    placeholder: String,
    label: String,
    onExpandChange: () -> Unit,
    onDismissRequest: () -> Unit,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
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
                ) {
                    Text(placeholder)
                }
            },
            label = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
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
            onDismissRequest = { onDismissRequest() },
        ) {
            for ((index, option) in options.withIndex()) {
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = null,
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = option)
                        }
                    },
                    onClick = {
                        onClick(index)
                    },
                )
            }
        }
    }
}
