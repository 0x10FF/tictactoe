package com.sample.baseline.game

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun onCellTap(index: Int) {
        val current = _state.value
        var game = applyMove(current.game, index)
        if (game === current.game) return // invalid tap
        
        if (current.mode == Mode.VS_COMPUTER && !game.isOver && game.turn == Player.O) {
            game = applyMove(game, bestMove(game.board, Player.O))
        }
        
        if (game.isOver) {
            val newScore = when (game.winner) {
                Player.X -> current.score.copy(x = current.score.x + 1)
                Player.O -> current.score.copy(o = current.score.o + 1)
                null -> current.score.copy(draws = current.score.draws + 1)
            }
            _state.value = current.copy(game = game, score = newScore)
        } else {
            _state.value = current.copy(game = game)
        }
    }

    fun restart() {
        _state.value = _state.value.copy(game = GameState())
    }

    fun setMode(mode: Mode) {
        _state.value = UiState(mode = mode)
    }
}