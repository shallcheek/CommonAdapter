package com.chaek.android.chat;

/**
 * Auth: Chaek
 * Date: 2017/12/13
 */

public class ChatData {
    int messageType;
    String message;

    public ChatData(int messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
