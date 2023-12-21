package pt.isec.amov.tp1.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.OnDeviceTraining
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.amov.tp1.data.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListCategories(
    categories: List<Category>,
    modifier: Modifier = Modifier,
    onSelected: (Category) -> Unit,
    onDetails: (Category) -> Unit,
    onRemove: (Category) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items(
            categories,
            key = { it.id }
        ) {
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = modifier
                    .padding(8.dp),
                onClick = { onSelected(it) }
            ) {
                Box(modifier = modifier.fillMaxSize()) {
                    Row(modifier = modifier.align(Alignment.CenterEnd)){
                        IconButton(
                            onClick = { onRemove(it) },
                        ) {
                            Icon(imageVector = Icons.Rounded.Remove, contentDescription ="Remove")
                        }
                        IconButton(
                            onClick = { onDetails(it) },
                        ) {
                            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Details")
                        }

                    }

                    Text(
                        text = it.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = modifier
                            .align(Alignment.CenterStart)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}