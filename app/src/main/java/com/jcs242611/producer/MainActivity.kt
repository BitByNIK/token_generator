package com.jcs242611.producer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jcs242611.producer.ui.theme.ProducerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProducerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding).fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column {
                            Text("Service Application")

                            Button(
                                onClick = {
                                    val intent = Intent(this@MainActivity, MyService::class.java)
                                    startService(intent)
                                    Toast.makeText(this@MainActivity, "Start Service", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text("Start Service")
                            }

                            Button(
                                onClick = {
                                    val intent = Intent(this@MainActivity, MyService::class.java)
                                    stopService(intent)
                                    Toast.makeText(this@MainActivity, "Stop Service", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text("Stop Service")
                            }
                        }
                    }
                }
            }
        }
    }
}