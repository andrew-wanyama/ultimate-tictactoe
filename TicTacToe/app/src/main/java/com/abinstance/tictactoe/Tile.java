// Tile.java
// This class represents a square on the board at any of the three levels.
package com.abinstance.tictactoe; // package declaration

// import statements for classes from Android standard packages
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class Tile {
    // enumeration with constants that represent owner of a Tile
    public enum Owner {
        X,
        O,
        NEITHER,
        BOTH
    }

    // These levels are defined in the drawable definitions
    private static final int LEVEL_X = 0; // blue (both square or small board)
    private static final int LEVEL_O = 1; // red (both square or small board)
    private static final int LEVEL_BLANK = 2; // neither owns small board (gray) or empty square
    private static final int LEVEL_AVAILABLE = 3; // small square available (green)
    private static final int LEVEL_TIE = 3; // both own the small board (purple)

    private GameFragment mGame;
    private Owner mOwner = Owner.NEITHER;
    private View mView;
    private Tile[] mSubTiles;

    // Tile constructor
    public Tile(GameFragment game) {
        this.mGame = game;
    }

    // get Tile view
    public View getView() {
        return mView;
    }

    // set Tile view
    public void setView(View view) {
        this.mView = view;
    }

    // get Tile owner
    public Owner getOwner() {
        return mOwner;
    }

    // set Tile owner
    public void setOwner(Owner owner) {
        this.mOwner = owner;
    }

    // get Tile subTiles
    public Tile[] getSubTiles() {
        return mSubTiles;
    }

    // set Tile subTiles
    public void setSubTiles(Tile[] subTiles) {
        this.mSubTiles = subTiles;
    }

    public void updateDrawableState() {
        if (mView == null)
            return; // skip if Tile does not have its view set yet
        int level = getLevel();

        // test for small board's background
        if (mView.getBackground() != null)
            mView.getBackground().setLevel(level);

        // determine whether element is an ImageButton i.e., a small square
        if (mView instanceof ImageButton) {
            Drawable drawable = ((ImageButton) mView).getDrawable();
            // set a small square's src with updated state
            drawable.setLevel(level);
        }
    } // end method updateDrawableState

    private int getLevel() {
        int level = LEVEL_BLANK; // 2 - empty small square and gray small board
        // determine Tile owner and drawable's level
        switch(mOwner) {
            case X:
                level = LEVEL_X; // 0
                break;
            case O:
                level = LEVEL_O; // 1
                break;
            case BOTH:
                level = LEVEL_TIE; // 3 (applies only to small board)
                break;
            case NEITHER: // ? 3 (small greens) : 2 (empty square, small board)
                level = mGame.isAvailable(this) ? LEVEL_AVAILABLE : LEVEL_BLANK;
                break;
        }
        return level;
    } // end method getLevel

    // used by current small board and entire board to find a winner
    public Owner findWinner() {
        // if owner already calculated, return it
        if (getOwner() != Owner.NEITHER)
            return getOwner();

        // each element of these integer arrays initially gets default value 0
        int[] totalX = new int[4];
        int[] totalO = new int[4];
        /**
         * check if 3 sub-tiles have been captured in-a-row i.e., are aligned.
         * read as "frequency of [0 | 1 | 2 | 3] X's or O's is the index value" or
         * read as "number of [0 | 1 | 2 | 3]-in-a-row for X or O".
         */
        countCaptures(totalX, totalO);

        // here we are interested in a frequency of 3 occurrences, i.e., a win!
        for (int X = 0; X < totalX.length; X++)
            Log.d("Frequency of", " " + X + " X's is " + totalX[X]);
        for (int O = 0; O < totalO.length; O++)
            Log.d("Frequency of", " " + O + " O's is " + totalO[O]);
        if (totalX[3] > 0)
            return Owner.X;
        if (totalO[3] > 0)
            return Owner.O;

        // check for a draw or tie (eliminates X or O as owners)
        int total = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                // owners of current tile sub-tiles (index 0-8)
                // (0, 1, 2) (3, 4, 5) (6, 7, 8
                Owner owner = mSubTiles[3 * row + col].getOwner();
                // if claimed, accumulate the total
                if (owner != Owner.NEITHER)
                    total++;
            }
            // after checking all sub-tile indexes (a row at a time),
            // and all are claimed by X or O (but no 3-in-a-row), declare a tie
            if (total == 9)
                return Owner.BOTH;
        }

        // if there are still unclaimed sub-tile indexes (i.e., total != 9),
        // (and no 3-in-a-row), neither player has won this tile
        return Owner.NEITHER;
    } // end method findWinner

    // count the number of tiles claimed by each player
    private void countCaptures(int[] totalX, int[] totalO) {
        // declare two int variables to store accumulated counts for X and O
        int capturedX, capturedO;

        // check the horizontal
        for (int row = 0; row < 3; row++) {
            // reset (capturedX and capturedO to have maximum count of 3)
            capturedX = capturedO = 0;

            for (int col = 0; col < 3; col++) {
                // owners of current tile sub-tiles (index 0-8)
                // check these positions: (0, 1, 2) (3, 4, 5) (6, 7, 8)
                Owner owner = mSubTiles[3 * row + col].getOwner();
                // if claimed by X or BOTH, accumulate total for X
                if (owner == Owner.X || owner == Owner.BOTH)
                    capturedX++;
                // if claimed by O or BOTH, accumulate total for O
                if (owner == Owner.O || owner == Owner.BOTH)
                    capturedO++;
            } // end inner for (horizontal check)
            /**
             * increment (+1) value of Xs from default 0 (index is number of Xs)
             * assume after first loop, capturedX is 3, then value of totalX[3] is 1
             * after third loop, totalX[3] have will have a maximum of 3 (unlikely).
             * we only need totalX[3] to be at least 1 to win the tile!
             */
            totalX[capturedX]++; // increments the value at index capturedX
            totalO[capturedO]++; // increments the element at position capturedO
        } // end outer for (horizontal check)

        // check the vertical
        for (int col = 0; col < 3; col++) {
            capturedX = capturedO = 0;
            for (int row = 0; row < 3; row++) {
                // (0, 3, 6) (1, 4, 7) (2, 5, 8)
                Owner owner = mSubTiles[3 * row + col].getOwner();
                if (owner == Owner.X || owner == Owner.BOTH)
                    capturedX++;
                if (owner == Owner.O || owner == Owner.BOTH)
                    capturedO++;
            } // end inner for (vertical check)
            totalX[capturedX]++;
            totalO[capturedO]++;
        } // end outer for (vertical check)

        // check on the first diagonal (top-left to bottom-right)
        capturedX = capturedO = 0;
        for (int diag = 0; diag < 3; diag++) {
            // (0, 4, 8)
            Owner owner = mSubTiles[3 * diag + diag].getOwner();
            if (owner == Owner.X || owner == Owner.BOTH)
                capturedX++;
            if (owner == Owner.O || owner == Owner.BOTH)
                capturedO++;
        } // end for (first diagonal)
        totalX[capturedX]++;
        totalO[capturedO]++;

        // check on the second diagonal (top-right to bottom-left)
        capturedX = capturedO = 0;
        for (int diag = 0; diag < 3; diag++) {
            // (2, 4, 6)
            Owner owner = mSubTiles[3 * diag + (2 - diag)].getOwner();
            if (owner == Owner.X || owner == Owner.BOTH)
                capturedX++;
            if (owner == Owner.O || owner == Owner.BOTH)
                capturedO++;
        } // end for (second diagonal)
        totalX[capturedX]++;
        totalO[capturedO]++;
    } // end method countCaptures
} // end class Tile