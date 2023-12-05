package pt.isec.amov.tp1.ui.screens.login_register

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.viewmodels.FireBaseViewModel

@Composable
fun Register(
    fireBaseViewModel: FireBaseViewModel,
    title: String,
    navController: NavHostController?,
    modifier: Modifier = Modifier,
    onSuccess: ()->Unit
) {
    val email = remember{ mutableStateOf("") }
    val password = remember{ mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val error = remember{fireBaseViewModel.error}
    val user by remember{fireBaseViewModel.user}
    val options = listOf(
        Screens.LOGIN.route,
        Screens.CREDITS.route
    )
    LaunchedEffect(key1 = user){
        if(user!=null&&error.value==null) onSuccess()
    }
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
                value =  name.value,
                onValueChange = {
                    name.value=it
                },
                label = { Text(text = stringResource(R.string.name_label))},
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = email.value,
                onValueChange = {
                    email.value=it
                },
                label = { Text(text = stringResource(R.string.email_label))},
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = password.value,
                onValueChange = {
                    password.value=it
                },
                label = { Text(text = stringResource(R.string.password_label))},
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value =confirmPassword.value ,
                onValueChange = {
                    confirmPassword.value=it
                },
                label = { Text(text = stringResource(R.string.confirm_password_label))},
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Button(
                onClick = { fireBaseViewModel.createUserWithEmail(email.value,password.value)  }
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

}