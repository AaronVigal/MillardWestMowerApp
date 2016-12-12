package com.example.shipwreck1028.mower;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity {

    String className = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    /**
     * This is where we gotta do some work. First of all, we need this
     * huge ugly block of code to get all the text from the fields and set them
     * to variables. Our next step is to check to make sure none of them are
     * blank and fit all of our requirements. If all of that passes, we can
     * salt and hash their password and put them in the database.
     *
     * @param view
     */
    public void validateFields(View view) {
        Log.w(className, "Getting text from fields and setting variables.");
        EditText firstNameText = (EditText) findViewById(R.id.firstName);
        EditText lastNameText = (EditText) findViewById(R.id.lastName);
        EditText usernameText = (EditText) findViewById(R.id.username);
        EditText emailText = (EditText) findViewById(R.id.email);
        EditText phone1Text = (EditText) findViewById(R.id.phone1);
        EditText phone2Text = (EditText) findViewById(R.id.phone2);
        EditText passwordText = (EditText) findViewById(R.id.password);
        EditText confirmText = (EditText) findViewById(R.id.confirm);
        EditText pinText = (EditText) findViewById(R.id.pin);
        String username = usernameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String firstName = firstNameText.getText().toString();
        String email = emailText.getText().toString();
        String phone1 = phone1Text.getText().toString();
        String phone2 = phone2Text.getText().toString();
        String password = passwordText.getText().toString();
        String confirm = confirmText.getText().toString();
        String pin = pinText.getText().toString();

        boolean valid = true;
        String[] tooCommon = {"password", "12345678", "qwertyui", "23456789", "dragon", "baseball", "football", "letmein", "monkey", "69696969", "abc12345", "mustang", "michael", "shadow", "master", "jennifer", "11111111", "jordan", "superman", "harley", "fuckyou", "trustno1", "ranger", "buster", "thomas", "tigger", "robert", "soccer", "batman", "killer", "hockey", "george", "charlie", "andrew", "michelle", "sunshine", "jessica", "asshole", "6969"};

        Log.w(className, "Checking fields.");
        // Check to see if either of the name fields are empty.
        if (firstName.length() == 0 || lastName.length() == 0) {
            valid = false;
            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.blank_name);
        }

        // Check if their username field is empty.
        if (username.length() == 0) {
            valid = false;
            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.blank_username);
        }

        // Check if they entered/entered a valid email address.
        if (email.length() == 0) {
            valid = false;
            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.blank_email);
        } else if (!email.contains("@") || !email.split("@")[1].contains(".")) {
            valid = false;
            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.invalid_email);
        }

        // Check for their phone number and restrict the characters to 11
        if (phone1.length() == 0 || phone2.length() == 0) {
            valid = false;
            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.missing_phone);
        } else if (!(phone1.matches("[0-9]+")) || !(phone2.matches("[0-9]+"))) {
            valid = false;
            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.only_numbers);
        }

        // make sure their password fits all our requirements
        if (password.length() < 8) {
            valid = false;
            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.eight_chars);
        } else if (username == password || username.toLowerCase().contains(password.toLowerCase())) {
            valid = false;
            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.pass_is_username);
        } else if (!password.equals(confirm)) {
            valid = false;
            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.pass_no_match);
        } else if (Arrays.asList(tooCommon).contains(password)) {
            valid = false;
            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.too_common);
        }

        // Check for their new security pin which will be used for confirming actions
        if (!(pin.length() == 4)) {
            valid = false;
            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.blank_pin);
        } else if (!(pin.matches("[0-9]+"))) {
            valid = false;
            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.pin_only_num);
        }

        Log.w(className, "Good to go! Hashing and entering in database.");
        // If they have passed all of out tests, go on.
        if (valid) {
            MessageDigest md = null;
            SecureRandom random = new SecureRandom();
            String salt = nextSalt(random);
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

            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#246820"));
            t.setText(R.string.success);

            Log.w(":", "----------------------- ::");
            Log.w("Message", "Registered!");
            Log.w("Name", firstName + " " + lastName);
            Log.w("Username", username);
            Log.w("Email", email);
            Log.w("Phone", phone1);
            Log.w("Alt. Phone", phone2);
            Log.w("Password", password);
            Log.w("Salt", salt);
            Log.w("Hashed", hash);
            Log.w("Pin", pin);

            new GetQueryResponse().insertIntoDB(firstName, lastName, username, email, phone1, phone2, hash, salt, pin);

        } else {
            Log.w(className, "You messed something up really bad...");
        }
    }

    /**
     * Generate a salt that is 32 digits long to append to the end of the
     * password before hashing.s
     *
     * @param random
     * @return
     */
    public String nextSalt(SecureRandom random) {
        return new BigInteger(130, random).toString(32);
    }
}