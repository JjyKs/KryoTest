package com.mygdx.Network.Server.MessageHandlers;

import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Connection;
import com.mygdx.Network.Server.Misc.CharacterConnection;
import com.mygdx.Network.Shared.Network;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SentMessageHandler extends Thread {

    private Thread t;
    private String threadName = "SentMessageHandler";
    static public BlockingQueue<QueuedMessage> receivedMessages = new ArrayBlockingQueue(10240);

    public SentMessageHandler() {
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
            processMessages();
        }
    }

    public void processMessages() {
        try {
            QueuedMessage message = receivedMessages.take();
            Object object = message.object;
            Connection c = message.c;

            // We know all connections for this server are actually CharacterConnections.
            CharacterConnection connection = (CharacterConnection) c;

            //SendDialogue
            if (object instanceof Network.SendDialogue) {
                c.sendUDP((Network.SendDialogue) object);
                return;
            }

        } catch (InterruptedException ex) {
        }
    }
}
