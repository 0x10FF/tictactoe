package com.sample.baseline.game

import com.sample.baseline.game.Player
import com.sample.baseline.game.Board
import com.sample.baseline.game.winningLine
import com.sample.baseline.game.EMPTY_BOARD

/**
 * Minimax AI implementation for Tic-tac-toe.
 *
 * @param board The current board state
 * @param me The player we're optimizing for (X or O)
 * @return The index of the optimal move
 */
fun bestMove(board: Board, me: Player): Int {
    var best = -1
    var bestScore = Int.MIN_VALUE
    for (i in 0..8) {
        if (board[i] != null) continue
        val next = board.toMutableList().also { it[i] = me }
        val score = minimax(next, me.other(), me, 1)
        if (score > bestScore) { bestScore = score; best = i }
    }
    return best
}

private fun minimax(board: Board, toMove: Player, me: Player, depth: Int): Int {
    val line = winningLine(board)
    if (line != null) return if (board[line[0]] == me) 10 - depth else depth - 10
    if (board.none { it == null }) return 0
    var best = if (toMove == me) Int.MIN_VALUE else Int.MAX_VALUE
    for (i in 0..8) {
        if (board[i] != null) continue
        val next = board.toMutableList().also { it[i] = toMove }
        val score = minimax(next, toMove.other(), me, depth + 1)
        best = if (toMove == me) maxOf(best, score) else minOf(best, score)
    }
    return best
}