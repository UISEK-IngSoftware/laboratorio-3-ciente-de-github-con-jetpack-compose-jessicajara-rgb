package ec.edu.uisek.githubclient.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import ec.edu.uisek.githubclient.services.AuthService
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme

@Composable
fun LoginForm(
    onLoginSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    val authService = remember { AuthService(context) }

    var username by remember { mutableStateOf(value = "") }
    var token by remember { mutableStateOf(value = "") }
    var passwordVisible by remember { mutableStateOf(value = false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all=16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ingreso a cliente de Github",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it},
            label = { Text("Nombre de usuario")},
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = token,
            onValueChange = { token = it},
            label = { Text("Contraseña")},
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if(passwordVisible) Icons.Filled.Lock
                    else Icons.Filled.AccountBox

                IconButton(onClick = {passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null )
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                authService.saveAuth(username, token)
                onLoginSuccess()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = username.isNotBlank() && token.isNotBlank()
        ) {
            Icon (
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                modifier = Modifier.padding(end=8.dp)
            )
            Text(text = "Ingresar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginFormPreview() {
    GithubClientTheme {
        LoginForm()
    }
}