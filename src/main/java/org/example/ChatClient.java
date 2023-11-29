package org.example;

import java.io.*;
import java.net.*;
import java.util.List;
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

            Scanner scanner = new Scanner(System.in);

            String roomID = ""; //Specify room ID

            System.out. println("Welcome new Client, Select Room To Chat.");
            System.out. println("Room List: ");
            try {
                RoomList roomList = (RoomList) objectInputStream.readObject();
                for (int i = 0; i < roomList.getList().size(); i++) {
                    System.out.println("\t" + (i+1) + ". " + roomList.getList().get(i));
                }
                System.out. print("Choose: "); int userChoose = scanner.nextInt();
                roomID = roomList.getList().get(userChoose - 1);

                sendRoomID(roomID); // Send server chosen RoomID

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            new Thread(this::handleServerMessages).start();

            scanner.nextLine();
            while (true) {
                System.out.println("Enter your message or file path (type 'exit' to quit): ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                if (input.startsWith("@file:")) {
                    String filePath = input.substring(6);
                    sendFile(filePath);
                } else {
                    sendMessage(input, roomID);
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message, String roomID) { // Client
        try {
            objectOutputStream.writeObject(new Message(MessageType.TEXT, message, roomID));
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


    private void sendRoomID(String roomID) {
        try {
            objectOutputStream.writeObject(new Message(MessageType.ROOM_ID, "", roomID));
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
