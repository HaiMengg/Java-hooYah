package com.psvm.client.views;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

class FriendListBar extends JPanel{
    //Chat of selected Friend panel
    //private final ContentPane contentPane;
    private JButton selectedButton;
    //private JButton currentButton;

    //later for chat of selected Friend
    //FriendList(ContentPane contentPane)
    FriendListBar() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(320, 820));
        this.setBorder(new MatteBorder(0,0,0,1,Color.decode("#CDD5DE")));

        // Add header
        FriendListHeader friendListHeader = new FriendListHeader();
        this.add(friendListHeader);

        // Add search option
        FriendSearchOptions friendSearchOptions = new FriendSearchOptions();
        this.add(friendSearchOptions);

        // friend search and add friend
        SearchFriendField searchFriendField = new SearchFriendField();
        AddFriendIconButton addFriendIconButton = new AddFriendIconButton();
        JPanel friendSearchAndAdd = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        friendSearchAndAdd.setBackground(Color.WHITE);
        friendSearchAndAdd.add(searchFriendField);
        friendSearchAndAdd.add(addFriendIconButton);
        this.add(friendSearchAndAdd);


        // Friend list
        JScrollPane scrollFriend = new JScrollPane();
        scrollFriend.setBorder(null);
        scrollFriend.setPreferredSize(new Dimension(320, 630));
        scrollFriend.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollFriend.getViewport().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                scrollFriend.revalidate();
                scrollFriend.repaint();
            }
        });

        this.add(scrollFriend);

        // Setup choosing panel
        FriendListChoosingCategory friendListChoosingCategory = new FriendListChoosingCategory(scrollFriend);
        this.add(friendListChoosingCategory);

    }
}