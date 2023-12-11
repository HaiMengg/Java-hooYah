package com.psvm.client.views.components.friend;

import com.psvm.client.controllers.FriendListRequest;
import com.psvm.client.controllers.FriendMessageListRequest;
import com.psvm.shared.socket.SocketResponse;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class ListFriendOfUserThread extends SwingWorker<Void, Map<String, Object>> {
    private Socket clientSocket;
    ObjectInputStream socketIn;
    ObjectOutputStream socketOut;
    private Observer observer;

    public interface Observer {
        public void workerDidUpdate(Vector<Map<String, Object>> message);
    }

    public ListFriendOfUserThread(Socket clientSocket, ObjectInputStream socketIn, ObjectOutputStream socketOut, Observer observer) {
        this.clientSocket = clientSocket;
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.observer = observer;

    }

    @Override
    protected Void doInBackground() throws Exception {
        FriendMessageListRequest request = new FriendMessageListRequest(clientSocket, socketIn, socketOut);
        SocketResponse response = request.talk();

        for (Map<String, Object> datum: response.getData()) {
            publish(datum);
        }

        return null;
    }

    @Override
    protected void process(List<Map<String, Object>> chunks) {
        super.process(chunks);

		Vector<Map<String, Object>> messages = new Vector<>(chunks);
        observer.workerDidUpdate(messages);
    }
}

public class ListFriendOfUser extends JPanel {
    // Multithreading + Socket
    ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
    final String SOCKET_HOST = "localhost";
    final int SOCKET_PORT = 5555;
    Socket socket;
    ObjectInputStream socketIn;
    ObjectOutputStream socketOut;

    private JPanel currentSelectedFriend;
    ListFriendOfUser(){
        // Initialize this GUI component
        setBackground(new Color(0,0,0,0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Multithreading + Socket
        try {
            socket = new Socket(SOCKET_HOST, SOCKET_PORT);
            socketIn = new ObjectInputStream(socket.getInputStream());
            socketOut = new ObjectOutputStream(socket.getOutputStream());
            startNextWorker();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void addHoverEffect(){
        
    }

    protected void startNextWorker() {
        JPanel thisPanel = this;
        ListFriendOfUserThread worker = new ListFriendOfUserThread(socket, socketIn, socketOut, new ListFriendOfUserThread.Observer() {
            @Override
            public void workerDidUpdate(Vector<Map<String, Object>> friends) {
                SwingUtilities.invokeLater(() -> {
                    thisPanel.removeAll();
                    for (Map<String, Object> friend: friends) {
                        UserEachFriend userEachFriend = new UserEachFriend("af",friend.get("ConversationName").toString(), friend.get("Content").toString(), ((Timestamp) friend.get("Datetime")).toLocalDateTime(),"Online");
                        thisPanel.add(userEachFriend);
                        // Add vertical spacing between components
                        thisPanel.add(Box.createVerticalStrut(10));
                    }
                    thisPanel.add(Box.createVerticalGlue());
                    thisPanel.revalidate();
                });
            }
        });
        worker.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (worker.getState() == SwingWorker.StateValue.DONE) {
                    worker.removePropertyChangeListener(this);
                    startNextWorker();
                }
            }
        });
        service.schedule(worker, 250, TimeUnit.MILLISECONDS);
    }
}