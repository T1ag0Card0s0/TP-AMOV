package pt.isec.amov.tp1.ui.composables

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import pt.isec.amov.tp1.data.Local

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListLocals(
    locals: List<Local>,
    modifier: Modifier = Modifier,
    onSelected: (Local) -> Unit,
    onDetails:(Local)-> Unit
){

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ){
        items(
            locals,
            key = { it.id }
        ){
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = modifier
                    .padding(8.dp),
                onClick = {onSelected(it)}
            ) {
                Box(modifier = modifier.fillMaxSize()) {
                    IconButton(onClick = { onDetails(it) },
                        modifier=modifier.align(Alignment.TopEnd)) {
                        Icon(imageVector = Icons.Filled.MoreVert, contentDescription ="Details" )
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
                        if(it.image!=null)
                            Image(bitmap = it.image!!.asImageBitmap(), contentDescription = " " )

                        else
                            AsyncImage(
                                model = it.imagePath,
                                contentDescription = "Image",
                                contentScale = ContentScale.Fit,
                            )
                    }
                }

            }
        }
    }
}