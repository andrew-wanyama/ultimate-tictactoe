// MainFragment.java
// This class defines the fragment used in the main activity.
package com.abinstance.tictactoe; // package declaration

// import statements for classes from Android standard packages
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {
    // a subclass of Dialog that can display one, two or three buttons
    private AlertDialog mDialog;

    /**
     * Create the view for this fragment, using the arguments given to it
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate a new view hierarchy from the specified xml resource
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        // handle buttons here
        View aboutButton = rootView.findViewById(R.id.about_button);
        // register click event listener object to "About" button
        aboutButton.setOnClickListener(
            // anonymous inner class implements event-listener interface
            new View.OnClickListener() {
                /**
                 * Called when a view has been clicked
                 */
                @Override
                public void onClick (View view) {
                    // code here executes on main thread after user presses button
                    // create builder for an alert dialog
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.about_label); // set dialog title
                    builder.setMessage(R.string.about_text); // message to display
                    builder.setCancelable(false); // do not dismiss the dialog
                    // set a listener to be invoked when dialog "OK" is pressed
                    builder.setPositiveButton(R.string.ok_label,
                        // anonymous inner class implements event-listener interface
                        new DialogInterface.OnClickListener() {
                            /**
                             * onClick will be invoked when OK button is clicked
                             */
                            @Override
                            public void onClick (DialogInterface dialog, int which) {
                                // do nothing...
                                Log.d("UT3", which + " OK button clicked!");
                            }
                        }
                    );
                    mDialog = builder.show();
                } // end method onClick
            } // end anonymous inner class
        ); // end call to setOnClickListener

        View newButton = rootView.findViewById(R.id.new_button);
        View continueButton = rootView.findViewById(R.id.continue_button);

        // register event handler for "New Game" button
        newButton.setOnClickListener(
            // anonymous inner class implements event-listener interface
            new View.OnClickListener() {
                /**
                 * Called when a view has been clicked
                 */
                @Override
                public void onClick(View view) {
                    // use explicit intent to trigger GameActivity to start up
                    Intent intent = new Intent(getActivity(), GameActivity.class);
                    getActivity().startActivity(intent);
                }
            } // end anonymous inner class
        ); // end call to setOnClickListener

        // register event handler for "Continue" button
        continueButton.setOnClickListener(
            // anonymous inner class implements event-listener interface
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), GameActivity.class);
                    // use explicit intent to trigger GameActivity to start up
                    intent.putExtra(GameActivity.KEY_RESTORE, true);
                    getActivity().startActivity(intent);
                }
            } // end anonymous inner class
        ); // end call to setOnClickListener

        return rootView;
    } //end lifecycle method OnCreateView

    @Override
    public void onPause() {
        super.onPause();
        // get rid of the "About" dialog if it's still up
        if (mDialog != null)
            mDialog.dismiss();
    }
} // end class MainFragment