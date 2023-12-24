package com.psvm.client.views;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FriendListChoosingCategory extends JPanel {
    JScrollPane scrollFriend;
    JButton selectedButton;
    JButton allFriendsButton;
    JButton onlineFriendsButton;
    JButton groupButton;
    JButton blockedButton;

    FriendListChoosingCategory(JScrollPane scrollFriend) {
        this.scrollFriend = scrollFriend;
        this.setOpaque(false);
        this.setLayout(new GridLayout(2, 2));

        // Button 1: Tất cả bạn bè
        allFriendsButton = createButton("Tất cả bạn bè");

        this.add(allFriendsButton);

        // Button 2: Bạn bè online
         onlineFriendsButton = createButton("Bạn bè online");
        this.add(onlineFriendsButton);

        // Button 3: Nhóm
        groupButton = createButton("Nhóm");
        this.add(groupButton);

        // Button 4: Bị chặn
        blockedButton = createButton("Bị chặn");
        this.add(blockedButton);

        allFriendsButton.doClick();
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != selectedButton) {
                    button.setBackground(Color.lightGray);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.getModel().isPressed() && button != selectedButton) {
                    button.setBackground(Color.WHITE);
                }
            }
        });

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedButton != null) {
                    selectedButton.setBackground(Color.WHITE);
                }
                button.setBackground(Color.CYAN);
                selectedButton = button;

                JViewport viewport = scrollFriend.getViewport();
                // Remove all components from the viewport's view
                viewport.removeAll();
                // Revalidate and repaint to reflect the changes
                scrollFriend.revalidate();
                scrollFriend.repaint();

                if (selectedButton == allFriendsButton){
                    // Friend list
                    ListFriendOfUser listFriendOfUser = new ListFriendOfUser();
                    scrollFriend.setViewportView(listFriendOfUser);

                }

                if (selectedButton == onlineFriendsButton){
                    //Online friend
                }
                else if (selectedButton == groupButton){
                    //group
                    ListGroupOfUser listGroupOfUser = new ListGroupOfUser();
                    scrollFriend.setViewportView(listGroupOfUser);
                }
                else if (selectedButton == blockedButton){
                    //blocked


                }
            }
        });

        return button;
    }
}
