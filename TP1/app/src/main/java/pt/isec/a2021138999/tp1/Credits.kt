package pt.isec.a2021138999.tp1

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun Credits(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.credits_title),
            fontSize = 48.sp,
            )
        Text(text = "Trabalho realizado no âmbito da unidade curricular Arquiteturas Móveis\n"+
                "Tiago Rafael Santos Cardoso\n"+
                "\t\tNumero: 2021138999\n"+
                "\t\tE-Mail: a2021138999@isec.pt\n"+
                "Adicionar o resto do grupo")
    }
}
@Preview
@Composable
fun CreditsPreview(){
    Credits()
}