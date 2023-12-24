package com.psvm.client.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;

public class ListGroupOfUser extends JPanel {
    private UserEachGroup currentSelectedFriend;
    ListGroupOfUser(){
        this.setBackground(Color.white);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //friendList.add(friendSearchAndAdd);
        for (int i = 1; i <= 4; i++) {
            UserEachGroup userEachFriend = new UserEachGroup("af","username1","Nhóm nè","nhóm", LocalDateTime.of(2005,12,4, 1,40),"Offline","Offline");
            addHoverEffect(userEachFriend);
            this.add(userEachFriend);
        }
        this.add(Box.createVerticalGlue());
    }
    private void addHoverEffect(UserEachGroup group) {
        group.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (group != currentSelectedFriend) {
                    group.setBackground(Color.LIGHT_GRAY);
                }
            }
            public void mouseExited(MouseEvent evt) {
                if (group != currentSelectedFriend) {
                    group.setBackground(Color.WHITE);
                }

            }
            public void mouseClicked(MouseEvent evt) {
                if (currentSelectedFriend != null){
                    currentSelectedFriend.setBackground(Color.WHITE);
                }
                // do sth

                group.setBackground(Color.decode("#ADD8E6"));
                currentSelectedFriend = group;
            }
        });
        // Set maximum size to allow varying sizes
        group.setMaximumSize(new Dimension(Integer.MAX_VALUE, group.getPreferredSize().height));
        group.revalidate();
        group.repaint();
    }
}
