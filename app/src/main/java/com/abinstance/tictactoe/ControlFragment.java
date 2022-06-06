// ControlFragment.java
// This class defines the control fragment used in the game activity.
package com.abinstance.tictactoe; // package declaration

// import statements for classes from Android standard packages
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ControlFragment extends Fragment {
    /**
     * Create the view for this fragment, using the arguments given to it.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate a new view hierarchy from the specified xml resource
        View rootView =
                inflater.inflate(R.layout.fragment_control, container, false);

        View mainButton = rootView.findViewById(R.id.main_button);
        View restartButton = rootView.findViewById(R.id.restart_button);

        // register a click event handler when the user taps on "Home" button
        mainButton.setOnClickListener(
            // anonymous inner class implements event-listener interface
            new View.OnClickListener() {
                /**
                 * Called when Home button has been clicked.
                 */
                @Override
                public void onClick (View view) {
                    // finishes the current activity (same as pressing Back)
                    getActivity().finish();
                }
            } // end anonymous inner class
        ); // end setOnClickListener method call

        // register a click event handler when the user taps on Restart button
        restartButton.setOnClickListener(
            // anonymous inner class implements event-listener interface
            new View.OnClickListener() {
                /**
                 * Called when Restart button has been clicked.
                 */
                @Override
                public void onClick (View view) {
                    ((GameActivity) getActivity()).restartGame();
                }
            } // end anonymous inner class
        ); // end setOnClickListener method call

        return rootView;
    } //end lifecycle method OnCreateView
} // end class ControlFragment