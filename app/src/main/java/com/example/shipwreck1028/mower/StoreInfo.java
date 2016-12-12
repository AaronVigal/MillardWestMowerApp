package com.example.shipwreck1028.mower;

import java.text.SimpleDateFormat;
import java.util.Date;

class StoreInfo {

    Date date = new Date();
    private static final SimpleDateFormat month = new SimpleDateFormat("MM");
    private static final SimpleDateFormat day = new SimpleDateFormat("dd");
    private static final SimpleDateFormat hour = new SimpleDateFormat("HH");
    private String id = "";
    private String username = "";
    private String password = "";
    private String salt = "";
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String phone1 = "";
    private String phone2 = "";
    private String pin = "";

    public void saveInfo(String uname) {

        // Queries
        String idquery = "SELECT id FROM users WHERE username='" + username + "';";
        String unamequery = "SELECT username FROM users WHERE username='" + username + "';";
        String passquery = "SELECT password FROM users WHERE username='" + username + "';";
        String saltquery = "SELECT salt FROM users WHERE username='" + username + "';";
        String fnamequery = "SELECT firstName FROM users WHERE username='" + username + "';";
        String lnamequery = "SELECT lastName FROM users WHERE username='" + username + "';";
        String emailquery = "SELECT email FROM users WHERE username='" + username + "';";
        String p1query = "SELECT phone1 FROM users WHERE username='" + username + "';";
        String p2query = "SELECT phone2 FROM users WHERE username='" + username + "';";
        String pinquery = "SELECT pin FROM users WHERE username='" + username + "';";

        this.username = uname;
        this.id = getInfo(idquery);
        this.password = getInfo(passquery);
        this.salt = getInfo(saltquery);
        this.firstName = getInfo(fnamequery);
        this.lastName = getInfo(lnamequery);
        this.email = getInfo(emailquery);
        this.phone1 = getInfo(p1query);
        this.phone2 = getInfo(p2query);
        this.pin = getInfo(pinquery);

    }

    public String getStoredInfo(String field) {
        switch (field.toLowerCase()) {
            case "id":
                return this.id;
            case "username":
                return this.username;
            case "password":
                return this.password;
            case "salt":
                return this.salt;
            case "firstname":
                return this.firstName;
            case "lastname":
                return this.lastName;
            case "email":
                return this.email;
            case "phone1":
                return this.phone1;
            case "phone2":
                return this.phone2;
            case "pin":
                return this.pin;
        }
        return "Not a valid field";
    }

    private String getInfo(String query) {
        String APIKey = "1028" + (Integer.parseInt((hour.format(date))) - 1) + "99" + ((month.format(date))) + "02" + (31 - (Integer.parseInt(((day.format(date)))))) + "43";
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
        String result = new GetQueryResponse().getResponseFromUrl(urlString);
        return result;
    }

}
