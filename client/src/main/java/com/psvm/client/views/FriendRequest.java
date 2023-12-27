package com.psvm.client.views;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FriendRequest extends JPanel {
    private boolean addStatus = false;
    private String avatar;
    private String username;
    private String name;

    FriendRequest(String avatar, String username, String name) {
        this.avatar = avatar;
        this.username = username;
        this.name = name;
        this.setPreferredSize(new Dimension(200, 50));
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setBackground(Color.WHITE);
        initialize();
    }

    String getUsername() {
        return username;
    }

    String getUserAppName() {
        return name;
    }

    void initialize() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Component 1 (avatar, spanning 2 rows)
        ImageIcon avatarIcon = createCircularAvatar("client/src/main/resources/icon/avatar_sample.jpg", 40, 40);
        JLabel avatarLabel = new JLabel(avatarIcon);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 10); // Add a 10 pixel gap to the right
        this.add(avatarLabel, gbc);

        // Component 2 (button in the second column)
        JLabel friendName = new JLabel(name);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1; // Reset grid height
        gbc.insets = new Insets(0, 0, 0, 50); // Add a 50 pixel gap to the right
        this.add(friendName, gbc);

        // Component 3 (button in the third column)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1; // Reset grid height
        gbc.insets = new Insets(0, 0, 0, 0); // Reset insets
        this.add(buttonPanel, gbc);

        JButton accept = createAcceptButton(buttonPanel);
        accept.setBackground(Color.WHITE);
        accept.setFocusPainted(false);

        JButton rejectButton = createRejectButton(buttonPanel);
        rejectButton.setBackground(Color.WHITE);
        rejectButton.setFocusPainted(false);

        buttonPanel.add(accept);
        buttonPanel.add(rejectButton);
    }

    private JButton createAcceptButton(JPanel buttonPanel) {
        String acceptFriendIconPath = "client/src/main/resources/icon/acceptFriendRequest.png";
        String acceptDone = "client/src/main/resources/icon/done.png";

        ImageIcon addFriendIcon = new ImageIcon(acceptFriendIconPath);
        ImageIcon doneIcon = new ImageIcon(acceptDone);

        Image scaledAddFriendImage = addFriendIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        Image scaledDoneImage = doneIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

        JButton toggleButton = new JButton(new ImageIcon(scaledAddFriendImage));
        toggleButton.setForeground(Color.GREEN);
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleButton.setIcon(new ImageIcon(scaledDoneImage));
                buttonPanel.remove(1);
                buttonPanel.revalidate();
                buttonPanel.repaint();
                toggleButton.setEnabled(false);
            }
        });
        return toggleButton;
    }

    private JButton createRejectButton(JPanel buttonPanel) {
        String rejectFriendIconPath = "client/src/main/resources/icon/rejectFriendRequest.png";
        String rejectDone = "client/src/main/resources/icon/cancelled.png";

        ImageIcon addFriendIcon = new ImageIcon(rejectFriendIconPath);
        ImageIcon doneIcon = new ImageIcon(rejectDone);

        Image scaledAddFriendImage = addFriendIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        Image scaledDoneImage = doneIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

        JButton toggleButton = new JButton(new ImageIcon(scaledAddFriendImage));
        toggleButton.setForeground(Color.RED);
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleButton.setIcon(new ImageIcon(scaledDoneImage));
                buttonPanel.remove(0);
                buttonPanel.revalidate();
                buttonPanel.repaint();
                toggleButton.setEnabled(false);
            }
        });
        return toggleButton;
    }

    private static ImageIcon createCircularAvatar(String imagePath, int width, int height) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImage.createGraphics();

            Shape clip = new Ellipse2D.Float(0, 0, width, height);
            g2d.setClip(clip);
            g2d.drawImage(originalImage, 0, 0, width, height, null);

            g2d.dispose();

            return new ImageIcon(resizedImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
