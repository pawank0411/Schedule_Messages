package com.example.schedule_message.model;

public class MessageData {
    private String phoneNumber;

    public MessageData(String phoneNumber, String message, String dateTime) {
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.dateTime = dateTime;
    }

    private String message;
    private String dateTime;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
