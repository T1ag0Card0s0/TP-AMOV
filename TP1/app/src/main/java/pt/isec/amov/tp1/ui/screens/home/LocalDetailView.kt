package pt.isec.amov.tp1.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel
import pt.isec.amov.tp1.ui.viewmodels.ItemType

@Composable
fun LocalDetailView(
    viewModel: AppViewModel,
    itemType: ItemType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        
        when(itemType){
            ItemType.LOCATION->{

            }
            ItemType.PLACE_OF_INTEREST->{

            }
        }
    }
    //TODO: Detalhes de um local
}