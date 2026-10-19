package com.sample.baseline.game

/**
 * Game contract (WP0). Other packages must use these types exactly and must not edit this file.
 *
 * Functions implemented by other packages, with fixed signatures:
 * - WP1 `game/Rules.kt`: `fun applyMove(state: GameState, index: Int): GameState`
 *   Plays `state.turn` at `index`. Returns `state` unchanged if the game is over, `index` is
 *   outside 0..8, or the cell is taken. Otherwise sets `winner` and `winningLine` when the move
 *   completes a line, sets `isDraw` only when the board is full with no winner, and switches `turn`.
 * - WP2 `game/Ai.kt`: `fun bestMove(board: Board, me: Player): Int`
 *   Minimax. Returns the index of an empty cell that is optimal for `me`. Never loses.
 *   Precondition: the board has an empty cell and no winner.
 * - WP3 `game/GameViewModel.kt`: `class GameViewModel : ViewModel()` with
 *   `val state: StateFlow<UiState>`, `fun onCellTap(index: Int)`, `fun restart()`, `fun setMode(mode: Mode)`.
 * - WP4 `ui/GameScreen.kt`: `@Composable fun GameScreen(state: UiState, onCellTap: (Int) -> Unit,
 *   onRestart: () -> Unit, onModeChange: (Mode) -> Unit, modifier: Modifier = Modifier)`.
 */

enum class Player {
    X, O;

    fun other(): Player = if (this == X) O else X
}

enum class Mode { TWO_PLAYERS, VS_COMPUTER }

/** 9 cells, index = row * 3 + col; null means empty. */
typealias Board = List<Player?>

val EMPTY_BOARD: Board = List(9) { null }

val WIN_LINES: List<List<Int>> = listOf(
    listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
    listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
    listOf(0, 4, 8), listOf(2, 4, 6),
)

/** The first completed line on [board], or null if nobody has three in a row. */
fun winningLine(board: Board): List<Int>? = WIN_LINES.firstOrNull { (a, b, c) ->
    board[a] != null && board[a] == board[b] && board[a] == board[c]
}

data class GameState(
    val board: Board = EMPTY_BOARD,
    val turn: Player = Player.X,
    val winner: Player? = null,
    val winningLine: List<Int>? = null,
    val isDraw: Boolean = false,
) {
    val isOver: Boolean get() = winner != null || isDraw
}

data class Score(val x: Int = 0, val o: Int = 0, val draws: Int = 0)

/** In VS_COMPUTER the human is X and the computer is O. */
data class UiState(
    val game: GameState = GameState(),
    val mode: Mode = Mode.TWO_PLAYERS,
    val score: Score = Score(),
)
