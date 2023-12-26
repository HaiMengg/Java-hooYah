package com.psvm.client.views.components.friend;

import com.psvm.client.settings.LocalData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Array;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ListFriendOfUser extends JPanel {
    // Global collections of messages of the 3 types: Unseen while online, Unseen while offline, Seen
    private Vector<Map<String, Object>> totalUnseenOnlineMessages = new Vector<>();
    private Vector<Map<String, Object>> totalUnseenOfflineMessages = new Vector<>();
    private Vector<Map<String, Object>> totalSeenMessages = new Vector<>();
    private Vector<Map<String, Object>> totalNoMessages = new Vector<>();

    // Separate JPanels to display these messages
    private int unseenOnlineMessagesIndex = 0;
    private int unseenOfflineMessagesIndex = 0;
    private int seenMessagesIndex = 0;
    private int noMessagesIndex = 0;
    private ArrayList<String> messageIndexer = new ArrayList<>();

    private Box vertical = Box.createVerticalBox();
    private JPanel currentSelectedFriend;
    private String currentSelectedFriendId;
    public ListFriendOfUser(){
        // Initialize this GUI component
        setBackground(new Color(255,255,255,255));
        setLayout(new BorderLayout());
        add(vertical, BorderLayout.PAGE_START);
//        vertical.add(Box.createVerticalGlue());
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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

                friend.setBackground(Color.decode("#ADD8E6"));
                currentSelectedFriend = friend;
                currentSelectedFriendId = friend.getConversationId();

                LocalData.setSelectedConversation(currentSelectedFriendId);
            }
        });
    }

    // Move message component at childIndex to 3 message type areas, then return the new index of the component
    int moveMessage(int childIndex, int newMessageType) {
        Box thisPanel = vertical;
        UserEachFriend conversation = (UserEachFriend) thisPanel.getComponent(childIndex);

        remove(conversation);

        switch (newMessageType) {
            case 1: {
                String conversationId = messageIndexer.get(childIndex);
                messageIndexer.remove(conversationId);
                messageIndexer.add(0, conversationId);

                thisPanel.add(conversation, 0);
                return 0;
            }
            case 2: {
                String conversationId = messageIndexer.get(childIndex);
                messageIndexer.remove(conversationId);

                if (unseenOnlineMessagesIndex <= messageIndexer.size() - 1)
                    messageIndexer.add(unseenOnlineMessagesIndex, conversationId);
                else messageIndexer.add(conversationId);

                thisPanel.add(conversation, unseenOnlineMessagesIndex);
                return unseenOnlineMessagesIndex;
            }
            case 3: {
                String conversationId = messageIndexer.get(childIndex);
                messageIndexer.remove(conversationId);

                if (unseenOnlineMessagesIndex + unseenOfflineMessagesIndex <= messageIndexer.size() - 1)
                    messageIndexer.add(unseenOnlineMessagesIndex + unseenOfflineMessagesIndex, conversationId);
                else messageIndexer.add(conversationId);

                thisPanel.add(conversation, unseenOnlineMessagesIndex + unseenOfflineMessagesIndex);
                return unseenOnlineMessagesIndex + unseenOfflineMessagesIndex;
            }
        }

        return -1;
    }

    public void resetList() {
        Box thisPanel = vertical;
        totalUnseenOnlineMessages.clear();
        totalUnseenOfflineMessages.clear();
        totalSeenMessages.clear();
        totalNoMessages.clear();
        unseenOnlineMessagesIndex = 0;
        unseenOfflineMessagesIndex = 0;
        seenMessagesIndex = 0;
        noMessagesIndex = 0;
        messageIndexer.clear();
        SwingUtilities.invokeLater(() -> {
            thisPanel.removeAll();
            thisPanel.revalidate();
            thisPanel.repaint();
        });
    }

    public void manuallySelectMessage(String conversationId) {
        UserEachFriend selectMessage = (UserEachFriend) getComponent(messageIndexer.indexOf(conversationId));

        selectMessage.setBackground(Color.decode("#ADD8E6"));
        currentSelectedFriend = selectMessage;
    }

    public void setData(Vector<Map<String, Object>> friends) {
        Box thisPanel = vertical;
        SwingUtilities.invokeLater(() -> {
            /* Add unseen messages while online */
            Vector<Map<String, Object>> unseenOnlineMessages = (Vector<Map<String, Object>>) friends.get(0).get("data");
            // If unseenOnlineMessages is empty after this line then the data is the same
            unseenOnlineMessages.removeAll(totalUnseenOnlineMessages);
            totalUnseenOnlineMessages.addAll(unseenOnlineMessages);
            for (Map<String, Object> friend: unseenOnlineMessages)
            {
                String convoName = (Boolean.parseBoolean(friend.get("IsGroup").toString())) ? friend.get("ConversationName").toString() : friend.get("MemberId").toString();
                if (!messageIndexer.contains(friend.get("ConversationId").toString())) {
                    UserEachFriend userEachFriend = new UserEachFriend(friend.get("ConversationId").toString(), "af", convoName, friend.get("Content").toString(), ((Timestamp) friend.get("Datetime")).toLocalDateTime(),"Online");
                    thisPanel.add(userEachFriend, unseenOnlineMessagesIndex);
                    messageIndexer.add(unseenOnlineMessagesIndex, friend.get("ConversationId").toString());
                    addHoverEffect(userEachFriend);
                }
                else {
                    int newIndex = moveMessage(messageIndexer.indexOf(friend.get("ConversationId").toString()), 1);
                    System.out.println(newIndex);
                    UserEachFriend userEachFriend = (UserEachFriend) thisPanel.getComponent(newIndex);
                    userEachFriend.setData(convoName, friend.get("Content").toString(), ((Timestamp) friend.get("Datetime")).toLocalDateTime(),"Online");
                }

                // Manually set the selected effect
                if (currentSelectedFriendId != null && currentSelectedFriendId.equals(friend.get("ConversationId").toString()))
                    manuallySelectMessage(currentSelectedFriendId);
                unseenOnlineMessagesIndex++;
            }

            /* Add unseen messages while offline */
            Vector<Map<String, Object>> unseenOfflineMessages = (Vector<Map<String, Object>>) friends.get(1).get("data");
            // If seenMessages is empty after this line then the data is the same
            unseenOfflineMessages.removeAll(totalUnseenOfflineMessages);
            totalUnseenOfflineMessages.addAll(unseenOfflineMessages);
            for (Map<String, Object> friend: unseenOfflineMessages) {
                String convoName = (Boolean.parseBoolean(friend.get("IsGroup").toString())) ? friend.get("ConversationName").toString() : friend.get("MemberId").toString();
                if (!messageIndexer.contains(friend.get("ConversationId").toString())) {
                    UserEachFriend userEachFriend = new UserEachFriend(friend.get("ConversationId").toString(), "af", convoName, friend.get("Content").toString(), ((Timestamp) friend.get("Datetime")).toLocalDateTime(),"Offline");
                    thisPanel.add(userEachFriend, unseenOnlineMessagesIndex + unseenOfflineMessagesIndex);
                    messageIndexer.add(unseenOnlineMessagesIndex + unseenOfflineMessagesIndex, friend.get("ConversationId").toString());
                    addHoverEffect(userEachFriend);
                }
                else {
                    int newIndex = moveMessage(messageIndexer.indexOf(friend.get("ConversationId").toString()), 2);
                    UserEachFriend userEachFriend = (UserEachFriend) thisPanel.getComponent(newIndex);
                    userEachFriend.setData(convoName, friend.get("Content").toString(), ((Timestamp) friend.get("Datetime")).toLocalDateTime(),"Offline");
                }

                // Manually set the selected effect
                if (currentSelectedFriendId != null && currentSelectedFriendId.equals(friend.get("ConversationId").toString()))
                    manuallySelectMessage(currentSelectedFriendId);
                unseenOfflineMessagesIndex++;
            }

            /* Add seen messages */
            Vector<Map<String, Object>> seenMessages = (Vector<Map<String, Object>>) friends.get(2).get("data");
            // If seenMessages is empty after this line then the data is the same
            seenMessages.removeAll(totalSeenMessages);
            totalSeenMessages.addAll(seenMessages);
            for (Map<String, Object> friend: seenMessages) {
                String convoName = (Boolean.parseBoolean(friend.get("IsGroup").toString())) ? friend.get("ConversationName").toString() : friend.get("MemberId").toString();
                if (!messageIndexer.contains(friend.get("ConversationId").toString())) {
                    UserEachFriend userEachFriend = new UserEachFriend(friend.get("ConversationId").toString(), "af", convoName, friend.get("Content").toString(), ((Timestamp) friend.get("Datetime")).toLocalDateTime(),"");
                    thisPanel.add(userEachFriend, unseenOnlineMessagesIndex + unseenOfflineMessagesIndex + seenMessagesIndex);
                    messageIndexer.add(unseenOnlineMessagesIndex + unseenOfflineMessagesIndex + seenMessagesIndex, friend.get("ConversationId").toString());
                    addHoverEffect(userEachFriend);
                }
                else {
                    int newIndex = moveMessage(messageIndexer.indexOf(friend.get("ConversationId").toString()), 3);
                    UserEachFriend userEachFriend = (UserEachFriend) thisPanel.getComponent(newIndex);
                    userEachFriend.setData(convoName, friend.get("Content").toString(), ((Timestamp) friend.get("Datetime")).toLocalDateTime(),"");
                }

                // Manually set the selected effect
                if (currentSelectedFriendId != null && currentSelectedFriendId.equals(friend.get("ConversationId").toString()))
                    manuallySelectMessage(currentSelectedFriendId);
                seenMessagesIndex++;
            }

            /* Add other friends with no messages */
            Vector<Map<String, Object>> noMessages = (Vector<Map<String, Object>>) friends.get(3).get("data");
            // If seenMessages is empty after this line then the data is the same
            noMessages.removeAll(totalNoMessages);
            totalNoMessages.addAll(noMessages);
            for (Map<String, Object> friend: noMessages) {
                UserEachFriend userEachFriend = new UserEachFriend(friend.get("ConversationId").toString(), "af", friend.get("MemberId").toString(), "", LocalDateTime.of(LocalDate.now(), LocalTime.now()),"");
                thisPanel.add(userEachFriend, unseenOnlineMessagesIndex + unseenOfflineMessagesIndex + seenMessagesIndex + noMessagesIndex);
                messageIndexer.add(unseenOnlineMessagesIndex + unseenOfflineMessagesIndex + seenMessagesIndex + noMessagesIndex, friend.get("ConversationId").toString());
                addHoverEffect(userEachFriend);

//                // Manually set the selected effect
//                if (currentSelectedFriendId != null && currentSelectedFriendId.equals(friend.get("ConversationId").toString()))
//                    manuallySelectMessage(currentSelectedFriendId);
//                seenMessagesIndex++;
            }

            thisPanel.revalidate();
        });
    }
}
