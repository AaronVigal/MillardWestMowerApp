package com.example.shipwreck1028.mower;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

class GetQueryResponse extends Thread {

    private static final SimpleDateFormat month = new SimpleDateFormat("MM");
    private static final SimpleDateFormat day = new SimpleDateFormat("dd");
    private static final SimpleDateFormat hour = new SimpleDateFormat("HH");

    public static String getResponseFromUrl(String url) {

        try {

            HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
            HttpGet httpget = new HttpGet(url); // Set the action you want to do
            HttpResponse response = httpclient.execute(httpget); // Executeit
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent(); // Create an InputStream with the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String resString = sb.toString();

            is.close();
            return resString;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void insertIntoDB(String firstName, String lastName, String username, String email, String phone1, String phone2, String password, String salt, String pin) {

        try {
            Date date = new Date();
            String APIKey = "1028" + (Integer.parseInt((hour.format(date))) - 1) + "99" + ((month.format(date))) + "02" + (31 - (Integer.parseInt(((day.format(date)))))) + "43";
            String oldString = "http://aaronvigal.com/querydb.php?token=" + APIKey + "&query=INSERT INTO users (firstName, lastName, username, email, phone1, phone2, password, salt, pin) VALUES ('" + firstName + "', '" + lastName + "', '" + username + "', '" + email + "', '" + phone1 + "', '" + phone2 + "', '" + password + "', '" + salt + "', " + pin + ");";
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
            Log.w("Execution", urlString);
            HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
            HttpGet httpget = new HttpGet(urlString); // Set the action you want to do
            HttpResponse response = httpclient.execute(httpget); // Executeit
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent(); // Create an InputStream with the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();

            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
