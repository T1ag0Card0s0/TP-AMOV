package pt.isec.a2021138999.tp1.ui.screens.login_register

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.a2021138999.tp1.R
import pt.isec.a2021138999.tp1.ui.screens.Screens

@Composable
fun Register(
    title: String,
    navController: NavHostController?,
    vararg options: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 48.sp,
            modifier = modifier
                .align(Alignment.TopCenter)
                .padding(24.dp)
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .align(Alignment.Center)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            Text(
                text = stringResource(R.string.register_form_title),
                fontSize = 24.sp,
                modifier = modifier
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = " ",
                onValueChange = {
                },
                label = { Text(text = stringResource(R.string.name_label))},
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = " ",
                onValueChange = {
                },
                label = { Text(text = stringResource(R.string.email_label))},
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = " ",
                onValueChange = {},
                label = { Text(text = stringResource(R.string.password_label))},
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = " ",
                onValueChange = {},
                label = { Text(text = stringResource(R.string.confirm_password_label))},
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Button(
                onClick = { navController?.navigate(Screens.LOCATION_SEARCH.route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(160,160,160)
                )
            ) {
                Text(text = stringResource(R.string.submit))
            }
        }
        Column (
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth(0.3f)
                .fillMaxHeight(0.75f)
        ) {
            for(btnName in options)
                Button(
                    onClick = { navController?.navigate(btnName)},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(160,160,160)
                    ),
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    Text(text = btnName)
                }
        }
    }
}
@Preview
@Composable
fun RegisterPreview(){
    Register(
        stringResource(id = R.string.app_name),
        null,
        stringResource(id = R.string.login_form_title),
        stringResource(id = R.string.credits_title))
}