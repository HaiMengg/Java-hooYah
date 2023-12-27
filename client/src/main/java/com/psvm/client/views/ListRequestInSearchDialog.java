package com.psvm.client.views;

import javax.swing.*;
import java.awt.*;

public class ListRequestInSearchDialog extends JPanel {
    public ListRequestInSearchDialog(){
        this.setBackground(Color.white);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //friendList.add(friendSearchAndAdd);

        for (int i = 1; i <= 10; i++) {
            FriendRequest friendRequestInSearchDialog = new FriendRequest("asdasd","asd","Kizark");
            this.add(friendRequestInSearchDialog);
        }
        this.add(Box.createVerticalGlue());
    }

}
