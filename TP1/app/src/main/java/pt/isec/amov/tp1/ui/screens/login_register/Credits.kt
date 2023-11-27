package pt.isec.amov.tp1.ui.screens.login_register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.isec.amov.tp1.R

@Composable
fun Credits(
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(text = stringResource(R.string.credits_description)+
                    "\n"+
                    "Tiago Rafael Santos Cardoso, 2021138999, a2021138999@isec.pt")
    }
}
@Preview
@Composable
fun CreditsPreview(){
    Credits()
}