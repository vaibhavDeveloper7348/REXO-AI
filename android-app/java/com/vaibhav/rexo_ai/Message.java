package com.vaibhav.quantumcore3;

public class Message {
    private String senderName;
    private String messageText;
    private String time;
    private boolean isAI;

    public Message(String senderName, String messageText, String time, boolean isAI) {
        this.senderName = senderName;
        this.messageText = messageText;
        this.time = time;
        this.isAI = isAI;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getTime() {
        return time;
    }

    public boolean isAI() {
        return isAI;
    }
}