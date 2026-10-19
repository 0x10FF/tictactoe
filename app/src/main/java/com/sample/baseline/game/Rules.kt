package com.sample.baseline.game

/**
 * WP1: Rules implementation
 */
fun applyMove(state: GameState, index: Int): GameState {
    // If game is over, or index is out of range, or cell is taken, return state unchanged
    if (state.isOver || index !in 0..8 || state.board[index] != null) {
        return state
    }

    // Build the new board with the current player at the specified index
    val newBoard = state.board.toMutableList()
    newBoard[index] = state.turn

    // Compute the winning line
    val line = winningLine(newBoard)

    // If there's a winning line, the game is over
    if (line != null) {
        return state.copy(
            board = newBoard,
            winner = state.turn,
            winningLine = line
        )
    }

    // If no cell is null, it's a draw
    if (newBoard.none { it == null }) {
        return state.copy(
            board = newBoard,
            isDraw = true
        )
    }

    // Otherwise, switch the turn
    return state.copy(
        board = newBoard,
        turn = state.turn.other()
    )
}