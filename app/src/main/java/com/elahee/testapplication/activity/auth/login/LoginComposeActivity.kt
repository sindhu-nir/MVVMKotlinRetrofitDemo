package com.btracsolutions.yesparking.activity.auth.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.btracsolutions.yesparking.R
import com.btracsolutions.yesparking.ui.theme.SplashActivityTheme

class LoginComposeActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SplashActivityTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(title = {
                            Text(text = "Tweetsy")
                        }, Modifier.background(color = Color.Black))
                    }
                ) {
                    Box(modifier = Modifier.padding(it)){
                        App()
                    }
                }

            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun App() {

    }

    @Composable
    fun LoginField(
        value: String,
        onChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        label: String = "Login",
        placeholder: String = "Enter your Login"
    ) {

        val focusManager = LocalFocusManager.current
        val leadingIcon = @Composable {
            Icon(
                Icons.Default.Person,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        TextField(
            value = value,
            onValueChange = onChange,
            modifier = modifier,
            leadingIcon = leadingIcon,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            placeholder = { Text(placeholder) },
            label = { Text(label) },
            singleLine = true,
            visualTransformation = VisualTransformation.None
        )
    }

}