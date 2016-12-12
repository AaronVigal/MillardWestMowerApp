package com.example.shipwreck1028.mower;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    String className = this.getClass().getSimpleName();
    Boolean Valid = false;
    private static final SimpleDateFormat month = new SimpleDateFormat("MM");
    private static final SimpleDateFormat day = new SimpleDateFormat("dd");
    private static final SimpleDateFormat minute = new SimpleDateFormat("mm");
    private static final SimpleDateFormat hour = new SimpleDateFormat("HH");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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

        boolean hasEnteredUandP = true;
        // Check for the following:
        // 1. Empty Username Field
        // 2. Empty Password Field
        if (!(username.length() > 0)) {
            hasEnteredUandP = false;
            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.no_username_error);
        } else if (!(password.length() > 0)) {
            hasEnteredUandP = false;
            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.no_password_error);
        }


        Date date = new Date();
        String APIKey = "1028" + (Integer.parseInt((hour.format(date))) - 1) + "99" + ((month.format(date))) + "02" + (31 - (Integer.parseInt(((day.format(date)))))) + "43";
        String query = "SELECT salt FROM users WHERE username='" + username + "';";
        String oldString = "http://aaronvigal.com/querydb.php?token=" + APIKey + "&query=" + query;
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

        Log.w(className, urlString);
        String salt = new GetQueryResponse().getResponseFromUrl(urlString);
        Log.w(className, salt);

        String query2 = "SELECT password FROM users WHERE username='" + username + "' AND salt='" + salt + "';";
        String oldString2 = "http://aaronvigal.com/querydb.php?token=" + APIKey + "&query=" + query2;
        String urlString2 = "";
        for (int i = 0; i < oldString2.length(); i++) {
            if (oldString2.charAt(i) == ' ') {
                urlString2 += "%20";
            } else if (oldString2.charAt(i) == '\'') {
                urlString2 += "%27";
            } else {
                urlString2 += oldString2.charAt(i);
            }
        }
        Log.w(className, urlString2);
        String passwordFromDB = new GetQueryResponse().getResponseFromUrl(urlString2);
        Log.w(className, passwordFromDB);


        MessageDigest md = null;
        String newPass = password + salt;

        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            Log.w(className, "Invalid hash type");
        }

        try {
            md.update(newPass.getBytes("UTF-8"));
        } catch (Exception e) {
            Log.w(className, "Invalid encoding");
        }

        byte[] digest = md.digest();
        String hash = String.format("%064x", new java.math.BigInteger(1, digest));

        Log.w(className, hash);
        if (!(hash.equals(passwordFromDB))) {
            setValid(true);
        } else {
            setValid(false);
        }

        // If Username & Password have passed all other tests,
        // create a new intent and move them to the MainActivity.
        if (hasEnteredUandP) {
            if (getValid()) {
                TextView t = (TextView) findViewById(R.id.errorBox);
                t.setTextColor(Color.parseColor("#FF0000"));
                t.setText(R.string.bad_creds_error);
            } else {
                if (salt.equals("")) {
                    TextView t = (TextView) findViewById(R.id.errorBox);
                    t.setTextColor(Color.parseColor("#FF0000"));
                    t.setText(R.string.bad_creds_error);
                }
                new StoreInfo().saveInfo(username);
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

    private void setValid(boolean set) {
        Valid = set;
    }

    private Boolean getValid() {
        return Valid;
    }
}