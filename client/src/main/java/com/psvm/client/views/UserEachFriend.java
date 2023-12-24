package com.psvm.client.views;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class UserEachFriend extends JPanel {
    String avatar;
    //Mới thêm cái này vì nó cần thiết
    String username;
    String name;
     String lastChat;
     LocalDateTime lastTime;
     String userStatus;
    //Mới thêm cái này vì nó cần thiết
     String lastChatStatus;
    UserEachFriend(String avatar, String username, String name, String lastChat, LocalDateTime lastTime, String userStatus, String lastChatStatus){
        this.avatar = avatar;
        this.username = username;
        this.name = name;
        this.lastChat = lastChat;
        this.lastTime = lastTime;
        this.userStatus = userStatus;
        this.lastChatStatus = lastChatStatus;
        this.setPreferredSize(new Dimension(super.getWidth(),70));
        this.setBorder(new EmptyBorder(0,0,0,0));
        this.setBackground(Color.WHITE);
        initialize();
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)){
                    showPopupMenu(e.getX(),e.getY());
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
    //thêm 2 cái hàm này để lọc ra
    String getUsername(){
        return username;
    }
    String getUserAppName(){
        return name;
    }
    void initialize(){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Component 1 (avatar, spanning 2 rows)//nhớ thay avatar dưới này
        ImageIcon avatarIcon = createCircularAvatar("client/src/main/resources/icon/avatar_sample.jpg", 40, 40,userStatus);
        JLabel avatarLabel = new JLabel(avatarIcon);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 10); // Add a 10-pixel gap to the right
        this.add(avatarLabel, gbc);

        // Component 2 (button in the second column)
        JLabel friendName = new JLabel(name);
        friendName.setPreferredSize(new Dimension(135, friendName.getPreferredSize().height));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch horizontally
        this.add(friendName, gbc);

        // Component 3 (button in the third column)
        JLabel friendLastTime = new JLabel(formatLocalDateTime(lastTime));

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0); // Reset insets
        this.add(friendLastTime, gbc);

        // Component 4 (button in the second row)
        JLabel lastMessage= new JLabel(lastChat);
        gbc.gridx = 1;
        gbc.gridy = 1;
        this.add(lastMessage, gbc);

        // Component 5 (button in the third row)
        JLabel statusMessage = createUserStatusDot(lastChatStatus);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0,90,0,0);
        this.add(statusMessage, gbc);
    }
    private void showPopupMenu(int x, int y){
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(Color.WHITE);

        JMenuItem addGroupItem = new JMenuItem("👋 Add Group");
        addGroupItem.setFont(new Font(null,Font.PLAIN,16));
        addGroupItem.setForeground(Color.blue);

        JMenuItem blockItem = new JMenuItem("🚫 Block");
        blockItem.setForeground(Color.red);
        blockItem.setFont(new Font(null,Font.PLAIN,16));

        JMenuItem unfriendItem = new JMenuItem("❌ Unfriend");
        unfriendItem.setFont(new Font(null,Font.PLAIN,16));
        unfriendItem.setForeground(Color.blue);

        JMenuItem spamItem = new JMenuItem("🤐 Report Spam");
        spamItem.setFont(new Font(null,Font.PLAIN,16));
        spamItem.setForeground(Color.red);

        popupMenu.add(addGroupItem);
        popupMenu.add(blockItem);
        popupMenu.add(unfriendItem);
        popupMenu.add(spamItem);

        addGroupItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "You have created a group with this user!");
            }
        });

        blockItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to block this user?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    // giữ hay bỏ gì tuỳ cái dialog này tuỳ ko quan trọng
                    JOptionPane.showMessageDialog(null, "Blocking...");
                }
            }
        });

        unfriendItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to unfriend this user?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    // giữ hay bỏ gì tuỳ cái dialog này tuỳ ko quan trọng
                    JOptionPane.showMessageDialog(null, "Unfriending...");
                }
            }
        });
        spamItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to report spam this user?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    // giữ hay bỏ gì tuỳ cái dialog này tuỳ ko quan trọng
                    JOptionPane.showMessageDialog(null, "Reporting Spam...");
                }
            }
        });
        popupMenu.show(this, x, y);
    }
     static ImageIcon createCircularAvatar(String imagePath, int width, int height, String userStatus) {
        try {
            //https://stackoverflow.com/questions/14731799/bufferedimage-into-circle-shape
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImage.createGraphics();

            Shape clip = new Ellipse2D.Float(0, 0, width, height);
            g2d.setClip(clip);
            g2d.drawImage(originalImage, 0, 0, width, height, null);

            if ("Online".equals(userStatus)) {
                g2d.setColor(Color.GREEN);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawOval(0, 0, width - 1, height - 1);
            }

            g2d.dispose();

            return new ImageIcon(resizedImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    static String formatLocalDateTime(LocalDateTime dateTime) {
//        LocalDateTime now = LocalDateTime.now();
//
//        if (dateTime.toLocalDate().isEqual(now.toLocalDate())) {
//            // Same day
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//            return dateTime.format(formatter);
//        } else {
//            // Different day
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//            return dateTime.format(formatter);
//        }
        // phần bên trên khiến cho cách dòng ko đều, chưa fix đc nên xài bên dưới
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        return dateTime.format(formatter);
    }


    static JLabel createUserStatusDot(String userStatus) {
        JLabel statusLabel = new JLabel();
        statusLabel.setPreferredSize(new Dimension(20, 20));

        ImageIcon icon = null;

        if ("Online".equals(userStatus)) {
        icon = new ImageIcon("client/src/main/resources/icon/chatWhenOnline.png");
    } else if ("Offline".equals(userStatus)) {
        icon = new ImageIcon("client/src/main/resources/icon/chatWhenOffline.png");
    }
        if (icon != null) {
        Image scaledImage = icon.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        statusLabel.setIcon(scaledIcon);
    } else {
        statusLabel.setVisible(false);
    }
        return statusLabel;
}

//thêm 2 cái hàm này để lọc ra
}
