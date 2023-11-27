package org.example;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private ChatServer chatServer;

    public ClientHandler(Socket clientSocket, ChatServer chatServer) {
        this.clientSocket = clientSocket;
        this.chatServer = chatServer;

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
            while (true) {
                Message message = (Message) objectInputStream.readObject();

                if (message.getType() == MessageType.TEXT) {
                    chatServer.broadcastMessage(message.toString(), this);
                } else if (message.getType() == MessageType.FILE) {
                    chatServer.broadcastFile(message.getFileData(), message.getFileName(), this);
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

    public void sendMessage(String message) {
        try {
            objectOutputStream.writeObject(new Message(MessageType.TEXT, message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(byte[] fileData, String fileName) {
        try {
            objectOutputStream.writeObject(new Message(MessageType.FILE, fileData, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
