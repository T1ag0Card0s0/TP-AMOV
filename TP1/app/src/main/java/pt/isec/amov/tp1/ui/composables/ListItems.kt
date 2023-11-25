package pt.isec.amov.tp1.ui.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.amov.tp1.data.Local

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItems(
    locals: List<Local>,
    modifier: Modifier = Modifier,
    onSelected: (Int) -> Unit
){

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ){
        item(
            key = null
        ) {

            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(255, 255, 255),
                    contentColor = Color(0, 0, 0)
                ),
                onClick = { onSelected(-1) }
            ) {
                Box(
                    modifier = modifier.fillMaxSize()
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = " ",
                        modifier = modifier
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
        items(
            locals,
            key = { it.id }
        ){
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(255,255,255),
                    contentColor = Color(0,0,0)
                ),
                onClick = {onSelected(it.id)}
            ) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Title: ${it.name}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Description:\n ${it.description}", fontSize = 12.sp)
                }
            }
        }
    }
}