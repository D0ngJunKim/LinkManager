package com.linkmanager.example

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.linkmanager.example.ui.theme.LinkManagerTheme
import com.linkmanager.lib.LinkManager
import com.linkmanager.lib.UrlParser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UrlParser.init(application)
        enableEdgeToEdge()
        setContent {
            LinkManagerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LazyColumn(contentPadding = innerPadding) {
                        items(6 ) { index ->
                            val url = when (index) {
                                0 -> "https://www.google.com"
                                1 -> "https://www.bbc.com/travel"
                                2 -> "https://shopping.naver.com/living/homeliving/home"
                                3 -> "https://shopping.naver.com/logistics/category?menu=10000026"
                                4 -> "https://webtoon.kakao.com"
                                5 -> "https://www.samsung.com/sec/"
                                else -> ""
                            }

                            Button(url, applicationContext)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Composable
fun Button(url: String, context: Context) {
    Button(onClick = {
        LinkManager.openUrl(url, mutableMapOf("context" to context))
    }) {
        Text(url)
    }
}

@Composable
fun GreetingPreview() {
    LinkManagerTheme {
        Greeting("Android")
    }
}