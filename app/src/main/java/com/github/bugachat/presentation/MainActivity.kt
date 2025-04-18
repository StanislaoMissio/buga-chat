package com.github.bugachat.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.github.bugachat.commom.GoogleSignInConfiguration

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GoogleSignInButton()
            }
        }
    }

}

@Composable
fun GoogleSignInButton() {
    val context = LocalContext.current
    val onClick: () -> Unit = {
        GoogleSignInConfiguration.configCredentialManager(context)
    }
    Button(onClick = onClick) {
        Text(text = "Sign in with Google")
    }
}