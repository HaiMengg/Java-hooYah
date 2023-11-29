package org.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private ChatServer chatServer;
    private String roomID;

    private Integer clientID = 0;

    private static Integer clientCount = 0;

    public static List<String> roomList = new ArrayList<>() {{
        add("roomChat1"); add("roomChat2"); add("roomChat3");
    }};
    public ClientHandler(Socket clientSocket, ChatServer chatServer) {
        this.clientSocket = clientSocket;
        this.chatServer = chatServer;
        this.clientID = clientCount ++;

        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            objectOutputStream.writeObject(new RoomList(roomList));
            while (true) {
                Message message = (Message) objectInputStream.readObject();

                if (message.getType() == MessageType.TEXT) {
                    chatServer.broadcastMessage(message.toString(), roomID, this);
                } else if (message.getType() == MessageType.FILE) {
                    chatServer.broadcastFile(message.getFileData(), message.getFileName(), this);
                } else if (message.getType() == MessageType.ROOM_ID) {
                    this.roomID = message.getRoomID();
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            chatServer.removeClient(this);
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendMessage(String message, String roomID) { // Broacast
        try {
            if (this.roomID.equals(roomID))
                objectOutputStream.writeObject(new Message(MessageType.TEXT, "Client" + clientID + " Send: " + message, roomID));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(byte[] fileData, String fileName) { // Broacast
        try {
            objectOutputStream.writeObject(new Message(MessageType.FILE, fileData, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
