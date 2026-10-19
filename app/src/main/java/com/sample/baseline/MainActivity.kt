package com.sample.baseline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.sample.baseline.game.GameViewModel
import com.sample.baseline.ui.GameScreen
import com.sample.baseline.ui.theme.BaselineTemplateTheme
import androidx.lifecycle.viewmodel.compose.viewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaselineTemplateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val vm: GameViewModel = viewModel()
                    val state by vm.state.collectAsState()
                    GameScreen(state, vm::onCellTap, vm::restart, vm::setMode, Modifier.padding(innerPadding))
                }
            }
        }
    }
}