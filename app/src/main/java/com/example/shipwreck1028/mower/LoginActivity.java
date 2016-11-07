package com.example.shipwreck1028.mower;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class LoginActivity extends AppCompatActivity {

    String className = this.getClass().getSimpleName();
    Boolean Valid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * Opens a new Intent of Register Activity.
     * Handled in the OnClick() for the register text.
     *
     * @param view You are required to pass this through
     */
    public void register(View view) {
        // Open a new RegisterActivity, thi is referenced just once on the
        // click of the "register" TextView under the submit button.
        Log.w(className, "Opening a new Register Activity");
        Intent newRegisterWindow = new Intent(this, RegisterActivity.class);
        startActivity(newRegisterWindow);
    }

    /**
     * Gets the text from the Username & Password field and
     * validates it to ensure there is text entered and that
     * it is a valid user from the database.
     *
     * @param view You are required to pass this through
     */
    public void validateCreds(View view) {
        // Get the Username & Password from the elements on the page
        // by their ID and set variables.
        Log.w(className, "Getting Username & Password");
        EditText usernameText = (EditText) findViewById(R.id.username);
        EditText passwordText = (EditText) findViewById(R.id.password);
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        Log.w("Username", username);
        Log.w("Password", password);

        boolean valid = true;
        // Check for the following:
        // 1. Empty Username Field
        // 2. Empty Password Field
        if (!(username.length() > 0)) {
            valid = false;
            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.no_username_error);
        } else if (!(password.length() > 0)) {
            valid = false;
            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.no_password_error);
        }

        new MyTask().execute(username);

        // If Username & Password have passed all other tests,
        // create a new intent and move them to the MainActivity.
        if (valid) {
            Log.w("Bleh", Valid + "");
            if (getValid()) {
                TextView t = (TextView) findViewById(R.id.errorBox);
                t.setTextColor(Color.parseColor("#FF0000"));
                t.setText(R.string.bad_creds_error);
            } else {
                Intent newRegisterWindow = new Intent(this, MainActivity.class);
                startActivity(newRegisterWindow);
            }
        }

    }

    @Override
    public void onBackPressed() {
        // Disallowing them to press the back button here ensures that
        // when a user is logged out, they cannot hit a virtual back button
        // to revert to the MainActivity where they were logged in.
        Log.w(className, "Back button pressed");
    }

    private void setValid() {
        Valid = true;
    }

    private Boolean getValid() {
        return Valid;
    }

    class MyTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings) {
            String line = "";
            String username = "";
            if (!(strings[0] == "")) {
                username = strings[0];
            }
            String query = "SELECT id FROM users WHERE username='" + username + "';";
            String oldString = "http://aaronvigal.com/querydb.php?token=102809911022543&query=" + query;
            String urlString = "";
            for (int i = 0; i < oldString.length(); i++) {
                if (oldString.charAt(i) == ' ') {
                    urlString += "%20";
                } else if (oldString.charAt(i) == '\'') {
                    urlString += "%27";
                } else {
                    urlString += oldString.charAt(i);
                }
            }
            System.out.println(urlString);
            try {
                URL url = new URL(urlString);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                line = reader.readLine();
                reader.close();
            } catch (IOException e) {
                System.out.println("Failed: " + e);
            }
            return line + "";
        }

        protected void onPostExecute(String result) {
            Log.w("MySQL Request", result);
            if (!(result == null || result.equals("null"))) {
                setValid();
            }
        }
    }
}
