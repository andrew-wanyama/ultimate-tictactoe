package com.abinstance.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class GameActivity extends Activity {
    public static final String KEY_RESTORE = "com.abinstance.tictactoe.key_restore";
    public static final String PREF_RESTORE = "com.abinstance.tictactoe.pref_restore";
    private GameFragment mGameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
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
    }

    public void showNextPlayer(final Tile.Owner next) {
        TextView nextPlayerTextView = findViewById(R.id.next_player);
        nextPlayerTextView.setText(getString(R.string.next_player, next));
    }

    public void reportWinner(final Tile.Owner winner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.declare_winner, winner));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok_label,
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int which) {
                finish();
            }
          });
        final Dialog dialog = builder.create();
        dialog.show();
        mGameFragment.initGame();
    }

    public void restartGame() {
        mGameFragment.restartGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        String gameData = mGameFragment.getState();
        getPreferences(MODE_PRIVATE).edit()
                .putString(PREF_RESTORE, gameData)
                .commit();
        Log.d("UT3", "saving game state = " + gameData);
    }
}