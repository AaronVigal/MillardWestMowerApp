package com.example.shipwreck1028.mower;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterMowerActivity extends AppCompatActivity {

    String className = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mower);
    }

    /**
     * Launch the MyCameraActivity Intent to take a photo of the mower
     * @param view You are required to pass this through
     */
    public void takePhoto(View view){
        Intent sendStuff = new Intent(this, MyCameraActivity.class);
        startActivity(sendStuff);
    }

    /**
     * First, we need to get all of the text from the fields and set them
     * to variables. Next we need to check to make sure none of them are
     * blank and fit all of our requirements. If all of that passes, we can
     * submit their mower to the database.
     * @param view You are required to pass this through
     */
    public void onSubmit(View view){
        Log.w(className,"Getting text from fields and setting variables.");
        EditText modelText = (EditText)findViewById(R.id.model);
        EditText makeText = (EditText)findViewById(R.id.make);
        EditText dateText = (EditText)findViewById(R.id.date);
        EditText addressText = (EditText)findViewById(R.id.address);
        EditText conditionText = (EditText)findViewById(R.id.condition);
        String model = modelText.getText().toString();
        String make = makeText.getText().toString();
        String date = dateText.getText().toString();
        String address = addressText.getText().toString();
        String condition = conditionText.getText().toString();

        boolean valid = true;

        // Check for a blank make field.
        if (make.length() == 0) {
            valid = false;
            TextView t = (TextView)findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.blank_make);
        }

        // Make sure they entered their model
        if (model.length() == 0) {
            valid = false;
            TextView t = (TextView)findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.blank_model);
        }

        // Ensure they entered a date
        if (date.length() == 0) {
            valid = false;
            TextView t = (TextView)findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.blank_date);
        }

        // Can you see the pattern here?
        if (address.length() == 0) {
            valid = false;
            TextView t = (TextView)findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.blank_address);
        }

        // Still doing the same thing
        if (condition.length() == 0) {
            valid = false;
            TextView t = (TextView)findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#FF0000"));
            t.setText(R.string.blank_condition);
        }

        // If everything is valid, keep going
        if (valid) {
            Log.w(className, "Everything looks good. Putting you into the database");
            TextView t = (TextView) findViewById(R.id.errorBox);
            t.setTextColor(Color.parseColor("#246820"));
            t.setText(R.string.submit_success);
        } else {
            Log.w(className,"You messed something up really bad...");
        }

    }
}
