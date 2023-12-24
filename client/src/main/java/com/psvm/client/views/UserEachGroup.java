package com.psvm.client.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDateTime;

public class UserEachGroup extends UserEachFriend {
    UserEachGroup(String avatar, String username, String name, String lastChat, LocalDateTime lastTime, String userStatus, String lastChatStatus){
        super(avatar,username,name,lastChat,lastTime,userStatus,lastChatStatus);
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)){
                    showPopupMenuForGroup(e.getX(),e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
    private void showPopupMenuForGroup(int x, int y){

        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(Color.WHITE);

        JMenuItem leaveGroup = new JMenuItem("🚪 Leave group");
        leaveGroup.setFont(new Font(null,Font.PLAIN,16));
        leaveGroup.setForeground(Color.RED);

        popupMenu.add(leaveGroup);

        leaveGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to leave this group?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    // giữ hay bỏ gì tuỳ cái dialog này tuỳ ko quan trọng
                    JOptionPane.showMessageDialog(null, "Leaving group...");
                }
            }
        });

        popupMenu.show(this, x, y);
    }



}
