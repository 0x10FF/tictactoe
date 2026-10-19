package com.sample.baseline.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sample.baseline.game.*
import com.sample.baseline.R
import androidx.compose.ui.tooling.preview.Preview
import com.sample.baseline.ui.theme.BaselineTemplateTheme

@Composable
fun GameScreen(
    state: UiState,
    onCellTap: (Int) -> Unit,
    onRestart: () -> Unit,
    onModeChange: (Mode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mode selection
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = state.mode == Mode.TWO_PLAYERS,
                onClick = { onModeChange(Mode.TWO_PLAYERS) },
                label = { Text(stringResource(R.string.mode_two_players)) }
            )
            FilterChip(
                selected = state.mode == Mode.VS_COMPUTER,
                onClick = { onModeChange(Mode.VS_COMPUTER) },
                label = { Text(stringResource(R.string.mode_vs_computer)) }
            )
        }

        // Status text
        val statusText = when {
            state.game.winner == Player.X -> stringResource(R.string.win_x)
            state.game.winner == Player.O -> stringResource(R.string.win_o)
            state.game.isDraw -> stringResource(R.string.draw)
            state.game.turn == Player.X -> stringResource(R.string.turn_x)
            else -> stringResource(R.string.turn_o)
        }
        Text(
            text = statusText,
            style = MaterialTheme.typography.headlineSmall
        )

        // Game grid
        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
        ) {
            for (row in 0..2) {
                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    for (col in 0..2) {
                        val i = row * 3 + col
                        val cellValue = state.game.board[i]
                        val mark = when (cellValue) {
                            Player.X -> "X"
                            Player.O -> "O"
                            null -> ""
                        }
                        val color = if (state.game.winningLine?.contains(i) == true) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(4.dp)
                                .background(color)
                                .border(1.dp, MaterialTheme.colorScheme.outline)
                                .testTag("cell_$i")
                                .clickable { onCellTap(i) },
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Text(
                                text = mark,
                                style = MaterialTheme.typography.displayMedium
                            )
                        }
                    }
                }
            }
        }

        // Score text
        Text(
            text = stringResource(R.string.score, state.score.x, state.score.o, state.score.draws),
            style = MaterialTheme.typography.bodyLarge
        )

        // Restart button
        Button(onClick = onRestart) {
            Text(stringResource(R.string.restart))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    BaselineTemplateTheme { GameScreen(UiState(), {}, {}, {}) }
}

@Preview(showBackground = true)
@Composable
fun GameScreenWonPreview() {
    BaselineTemplateTheme {
        GameScreen(
            UiState(game = GameState(board = listOf(Player.X, Player.X, Player.X, Player.O, Player.O, null, null, null, null), winner = Player.X, winningLine = listOf(0, 1, 2))),
            {}, {}, {}
        )
    }
}