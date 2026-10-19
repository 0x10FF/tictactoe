package com.sample.baseline.game

import org.junit.Test
import org.junit.Assert.*

class GameViewModelTest {
    private fun b(s: String): Board {
        val cells = s.filter { it != ' ' }
        require(cells.length == 9)
        return cells.map { when (it) { 'X' -> Player.X; 'O' -> Player.O; else -> null } }
    }

    @Test
    fun alternate() {
        val vm = GameViewModel()
        vm.onCellTap(0)
        vm.onCellTap(1)
        val state = vm.state.value
        assertEquals(Player.X, state.game.board[0])
        assertEquals(Player.O, state.game.board[1])
        assertEquals(Player.X, state.game.turn)
    }

    @Test
    fun takenCellIgnored() {
        val vm = GameViewModel()
        vm.onCellTap(0)
        val before = vm.state.value
        vm.onCellTap(0)
        val after = vm.state.value
        assertSame(before, after)
    }

    @Test
    fun xWinScoresOnce() {
        val vm = GameViewModel()
        vm.onCellTap(0) // X
        vm.onCellTap(3) // O
        vm.onCellTap(1) // X
        vm.onCellTap(4) // O
        vm.onCellTap(2) // X - wins
        val state = vm.state.value
        assertEquals(Player.X, state.game.winner)
        assertEquals(listOf(0, 1, 2), state.game.winningLine)
        assertEquals(Score(x = 1), state.score)
        val before = vm.state.value
        vm.onCellTap(5) // Should be ignored
        assertSame(before, vm.state.value)
    }

    @Test
    fun draw() {
        val vm = GameViewModel()
        vm.onCellTap(0) // X
        vm.onCellTap(1) // O
        vm.onCellTap(2) // X
        vm.onCellTap(4) // O
        vm.onCellTap(3) // X
        vm.onCellTap(5) // O
        vm.onCellTap(7) // X
        vm.onCellTap(6) // O
        vm.onCellTap(8) // X - draw
        val state = vm.state.value
        assertTrue(state.game.isDraw)
        assertNull(state.game.winner)
        assertEquals(Score(draws = 1), state.score)
    }

    @Test
    fun restartKeepsScore() {
        val vm = GameViewModel()
        vm.onCellTap(0) // X
        vm.onCellTap(3) // O
        vm.onCellTap(1) // X
        vm.onCellTap(4) // O
        vm.onCellTap(2) // X - wins
        vm.restart()
        val state = vm.state.value
        assertEquals(GameState(), state.game)
        assertEquals(Score(x = 1), state.score)
    }

    @Test
    fun setModeResets() {
        val vm = GameViewModel()
        vm.onCellTap(0) // X
        vm.onCellTap(3) // O
        vm.onCellTap(1) // X
        vm.onCellTap(4) // O
        vm.onCellTap(2) // X - wins
        vm.setMode(Mode.VS_COMPUTER)
        val state = vm.state.value
        assertEquals(UiState(mode = Mode.VS_COMPUTER), state)
    }

    @Test
    fun computerReplies() {
        val vm = GameViewModel()
        vm.setMode(Mode.VS_COMPUTER)
        vm.onCellTap(4)
        val state = vm.state.value
        val board = state.game.board
        val xCount = board.count { it == Player.X }
        val oCount = board.count { it == Player.O }
        assertEquals(1, xCount)
        assertEquals(1, oCount)
        assertEquals(Player.X, state.game.turn)
    }

    @Test
    fun computerNeverLoses() {
        for (first in 0..8) {
            val vm = GameViewModel()
            vm.setMode(Mode.VS_COMPUTER)
            vm.onCellTap(first)
            var i = 0
            while (!vm.state.value.game.isOver && i < 9) {
                val board = vm.state.value.game.board
                val emptyIndex = board.indexOfFirst { it == null }
                vm.onCellTap(emptyIndex)
                i++
            }
            val state = vm.state.value
            assertFalse("First move $first resulted in X win", state.game.winner == Player.X)
            assertEquals(0, state.score.x)
        }
    }
}