// GameActivity.java
// This activity represents the game board with tiles and controls.
package com.abinstance.tictactoe; // package declaration

// import statements for classes from Android standard packages
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class GameActivity extends Activity {
    public static final String KEY_RESTORE = "key_restore";
    public static final String PREF_RESTORE = "pref_restore";
    private GameFragment mGameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the activity content from a layout resource
        setContentView(R.layout.activity_game);
        // restore game
        mGameFragment = (GameFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_game);
        boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);
        if (restore) {
            String gameData = getPreferences(MODE_PRIVATE)
                    .getString(PREF_RESTORE, null);
            if (gameData != null) {
                Log.d("UT3", "restoring " + gameData);
                mGameFragment.putState(gameData);
            }
        }
        Log.d("UT3", "restore " + restore);
    } // end method onCreate

    // show next player view
    public void showNextPlayer(final Tile.Owner next) {
        TextView nextPlayerTextView = findViewById(R.id.next_player);
        nextPlayerTextView.setText(getString(R.string.next_player, next));
    }

    // announce winner after the winning move
    public void reportWinner(final Tile.Owner winner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.declare_winner, winner)); // message
        builder.setCancelable(false); // show even when user taps outside the box
        builder.setPositiveButton(R.string.ok_label,
            // anonymous inner class implements event-listener interface
            new DialogInterface.OnClickListener() {
                /**
                 * onClick will be invoked when OK button is clicked
                 */
                @Override
                public void onClick (DialogInterface dialog, int which)
                {
                    finish();
                }
            } // end anonymous inner class
        ); // end method call setPositiveButton

        final Dialog dialog = builder.create();
        dialog.show();
        mGameFragment.initGame(); // reset the board to the initial position
    } // end method reportWinner

    // invoked when the user selects the "Restart" button
    public void restartGame() {
        mGameFragment.restartGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // get game data from fragment and save to the preferences
        String gameData = mGameFragment.getState();
        getPreferences(MODE_PRIVATE).edit()
                .putString(PREF_RESTORE, gameData)
                .commit();
        Log.d("UT3", "state = " + gameData);
    } // end method onPause
} // end class GameActivity