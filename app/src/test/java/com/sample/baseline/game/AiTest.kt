package com.sample.baseline.game

import com.sample.baseline.game.Player
import com.sample.baseline.game.Board
import com.sample.baseline.game.winningLine
import com.sample.baseline.game.EMPTY_BOARD
import org.junit.Assert.*
import org.junit.Test

class AiTest {
    private fun b(s: String): Board {
        val cells = s.filter { it != ' ' }
        require(cells.length == 9)
        return cells.map { when (it) { 'X' -> Player.X; 'O' -> Player.O; else -> null } }
    }

    private fun place(board: Board, i: Int, p: Player): Board = board.toMutableList().also { it[i] = p }
    private fun over(board: Board): Boolean = winningLine(board) != null || board.none { it == null }

    /** X (human) tries every move; O is the AI. X must never win. */
    private fun assertXNeverWins(board: Board) {
        for (i in 0..8) {
            if (board[i] != null) continue
            val afterX = place(board, i, Player.X)
            assertNull("X won: $afterX", winningLine(afterX))
            if (over(afterX)) continue
            val o = bestMove(afterX, Player.O)
            assertNull(afterX[o])
            val afterO = place(afterX, o, Player.O)
            if (!over(afterO)) assertXNeverWins(afterO)
        }
    }

    /** X is the AI and moves first; O (human) tries every move. O must never win. */
    private fun assertONeverWins(board: Board) {
        val x = bestMove(board, Player.X)
        assertNull(board[x])
        val afterX = place(board, x, Player.X)
        if (over(afterX)) return
        for (i in 0..8) {
            if (afterX[i] != null) continue
            val afterO = place(afterX, i, Player.O)
            assertNull("O won: $afterO", winningLine(afterO))
            if (!over(afterO)) assertONeverWins(afterO)
        }
    }

    @Test
    fun takesWin() {
        assertEquals(2, bestMove(b("XX_ OO_ ___"), Player.X))
    }

    @Test
    fun blocksLoss() {
        assertEquals(2, bestMove(b("XX_ _O_ ___"), Player.O))
    }

    @Test
    fun cornerReplyToCenter() {
        val m = bestMove(b("___ _X_ ___"), Player.O)
        assertTrue(m in setOf(0, 2, 6, 8))
    }

    @Test
    fun returnsEmptyCell() {
        val boards = listOf(
            b("XX_ OO_ ___"),
            b("XX_ _O_ ___"),
            b("___ _X_ ___")
        )
        for (board in boards) {
            val m = bestMove(board, Player.X)
            assertNull(board[m])
        }
    }

    @Test
    fun aiAsONeverLoses() {
        assertXNeverWins(EMPTY_BOARD)
    }

    @Test
    fun aiAsXNeverLoses() {
        assertONeverWins(EMPTY_BOARD)
    }
}