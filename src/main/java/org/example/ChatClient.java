package org.example;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public static void main(String[] args) {
        new ChatClient().startClient();
    }

    public void startClient() {
        try {
            socket = new Socket("localhost", 5555);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            new Thread(this::handleServerMessages).start();

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Enter your message or file path (type 'exit' to quit): ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                if (input.startsWith("@file:")) {
                    String filePath = input.substring(6);
                    sendFile(filePath);
                } else {
                    sendMessage(input);
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        try {
            objectOutputStream.writeObject(new Message(MessageType.TEXT, message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFile(String filePath) {
        try {
            File file = new File(filePath);
            byte[] fileData = new byte[(int) file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(fileData);
            fileInputStream.close();

            objectOutputStream.writeObject(new Message(MessageType.FILE, fileData, file.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleServerMessages() {
        try {
            while (true) {
                Message message = (Message) objectInputStream.readObject();
                System.out.println(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
