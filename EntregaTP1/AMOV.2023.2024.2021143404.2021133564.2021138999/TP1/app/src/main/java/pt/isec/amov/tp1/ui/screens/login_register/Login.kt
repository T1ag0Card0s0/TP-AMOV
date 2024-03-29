package pt.isec.amov.tp1.ui.screens.login_register

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pt.isec.amov.tp1.R
import pt.isec.amov.tp1.ui.composables.MyTextField
import pt.isec.amov.tp1.ui.composables.PasswordField
import pt.isec.amov.tp1.ui.screens.Screens
import pt.isec.amov.tp1.ui.viewmodels.AppViewModel

@Composable
fun LoginForm(
    viewModel: AppViewModel,
    navController: NavHostController?,
    modifier: Modifier = Modifier,
) {
    val email = remember{ mutableStateOf("") }
    val password = remember{ mutableStateOf("") }
    val error = remember{viewModel.error}
    val user by remember{viewModel.user}
    val options = listOf(
        Screens.REGISTER.route,
        Screens.CREDITS.route
    )

    LaunchedEffect(key1 = user) {
        if (user != null && error.value == null) navController?.navigate(Screens.SEARCH_LOCATIONS.route)

    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.amov),
            contentDescription = null,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            if(error.value!=null){
                Text(error.value!!, textAlign = TextAlign.Center)
            }
            MyTextField(
                value = email.value,
                onChange = { email.value = it },
                stringResource(R.string.enter_your_email),
                label = stringResource(R.string.email),
                icon = Icons.Default.Person,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            PasswordField(
                value = password.value,
                onChange = { password.value = it },
                submit = { viewModel.signInWithEmail(email.value, password.value) },
                label = stringResource(R.string.password),
                stringResource(R.string.enter_your_password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.signInWithEmail(email.value,password.value) },
                enabled = true,
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.submit))
            }
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth(0.4f)
                .fillMaxHeight(0.75f)
        ) {
            options.forEach { btnName ->
                Button(
                    onClick = { navController?.navigate(btnName) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = btnName)
                }
            }
        }
    }
}


