package pt.isec.amov.tp1.ui.screens.login_register

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
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
import pt.isec.amov.tp1.ui.composables.LoginField
import pt.isec.amov.tp1.ui.composables.PasswordField
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.viewmodels.FireBaseViewModel

@Composable
fun RegisterForm(
    fireBaseViewModel: FireBaseViewModel,
    navController: NavHostController?,
    modifier: Modifier = Modifier,
    onSuccess: ()->Unit
){
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
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            LoginField(
                value = name.value,
                onChange = { name.value=it},
                stringResource(R.string.name_label),
                stringResource(R.string.name),
                Icons.Default.Abc,
                modifier = Modifier.fillMaxWidth()
            )
            LoginField(
                value = email.value,
                onChange = { email.value=it},
                stringResource(R.string.enter_your_email),
                stringResource(R.string.email),
                Icons.Default.Person,
                modifier = Modifier.fillMaxWidth()
            )
            PasswordField(
                value = password.value,
                onChange = { password.value = it },
                submit = { },
                stringResource(R.string.password),
                stringResource(R.string.enter_your_password),
                modifier = Modifier.fillMaxWidth()
            )
            PasswordField(
                value = confirmPassword.value,
                onChange = { confirmPassword.value = it },
                submit = { },
                stringResource(R.string.confirm_password),
                stringResource(R.string.confirm_your_password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { fireBaseViewModel.signInWithEmail(email.value,password.value) },
                enabled = true,
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.submit))
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