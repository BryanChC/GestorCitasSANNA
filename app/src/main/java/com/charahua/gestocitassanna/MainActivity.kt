package com.charahua.gestocitassanna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.charahua.gestocitassanna.presentation.navigation.SannaNavGraph
import com.charahua.gestocitassanna.ui.theme.GestoCitasSANNATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GestoCitasSANNATheme {
                SannaNavGraph()
            }
        }
    }
}
