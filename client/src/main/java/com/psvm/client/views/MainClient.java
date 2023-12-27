package com.psvm.client.views;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class MainClient {
    private final JFrame jfrm;
    MainClient(){
        jfrm = new JFrame("hooYah");
        jfrm.setLayout(new BorderLayout());
        jfrm.setLocationRelativeTo(null);
        jfrm.getContentPane().setBackground(Color.decode("#FDFDFD"));
        jfrm.getRootPane().setBorder(new MatteBorder(1,1,1,1,Color.decode("#CDD5DE")));
        //favicon
        ImageIcon favicon = new ImageIcon("client/src/main/resources/icon/chat-app-logo-2.png");

        jfrm.setIconImage(favicon.getImage());
        // Give the frame an initial size.
        jfrm.setSize(1440, 820);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Terminate the program when the user closes
        // the application.


        //Chat Section
        ChatSection chatSection = new ChatSection();
        jfrm.add(chatSection,BorderLayout.CENTER);

        //FriendListBar
        FriendListBar friendListBar = new FriendListBar(); //Khi implement xong thì nhớ bỏ chatsection vào constructor của friendListBar
        jfrm.add(friendListBar,BorderLayout.WEST);


        //Set visible
        jfrm.setVisible(true);
    }

    public int getFrameWidth() {
        return jfrm.getWidth();
    }

    // Method to get the current frame height
    public int getFrameHeight() {
        return jfrm.getHeight();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainClient mainFrame = new MainClient();

                int width = mainFrame.getFrameWidth();
                int height = mainFrame.getFrameHeight();

                System.out.println("Current Frame Width: " + width);
                System.out.println("Current Frame Height: " + height);
            }
        });
    }
}
