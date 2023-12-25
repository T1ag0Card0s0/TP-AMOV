package pt.isec.amov.tp1.ui.composables

import android.util.Log
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import pt.isec.amov.tp1.data.Local

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListLocals(
    locals: List<Local>,
    userEmail: String,
    modifier: Modifier = Modifier,
    onSelected: (Local) -> Unit,
    onDetails: (Local) -> Unit,
    onRemove: (Local)->Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items(
            locals,
            key = { it.id }
        ) {
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = modifier
                    .padding(8.dp),
                onClick = { onSelected(it) }
            ) {
                Box(modifier = modifier.fillMaxSize()) {
                    Row ( modifier = modifier.align(Alignment.TopEnd)){
                        if(it.authorEmail == userEmail) {
                            IconButton(
                                onClick = { onRemove(it) },
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Remove,
                                    contentDescription = "Remove"
                                )
                            }
                        }
                        IconButton(
                            onClick = { onDetails(it) },
                        ) {
                            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Details")
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
                        if(it.imageUri!=null){
                            Card (modifier = modifier.fillMaxSize()){
                                AsyncImage(
                                    model =it.imageUri!!,
                                    contentDescription = "Local image",
                                    contentScale = ContentScale.Fit,
                                    modifier = modifier.fillMaxSize()
                                )
                            }

                        }
                    }
                }

            }
        }
    }
}