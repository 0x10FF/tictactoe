package com.sample.baseline.game

import org.junit.Assert.*
import org.junit.Test

private fun b(s: String): Board {
    val cells = s.filter { it != ' ' }
    require(cells.length == 9)
    return cells.map { when (it) { 'X' -> com.sample.baseline.game.Player.X; 'O' -> com.sample.baseline.game.Player.O; else -> null } }
}

class RulesTest {

    @Test
    fun alternate() {
        val state = GameState()
        val after0 = applyMove(state, 0)
        assertEquals(com.sample.baseline.game.Player.X, after0.board[0])
        assertEquals(com.sample.baseline.game.Player.O, after0.turn)
        
        val after1 = applyMove(after0, 1)
        assertEquals(com.sample.baseline.game.Player.O, after1.board[1])
        assertEquals(com.sample.baseline.game.Player.X, after1.turn)
    }

    @Test
    fun takenCell() {
        val state = GameState()
        val s = applyMove(state, 0)
        assertSame(s, applyMove(s, 0))
    }

    @Test
    fun outOfRange() {
        val state = GameState()
        assertSame(state, applyMove(state, -1))
        assertSame(state, applyMove(state, 9))
    }

    @Test
    fun rowWin() {
        val state = GameState(board = b("XX_ OO_ ___"), turn = com.sample.baseline.game.Player.X)
        val result = applyMove(state, 2)
        assertEquals(com.sample.baseline.game.Player.X, result.winner)
        assertEquals(listOf(0, 1, 2), result.winningLine)
        assertFalse(result.isDraw)
        assertTrue(result.isOver)
    }

    @Test
    fun columnWin() {
        val state = GameState(board = b("OX_ OX_ __X"), turn = com.sample.baseline.game.Player.O)
        val result = applyMove(state, 6)
        assertEquals(com.sample.baseline.game.Player.O, result.winner)
        assertEquals(listOf(0, 3, 6), result.winningLine)
        assertFalse(result.isDraw)
        assertTrue(result.isOver)
    }

    @Test
    fun diagonalWin() {
        val state = GameState(board = b("XO_ OX_ ___"), turn = com.sample.baseline.game.Player.X)
        val result = applyMove(state, 8)
        assertEquals(com.sample.baseline.game.Player.X, result.winner)
        assertEquals(listOf(0, 4, 8), result.winningLine)
        assertFalse(result.isDraw)
        assertTrue(result.isOver)
    }

    @Test
    fun antiDiagonalWin() {
        val state = GameState(board = b("_OX OX_ ___"), turn = com.sample.baseline.game.Player.X)
        val result = applyMove(state, 6)
        assertEquals(com.sample.baseline.game.Player.X, result.winner)
        assertEquals(listOf(2, 4, 6), result.winningLine)
        assertFalse(result.isDraw)
        assertTrue(result.isOver)
    }

    @Test
    fun tapAfterWin() {
        val state = GameState(board = b("XX_ OO_ ___"), turn = com.sample.baseline.game.Player.X)
        val won = applyMove(state, 2)
        assertSame(won, applyMove(won, 5))
    }

    @Test
    fun draw() {
        val state = GameState(board = b("XOX XOO OX_"), turn = com.sample.baseline.game.Player.X)
        val result = applyMove(state, 8)
        assertTrue(result.isDraw)
        assertNull(result.winner)
        assertNull(result.winningLine)
        assertTrue(result.isOver)
    }

    @Test
    fun winOnLastMove() {
        val state = GameState(board = b("XOX OXO OX_"), turn = com.sample.baseline.game.Player.X)
        val result = applyMove(state, 8)
        assertEquals(com.sample.baseline.game.Player.X, result.winner)
        assertEquals(listOf(0, 4, 8), result.winningLine)
        assertFalse(result.isDraw)
        assertTrue(result.isOver)
    }

    @Test
    fun notOverYet() {
        val state = GameState()
        val result = applyMove(state, 4)
        assertFalse(result.isDraw)
        assertNull(result.winner)
        assertNull(result.winningLine)
        assertFalse(result.isOver)
    }
}