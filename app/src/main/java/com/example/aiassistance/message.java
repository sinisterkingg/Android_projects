package com.example.aiassistance;

public class message {

    public static String SENT_BY_ME = "ME";
    public static String SENT_BY_AI = "AI";

    String message;
    String send_by;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSend_by() {
        return send_by;
    }

    public void setSend_by(String send_by) {
        this.send_by = send_by;
    }

    public message(String message, String send_by) {
        this.message = message;
        this.send_by = send_by;
    }
}
