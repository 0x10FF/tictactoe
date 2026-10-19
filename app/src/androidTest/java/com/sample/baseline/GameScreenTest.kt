package com.sample.baseline

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sample.baseline.game.GameViewModel
import com.sample.baseline.ui.GameScreen
import com.sample.baseline.ui.theme.BaselineTemplateTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameScreenTest {
    @get:Rule
    val rule = createComposeRule()

    private fun setUp(): GameViewModel {
        val vm = GameViewModel()
        rule.setContent {
            BaselineTemplateTheme {
                val s by vm.state.collectAsState()
                GameScreen(s, vm::onCellTap, vm::restart, vm::setMode)
            }
        }
        return vm
    }

    @Test
    fun firstTapPlacesX() {
        val vm = setUp()
        rule.onNodeWithTag("cell_0").performClick()
        rule.onNodeWithTag("cell_0").assertTextEquals("X")
        rule.onNodeWithText("O's turn").assertExists()
    }

    @Test
    fun winIsShownAndBoardLocks() {
        val vm = setUp()
        // Click cells 0, 3, 1, 4, 2
        rule.onNodeWithTag("cell_0").performClick()
        rule.onNodeWithTag("cell_3").performClick()
        rule.onNodeWithTag("cell_1").performClick()
        rule.onNodeWithTag("cell_4").performClick()
        rule.onNodeWithTag("cell_2").performClick()
        
        rule.onNodeWithText("X wins").assertExists()
        // Try clicking cell 5 - should not change anything
        rule.onNodeWithTag("cell_5").performClick()
        rule.onNodeWithTag("cell_5").assertTextEquals("")
    }

    @Test
    fun restartClears() {
        val vm = setUp()
        // Click cells 0, 1
        rule.onNodeWithTag("cell_0").performClick()
        rule.onNodeWithTag("cell_1").performClick()
        
        // Restart
        rule.onNodeWithText("Restart").performClick()
        
        // Verify cells are empty
        rule.onNodeWithTag("cell_0").assertTextEquals("")
        rule.onNodeWithTag("cell_1").assertTextEquals("")
        rule.onNodeWithText("X's turn").assertExists()
    }

    @Test
    fun computerReplies() {
        val vm = setUp()
        // Set mode to VS_COMPUTER
        rule.onNodeWithText("vs computer").performClick()
        // Click cell 4
        rule.onNodeWithTag("cell_4").performClick()
        
        // Should have exactly one O (the computer's move)
        rule.onAllNodesWithText("O").assertCountEquals(1)
    }
}