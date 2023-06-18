# Ultimate Tic-Tac-Toe

[Ultimate Tic-Tac-Toe](https://mathwithbaddrawings.com/2013/06/16/ultimate-tic-tac-toe/) is an Android board game similar to regular Tic-Tac-Toe except that you can't simply put a mark in your square — you have to win the square by playing a nested game of Tic-Tac-Toe using a 3 x 3 array of smaller tiles inside the big one. A tie happens when there are no further moves. In the case of a tie in a small board, that will count as a win for both sides in the larger game.

At this point, the game involves two human players to play against each other by marking either an X or an O in an empty spot on the board.

The main screen contains three buttons for continuing a game in progress, starting a game, and displaying information about how to play the game. We save the game state every time the app is stopped such as when the user switches to another app on their phone.

## Game Rules

There are 81 small tiles on the game board and each has one of four possible appearances: X (blue), O (red), Empty (gray) or Available (green). You can only move to an Available tile and that's based on past moves and game rules. A small board has 9 tiles arranged in a 3×3 grid. Each small board has a state, just like each small tile - we use a blue color if the X player owns the board, red if owned by O player, gray if nobody owns it, and purple if both players own it (that is, the small board is tied). The large (entire) board is a 3×3 grid of small boards.

One fundamental rule is that when a player makes a move, it forces the opponent to move in the small board that has the same coordinate as the small tile picked by the player. For example, if Player X marks the top-left tile of any small board, then Player O's move has to be somewhere in the top-left small board. If there are no moves available (that is, the destination small board is full), then the second player can move anywhere.

When a small tile is clicked and is available for a move, we set the owner of the small tile to the current player. We then check if anyone has won the board containing the small tile. If so, we set its owner. We also look at the entire board to see if that has an owner at that point. That would mean someone has won the game, so when that happens we report the winner. If not, we switch sides to give the other player a turn.
