package com.automation.utils;

public class TestEmail {

    // Static method to get the verification code as a String
    public static String getVerificationCode() {
        String email = "testautosujet10@gmail.com";
        String appPassword = "jtce bsku nmho hjuh"; // App Password with 2FA

        // Assuming EmailUtils.getVerificationCode returns a String
        return EmailUtils.getVerificationCode(email, appPassword);
    }

    // Optional: main method for testing
    public static void main(String[] args) {
        System.out.println(getVerificationCode());
    }
}