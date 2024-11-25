package com.chat.model;

import java.time.LocalDateTime;

public class Message {
    private final String meassageId; //unique id for message
    private final String senderId;
    private final String receiverId;
    private final String content; // just consider receive and send text message
    private final LocalDateTime timestamp; //message sending time
    private MessageStatus status; // message status: sending/sent/received/read

    //enumerated types indicate message status
    public enum MessageStatus{
        SENDING,
        SENT,
        RECEIVED,
        READ
    }

    //constructor
    public Message(String meassageId, String senderId, String receiverId, String content, LocalDateTime timestamp, MessageStatus status) {
        this.meassageId = meassageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
        this.status = status;
    }

    //getter and setter
    //meassageId
    public String getMeassageId() {
        return meassageId;
    }

    //senderId
    public String getSenderId() {
        return senderId;
    }

    //receiverId
    public String getReceiverId() {
        return receiverId;
    }

    //content
    public String getContent() {
        return content;
    }

    //timestamp
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    //status
    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    //Override the toString() method to make it easier to print the message information. use for debugging and printing logs
    @Override
    public String toString(){
        return "Message{" +
                "meassageId = '" + meassageId + '\'' +
                ", senderId = '" + senderId + '\'' +
                ", receiverId = '" + receiverId + '\'' +
                ", content = '" + content + '\'' +
                ", timestamp = '" + timestamp + '\'' +
                ", status = '" + status +
                '}';
    }
}
