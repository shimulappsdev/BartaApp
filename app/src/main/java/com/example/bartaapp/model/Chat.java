package com.example.bartaapp.model;

public class Chat {

    String senderId, receiverId, message, chatKey, imgUrl;
    long msgTime;
    int feelings = -1;

    public Chat(String senderId, String receiverId, String message, String chatKey, long msgTime, int feelings) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.chatKey = chatKey;
        this.msgTime = msgTime;
        this.feelings = feelings;
    }

    public Chat() {
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatKey() {
        return chatKey;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }

    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    public int getFeelings() {
        return feelings;
    }

    public void setFeelings(int feelings) {
        this.feelings = feelings;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
