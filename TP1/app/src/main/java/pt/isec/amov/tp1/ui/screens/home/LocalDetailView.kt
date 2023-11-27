package pt.isec.amov.tp1.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pt.isec.amov.tp1.data.Local
import pt.isec.amov.tp1.data.PlaceOfInterest
@Composable
fun LocalDetailView(
    modifier: Modifier = Modifier
){
    Box(modifier = modifier.fillMaxSize()){
        Text(text = "Detail View", modifier.align(Alignment.Center))
    }
    //TODO: Detalhes de um local
}