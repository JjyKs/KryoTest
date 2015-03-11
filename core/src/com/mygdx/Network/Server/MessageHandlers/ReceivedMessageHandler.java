package com.mygdx.Network.Server.MessageHandlers;

import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Server;
import com.mygdx.Network.Server.MessageHandlers.Protocol.MessageOperator;
import com.mygdx.Network.Shared.Player;
import java.util.concurrent.BlockingQueue;
import com.mygdx.Network.Shared.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReceivedMessageHandler extends Thread {

    private Thread t;
    private String threadName = "MessageHandler";
    BlockingQueue<QueuedMessage> receivedMessages;
    ConcurrentHashMap<String, Player> loggedIn;
    Map map;
    Server server;
    MessageOperator operator;

    public ReceivedMessageHandler(BlockingQueue<QueuedMessage> receivedMessages, ConcurrentHashMap<String, Player> loggedIn, Map map, Server server) {
        this.receivedMessages = receivedMessages;
        this.loggedIn = loggedIn;
        this.map = map;
        this.server = server;
        operator = new MessageOperator(loggedIn, map, server);
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
            System.out.println(threadName + " Successfully initiated");
        } else {
            System.out.println("messagehandlererror");
        }
    }

    public void run() {
        while (true) {
            getMessages();
        }
    }

    public void getMessages() {
        try {
            QueuedMessage message = receivedMessages.take();
            operator.process(message.object, message.c);

        } catch (InterruptedException ex) {
        }
    }
}
