package com.psvm.client.controllers;

import com.psvm.client.controllers.objects.SocketTalk;
import com.psvm.client.settings.LocalData;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class UnBlockUserWithId extends SocketTalk {
    public UnBlockUserWithId(Socket socket, ObjectInputStream socketIn, ObjectOutputStream socketOut, String userId) {
        super(socket, socketIn, socketOut, SocketTalk.BLOCK_CODE_UNBLOCK_USER, Map.of(
                "blocker", LocalData.getCurrentUsername(),
                "blocked", userId
        ));
    }
}
