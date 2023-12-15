package com.psvm.client.views.components.friend;

import com.psvm.client.controllers.FriendListRequest;
import com.psvm.client.controllers.FriendMessageListRequest;
import com.psvm.shared.socket.SocketResponse;

import javax.swing.*;
import javax.swing.plaf.basic.DefaultMenuLayout;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ListFriendOfUser extends JPanel {
    // Global collections of messages of the 3 types: Unseen while online, Unseen while offline, Seen
    private Vector<Map<String, Object>> totalUnseenOnlineMessages = new Vector<>();
    private Vector<Map<String, Object>> totalUnseenOfflineMessages = new Vector<>();
    private Vector<Map<String, Object>> totalSeenMessages = new Vector<>();

    // Separate JPanels to display these messages
    private int unseenOnlineMessagesIndex = 0;
    private int unseenOfflineMessagesIndex = 0;
    private int seenMessagesIndex = 0;

    private JPanel currentSelectedFriend;
    ListFriendOfUser(){
        // Initialize this GUI component
        setBackground(new Color(255,255,255,255));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
    private void addHoverEffect(UserEachFriend friend) {
        friend.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (friend != currentSelectedFriend) {
                    friend.setBackground(Color.LIGHT_GRAY);
                }
            }
            public void mouseExited(MouseEvent evt) {
                if (friend != currentSelectedFriend) {
                    friend.setBackground(Color.WHITE);
                }

            }
            public void mouseClicked(MouseEvent evt) {
                if (currentSelectedFriend != null){
                    currentSelectedFriend.setBackground(Color.WHITE);
                }
                // do sth

                friend.setBackground(Color.decode("#ADD8E6"));
                currentSelectedFriend = friend;
            }
        });
    }

    public void setData(Vector<Map<String, Object>> friends) {
        JPanel thisPanel = this;
        SwingUtilities.invokeLater(() -> {
            Vector<Map<String, Object>> unseenOnlineMessages = (Vector<Map<String, Object>>) friends.get(0).get("data");
            // If unseenOnlineMessages is empty after this line then the data is the same
            unseenOnlineMessages.removeAll(totalUnseenOnlineMessages);
            totalUnseenOnlineMessages.addAll(unseenOnlineMessages);
            for (Map<String, Object> friend: unseenOnlineMessages)
            {
                String convoName = (Integer.parseInt(friend.get("MemberCount").toString()) > 2) ? friend.get("ConversationName").toString() : friend.get("MemberId").toString();
                UserEachFriend userEachFriend = new UserEachFriend("af", convoName, friend.get("Content").toString(), ((Timestamp) friend.get("Datetime")).toLocalDateTime(),"Online");
                thisPanel.add(userEachFriend, unseenOnlineMessagesIndex);
                // Add vertical spacing between components
                thisPanel.add(Box.createVerticalStrut(10), unseenOnlineMessagesIndex + 1);
                unseenOnlineMessagesIndex += 2;
                unseenOfflineMessagesIndex += 2;
                seenMessagesIndex += 2;
                addHoverEffect(userEachFriend);
            }
            for (Map<String, Object> friend: (Vector<Map<String, Object>>) friends.get(1).get("data")) {
                String convoName = (Integer.parseInt(friend.get("MemberCount").toString()) > 2) ? friend.get("ConversationName").toString() : friend.get("MemberId").toString();
                UserEachFriend userEachFriend = new UserEachFriend("af", convoName, friend.get("Content").toString(), ((Timestamp) friend.get("Datetime")).toLocalDateTime(),"Offline");
                thisPanel.add(userEachFriend, unseenOfflineMessagesIndex);
                // Add vertical spacing between components
                thisPanel.add(Box.createVerticalStrut(10), unseenOfflineMessagesIndex + 1);
                unseenOfflineMessagesIndex += 2;
                seenMessagesIndex += 2;
                addHoverEffect(userEachFriend);
            }
            Vector<Map<String, Object>> seenMessages = (Vector<Map<String, Object>>) friends.get(0).get("data");
            // If seenMessages is empty after this line then the data is the same
            seenMessages.removeAll(totalSeenMessages);
            totalSeenMessages.addAll(seenMessages);
            for (Map<String, Object> friend: seenMessages) {
                String convoName = (Integer.parseInt(friend.get("MemberCount").toString()) > 2) ? friend.get("ConversationName").toString() : friend.get("MemberId").toString();
                UserEachFriend userEachFriend = new UserEachFriend("af", convoName, friend.get("Content").toString(), ((Timestamp) friend.get("Datetime")).toLocalDateTime(),"");
                thisPanel.add(userEachFriend, seenMessagesIndex);
                // Add vertical spacing between components
                thisPanel.add(Box.createVerticalStrut(10), seenMessagesIndex + 1);
                seenMessagesIndex += 2;
                addHoverEffect(userEachFriend);
            }
            thisPanel.add(Box.createVerticalGlue());
            thisPanel.revalidate();
        });
    }
}
