// MainActivity.java
// This class sets up the main screen of the application.
package com.abinstance.tictactoe; // package declaration

// import statements for classes from Android standard packages
import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the activity content from a layout resource
        setContentView(R.layout.activity_main);
    }
} // end class MainActivity