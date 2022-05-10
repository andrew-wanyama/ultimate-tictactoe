// GameFragment.java
// This class defines the game fragment used in the game activity.
package com.abinstance.tictactoe; // package declaration

// import statements for classes from Android standard packages
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

// import statements for Java's collections-framework interfaces and classes
import java.util.HashSet; // interface that does not contain duplicates
import java.util.Set; // interface Set implementation that uses a hash table

public class GameFragment extends Fragment {
    /** mLargeIds and mSmallIds are constant arrays that map a number (0 through 8)
     * onto the resource ids of the large and small tiles, respectively
     */
    private static int[] mLargeIds = { R.id.large1, R.id.large2, R.id.large3,
            R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8,
            R.id.large9 };
    private static int[] mSmallIds = { R.id.small1, R.id.small2, R.id.small3,
            R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8,
            R.id.small9 };
    /**
     * Tile class represents one tile at any level of the game
     */
    private Tile mEntireBoard = new Tile(this);; // 1 tile for the whole game
    private Tile[] mLargeTiles = new Tile[9]; // 9 tiles (small boards)
    private Tile[][] mSmallTiles = new Tile[9][9]; // 81 tiles (small squares)

    private Tile.Owner mPlayer = Tile.Owner.X; // player X always goes first

    /** create a HashSet to contain unique Tile objects where it’s possible
     *  to make a move at any given time, based on past moves and game rules
     */
    private Set<Tile> mAvailable = new HashSet<Tile>();

    // indices of the last move
    private int mLastSmall;
    private int mLastLarge; // will be used to save game when it's paused

    /**
     * onCreate method is called to do initial creation of the fragment
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment across configuration changes
        setRetainInstance(true);
        initGame(); // set up all our data structures
    }

    /**
     * Create the view for this fragment, using the arguments given to it
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate a new view hierarchy from the specified xml resource
        View rootView =
            inflater.inflate(R.layout.large_board, container, false);
        initViews(rootView);
        updateAllTiles();
        return rootView;
    }

    // invoked when the fragment's activity has been created
    // and this fragment's view hierarchy instantiated
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(null);
        updateNextPlayerTextView();
    }

    private void updateNextPlayerTextView() {
        ((GameActivity) getActivity()).showNextPlayer(mPlayer);
    }

    // initializes the game state
    public void initGame() {
        Log.d("UT3", "init game");
        mEntireBoard = new Tile(this); // initialize the large board

        // create all the tiles (9 small boards and 81 small tiles)
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large] = new Tile(this);
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small] = new Tile(this);
            }
            // set sub-tiles for each of the 9 small boards
            mLargeTiles[large].setSubTiles(mSmallTiles[large]);
        }
        // set sub-tiles for the entire board
        mEntireBoard.setSubTiles(mLargeTiles);

        // mark all small squares as available for a move in the next turn
        mLastSmall = -1;
        mLastLarge = -1;
        setAvailableFromLastMove(mLastSmall);
    } // end method initGame

    // initialize the views for the GameFragment
    private void initViews(View rootView) {
        mEntireBoard.setView(rootView); // large_board layout as board's view

        for (int large = 0; large < 9; large++) {
            // set views for each of the small boards
            View outer = rootView.findViewById(mLargeIds[large]);
            mLargeTiles[large].setView(outer);

            for (int small = 0; small < 9; small++) {
                ImageButton inner =
                    (ImageButton) outer.findViewById(mSmallIds[small]);
                // current indices will be bound to each small tile
                final int fLarge = large;
                final int fSmall = small;
                final Tile smallTile = mSmallTiles[large][small];
                // set ImageButtons for each of the 81 squares
                smallTile.setView(inner);

                // register a click event handler when user taps a small tile
                inner.setOnClickListener(
                    // anonymous inner class implements event-listener interface
                    new View.OnClickListener() {
                        /**
                         * called when a small square has been clicked
                         */
                        @Override
                        public void onClick(View view) {
                            // check if square is available for a move
                            if (isAvailable(smallTile)) {
                                makeMove(fLarge, fSmall);
                                switchTurns();
                            }
                        }
                    } // end anonymous inner class
                ); // end setOnClickListener method call
            } // end inner for
        } // end outer for
    } // end method initViews

    // invoked when a square is clicked and is available for a move
    private void makeMove(int large, int small) {
        // indices and tiles of the last move (where player moved to)
        mLastLarge = large;
        mLastSmall = small;
        Tile smallTile = mSmallTiles[large][small];
        Tile largeTile = mLargeTiles[large];
        // set the owner of the small tile to the current player
        smallTile.setOwner(mPlayer); // X always goes first

        // check if board containing the small tile has been won
        Tile.Owner previousOwner = largeTile.getOwner();
        Tile.Owner winner = largeTile.findWinner();
        if (winner != previousOwner) {
            largeTile.setOwner(winner);
        }

        // determine destination of the opponent
        setAvailableFromLastMove(small);

        // look at the entire board to see if we have a winner
        winner = mEntireBoard.findWinner();
        mEntireBoard.setOwner(winner);

        updateAllTiles(); // redraw tiles to show new state

        // report winner if entire board has an owner
        if (winner != Tile.Owner.NEITHER)
            ((GameActivity) getActivity()).reportWinner(winner);
    } // end method makeMove

    // switch players
    private void switchTurns() {
        // toggle the value of the mPlayer variable
        mPlayer = mPlayer == Tile.Owner.X ? Tile.Owner.O : Tile.Owner.X;
        updateNextPlayerTextView();
    }

    // initialize game state when user selects the "Restart" button
    public void restartGame() {
        initGame(); // initializes the game state
        initViews(getView()); // initializes the views again
        updateAllTiles(); // updates all the tiles to show new state
        mPlayer = Tile.Owner.X; // X always goes first
        updateNextPlayerTextView();
    }

    // clears the available list of tiles so more can be added
    private void clearAvailable() {
        mAvailable.clear();
    }

    // add to the list of tiles available for a move
    private void addAvailable(Tile tile) {
        mAvailable.add(tile);
    }

    // checks if tile is available for a move
    public boolean isAvailable(Tile tile) {
        return mAvailable.contains(tile);
    }

    // mark unoccupied tiles at the destination large board as available
    private void setAvailableFromLastMove(int small) {
        clearAvailable(); // clear the available list of tiles

        if (small != -1) {
            // is the large board at the destination claimed?
            Tile destlargeTile = mLargeTiles[small];
            if (destlargeTile.getOwner() == Tile.Owner.NEITHER) {
                for (int dest = 0; dest < 9; dest++) {
                    // target tiles in large board with same co-ord as small tile
                    Tile tile = mSmallTiles[small][dest];
                    // add unoccupied tiles to list of available tiles
                    if (tile.getOwner() == Tile.Owner.NEITHER)
                        addAvailable(tile);
                }
            }
        }

        /** if there were none available, make all squares available
         * here is where first fake move from -1 makes all tiles available!
         * also, if all squares in the target board are claimed
         */
        if (mAvailable.isEmpty())
            setAllAvailable();
    } // end method setAvailableFromLastMove

    // mark all unoccupied tiles in all boards as available
    private void setAllAvailable() {
        for (int large = 0; large < 9; large++) {
            // if large tile is won skip small tiles within it
            if (mLargeTiles[large].getOwner() != Tile.Owner.NEITHER)
                continue;
            for (int small = 0; small < 9; small++) {
                Tile tile = mSmallTiles[large][small];
                if (tile.getOwner() == Tile.Owner.NEITHER)
                    addAvailable(tile);
            }
        }
    } // end method setAllAvailable

    // create a string containing the state of the game
    public String getState() {
        StringBuilder builder = new StringBuilder();
        // save indices of the last moves
        builder.append(mLastLarge);
        builder.append(',');
        builder.append(mLastSmall);
        builder.append(',');
        // save current player
        builder.append(mPlayer.name());
        builder.append(',');
        for (int large = 0; large < 9; large++) {
            // Save the owner of each large tile
            builder.append(mLargeTiles[large].getOwner().name());
            builder.append(',');
            for (int small = 0; small < 9; small++) {
                // save the owner of each small tile
                builder.append(mSmallTiles[large][small].getOwner().name());
                builder.append(',');
            }
        }
        return builder.toString();
    } // end method getState

    // restore the state of the game from the given string
    public void putState(String gameData) {
        String[] fields = gameData.split(",");
        int index = 0;
        mLastLarge = Integer.parseInt(fields[index++]); // 0
        mLastSmall = Integer.parseInt(fields[index++]); // 1
        mPlayer = Tile.Owner.valueOf(fields[index++]); // 2
        for (int large = 0; large < 9; large++) {
            // restore each large tile
            Tile.Owner lOwner = Tile.Owner.valueOf(fields[index++]); // 3...
            mLargeTiles[large].setOwner(lOwner);
            for (int small = 0; small < 9; small++) {
                // restore each small tile
                Tile.Owner sOwner = Tile.Owner.valueOf(fields[index++]); // 4...
                mSmallTiles[large][small].setOwner(sOwner);
            }
        }
        setAvailableFromLastMove(mLastSmall);
        updateAllTiles();
    } // end method putState

    // redraws tiles to show new state that is, set level-lists based on owner
    private void updateAllTiles() {
        mEntireBoard.updateDrawableState();
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large].updateDrawableState();
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small].updateDrawableState();
            }
        }
    } // end method updateAllTiles
} // end class GameFragment