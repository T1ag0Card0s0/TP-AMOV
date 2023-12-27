package pt.isec.amov.tp1.ui.composables

import android.widget.TextView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.data.Local

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListLocals(
    locals: List<Local>,
    userEmail: String?,
    modifier: Modifier = Modifier,
    ableToEvaluate: Boolean,
    onSelected: (Local) -> Unit,
    onDetails: (Local) -> Unit,
    onRemove: (Local) -> Unit,
    onEvaluate: (Local) -> Unit
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items(
            locals,
            key = { it.id }
        ) {
            var isExpanded by remember {
                mutableStateOf(false)
            }
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = modifier
                    .padding(8.dp),
                onClick = { onSelected(it) }
            ) {
                if (!it.approved) {
                    Row(modifier = modifier.fillMaxSize()) {
                        Icon(imageVector = Icons.Filled.WarningAmber, contentDescription = "Danger")
                        Text(text = "Item not yet approved, information may be incorrect")
                    }
                }
                Box(modifier = modifier.fillMaxSize()) {
                    Column(modifier = modifier.align(Alignment.TopEnd)) {
                        IconButton(
                            onClick = { isExpanded = !isExpanded },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "More Options"
                            )
                        }
                        DropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false }) {
                            if (userEmail != null) {
                                if (it.authorEmail == userEmail) {
                                    DropdownMenuItem(
                                        text = { Text(text = stringResource(R.string.remove)) },
                                        onClick = {
                                            isExpanded = false
                                            onRemove(it)
                                        }
                                    )
                                }
                            }
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.details)) }, onClick = {
                                    isExpanded = false
                                    onDetails(it)
                                }
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = it.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                            if (it.imageUri != null) {
                                AsyncImage(
                                    model = it.imageUri!!,
                                    contentDescription = "Local image",
                                    contentScale = ContentScale.FillWidth,
                                    modifier = modifier.fillMaxSize()
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = it.description,
                                modifier = Modifier.weight(1f)
                            )
                            if (ableToEvaluate) {
                                IconButton(onClick = { onEvaluate(it) }) {
                                    Icon(
                                        imageVector = Icons.Default.Comment,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        item{
            Spacer(modifier = modifier.padding(50.dp))
        }
    }
}