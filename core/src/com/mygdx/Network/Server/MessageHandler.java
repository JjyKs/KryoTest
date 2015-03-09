package com.mygdx.Network.Server;

import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Connection;
import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Server;
import com.mygdx.Network.Server.DataStructureHandlers.PlayerHandler;
import com.mygdx.Network.Shared.Network;
import com.mygdx.Network.Shared.Player;
import com.mygdx.Network.Shared.PlayerOverNetwork;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import com.mygdx.Network.Shared.Map;

public class MessageHandler extends Thread {

    private Thread t;
    private String threadName = "MessageHandler";
    BlockingQueue<QueuedMessage> receivedMessages;
    Set<Player> loggedIn;
    Map map;
    Server server;

    public MessageHandler(BlockingQueue<QueuedMessage> receivedMessages, Set<Player> loggedIn, Map map, Server server) {
        this.receivedMessages = receivedMessages;
        this.loggedIn = loggedIn;
        this.map = map;
        this.server = server;
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

    private boolean isValid(String value) {
        if (value == null) {
            return false;
        }
        value = value.trim();
        if (value.length() == 0) {
            return false;
        }
        return true;
    }

    void loggedIn(CharacterConnection c, Player character) {
        PlayerHandler.addPlayer(character);
        c.character = character;
        loggedIn.add(character);
    }
    
    

    public void run() {
        while (true) {
            getMessages();
        }
    }

    public void getMessages() {
        try {
            QueuedMessage message = receivedMessages.take();
            Object object = message.object;
            Connection c = message.c;

            // We know all connections for this server are actually CharacterConnections.
            CharacterConnection connection = (CharacterConnection) c;
            Player character = connection.character;

            //LOGIN
            if (object instanceof Network.Login) {
                // Ignore if already logged in.
                if (character != null) {
                    return;
                }

                // Reject if the name is invalid.
                String name = ((Network.Login) object).name;
                if (!isValid(name)) {
                    c.close();
                    return;
                }

                // Reject if already logged in.
                for (Player other : loggedIn) {
                    if (other.name.equals(name)) {
                        c.close();
                        return;
                    }
                }

                c.sendTCP(new Network.RegistrationRequired());
                return;
            }

            //REGISTER
            if (object instanceof Network.Register) {
                // Ignore if already logged in.
                if (character != null) {
                    return;
                }

                Network.Register register = (Network.Register) object;

                // Reject if the login is invalid.
                if (!isValid(register.name)) {
                    c.close();
                    return;
                }
                if (!isValid(register.otherStuff)) {
                    c.close();
                    return;
                }

                character = new Player();
                character.name = register.name;
                character.message = "";
                character.x = 0;
                character.y = 0;

                loggedIn(connection, character);
                return;
            }

            //MOVECHARACTER
            if (object instanceof Network.MoveCharacter) {
                // Ignore if not logged in.
                if (character == null) {
                    return;
                }

                Network.MoveCharacter msg = (Network.MoveCharacter) object;

                if (!map.map[msg.x / 32][msg.y / 32].walkable) {
                    return;
                }
                if (msg.x >= 0 && msg.y >= 0) {
                    character.xTarget = msg.x;
                    character.yTarget = msg.y;
                }

                return;
            }

            //TALK
            if (object instanceof Network.UpdateCharacter) {
                // Ignore if not logged in.
                if (character == null) {
                    return;
                }
                Network.UpdateCharacter msg = (Network.UpdateCharacter) object;
                character.message = msg.message;
                character.lastMessage = System.currentTimeMillis();

                return;
            }

            //ASKFORTICK
            if (object instanceof Network.AskForTick) {
                // Ignore if not logged in.
                if (character == null || character.lastTick + character.tickRate - 20 > System.currentTimeMillis()) {
                    System.out.println("EARLY UPDATE");
                    return;
                }

                character.lastTick = System.currentTimeMillis();

                Network.PlayerList msg = new Network.PlayerList();
                msg.playerList = new ArrayList();
                PlayerOverNetwork pointerToPlayer = null;
                for (Player p : PlayerHandler.returnClosePlayers(character, true)) {
                    msg.playerList.add(new PlayerOverNetwork());
                    msg.playerList.get(msg.playerList.size() - 1).name = p.name;
                    msg.playerList.get(msg.playerList.size() - 1).message = p.message;
                    msg.playerList.get(msg.playerList.size() - 1).x = p.x;
                    msg.playerList.get(msg.playerList.size() - 1).y = p.y;
                    msg.playerList.get(msg.playerList.size() - 1).xTarget = p.xTarget;
                    msg.playerList.get(msg.playerList.size() - 1).yTarget = p.yTarget;
                    if (p.name.equals(character.name)) {
                        pointerToPlayer = msg.playerList.get(msg.playerList.size() - 1);
                    }
                }

                character.tickRate = Math.max(4, msg.playerList.size() * 4) - c.getReturnTripTime() - 4;
                if (character.tickRate > 1000) {
                    character.tickRate = 1000;
                }
                if (pointerToPlayer != null) {
                    pointerToPlayer.tickRate = character.tickRate;
                }
                server.sendToUDP(connection.getID(), msg);
            }
        } catch (InterruptedException ex) {
        }
    }
}