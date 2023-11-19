package pt.isec.a2021138999.tp1

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

@Composable
fun Register(
    title: String,
    navController: NavHostController?,
    vararg options: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 48.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(24.dp)
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            Text(
                text = stringResource(R.string.register_form_title),
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = " ",
                onValueChange = {
                },
                label = { Text(text = stringResource(R.string.name_label))},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = " ",
                onValueChange = {
                },
                label = { Text(text = stringResource(R.string.email_label))},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = " ",
                onValueChange = {},
                label = { Text(text = stringResource(R.string.password_label))},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = " ",
                onValueChange = {},
                label = { Text(text = stringResource(R.string.confirm_password_label))},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Button(
                onClick = { /*TODO*/ },
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
            modifier = Modifier
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
                    modifier = Modifier
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