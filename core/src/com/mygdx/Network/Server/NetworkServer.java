package com.mygdx.Network.Server;

import com.mygdx.Network.Server.MessageHandlers.QueuedMessage;
import com.mygdx.Network.Server.Misc.CharacterConnection;
import com.mygdx.Network.Server.MessageHandlers.ReceivedMessageHandler;
import com.mygdx.Network.Server.MessageHandlers.SentMessageHandler;
import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Connection;
import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Listener;
import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.mygdx.Network.Shared.Network;
import com.mygdx.Network.Server.DataStructureHandlers.PlayerHandler;
import com.mygdx.Network.Shared.Map;
import com.mygdx.Network.Shared.Player;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkServer {

    Server server;
    //Set<Player> loggedIn = Collections.newSetFromMap(new ConcurrentHashMap<Player, Boolean>());
    ConcurrentHashMap loggedIn = new ConcurrentHashMap<Player, Boolean>();
    Map map = new Map();
    BlockingQueue<QueuedMessage> receivedMessages = new ArrayBlockingQueue(10240);

    public NetworkServer() throws IOException {
        server = new Server() {
            @Override
            protected Connection newConnection() {
                // By providing our own connection implementation, we can store per
                // connection state without a connection ID to state look up.
                return new CharacterConnection();
            }
        };

        // For consistency, the classes to be sent over the network are
        // registered by the same method for both the client and server.
        Network.register(server);

        server.addListener(new Listener() {
            @Override
            public void received(Connection c, Object object) {
                try {
                    if (receivedMessages.remainingCapacity() == 0) {
                        receivedMessages.take();
                        System.out.println("FATAL: MESSAGEHANDLER NOT HANDLING MESSAGES FAST ENOUGH");
                    }
                    receivedMessages.put(new QueuedMessage(c, object));
                } catch (InterruptedException ex) {
                }
            }

            @Override
            public void disconnected(Connection c) {
                CharacterConnection connection = (CharacterConnection) c;
                if (connection.character != null) {
                    System.out.println("REMOVE");
                    PlayerHandler.removePlayer(connection.character);
                    loggedIn.remove(connection.character);
                    System.out.println(loggedIn.size()); 
               }
            }
        });
        server.bind(Network.portTCP, Network.portUDP);
        server.start();
        initThreads();
    }

    public void initThreads() {
        ServerLoop serverLoop = new ServerLoop(loggedIn, map);
        serverLoop.start();

        ReceivedMessageHandler messageHandler = new ReceivedMessageHandler(receivedMessages, loggedIn, map, server);
        messageHandler.start();

        SentMessageHandler sentMessageHandler = new SentMessageHandler();
        sentMessageHandler.start();
    }

    public static void main(String[] args) throws IOException {
        PlayerHandler.init();
        System.out.println("server started :D");
        Log.set(Log.LEVEL_NONE);
        new NetworkServer();
    }
}
