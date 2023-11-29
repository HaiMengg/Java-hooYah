package org.example;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoomList  implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private List<String> roomList = new ArrayList<>();

    RoomList(List<String> roomList) {
        this.roomList.addAll(roomList);
    }

    RoomList(String roomID) {
        this.roomList.add(roomID);
    }

    public List<String> getList() {
        return  roomList;
    }
}
