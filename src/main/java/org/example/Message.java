package org.example;

import java.io.Serializable;
import java.util.Arrays;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private MessageType type;
    private String text;
    private byte[] fileData;
    private String fileName;

    public Message(MessageType type, String text) {
        this.type = type;
        this.text = text;
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
}

