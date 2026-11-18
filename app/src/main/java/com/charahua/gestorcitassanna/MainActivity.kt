package com.charahua.gestorcitassanna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.charahua.gestorcitassanna.presentation.navigation.SannaNavGraph
import com.charahua.gestorcitassanna.ui.theme.GestorCitasSANNATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GestorCitasSANNATheme() {
                SannaNavGraph()
            }
        }
    }
}
