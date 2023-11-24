package pt.isec.a2021138999.tp1.ui.composables

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.a2021138999.tp1.data.Local

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