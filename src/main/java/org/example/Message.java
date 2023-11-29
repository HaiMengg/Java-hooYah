package org.example;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private MessageType type;
    private String text;
    private byte[] fileData;
    private String fileName;

    private String roomID;


    public Message(MessageType type, String text, String roomID) {
        this.type = type;
        this.text = text;
        this.roomID = roomID;
    }

    public Message(MessageType type, byte[] fileData, String fileName) {
        this.type = type;
        this.fileData = fileData;
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        if (type == MessageType.TEXT) {
            return text;
        } else if (type == MessageType.FILE) {
            return "File received: " + fileName + " Content: " + new String(fileData);
        }
        return super.toString();
    }

    public MessageType getType() {
        return type;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public String getRoomID() { return roomID; }
}

