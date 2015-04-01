package com.mygdx.Network.Client;

import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Client;
import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Connection;
import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Listener;
import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.esotericsoftware.minlog.Log;
import com.mygdx.Network.Shared.Network;
import com.mygdx.Network.Shared.Network.AskForTick;
import com.mygdx.Network.Shared.Network.DialogueAnswer;
import com.mygdx.Network.Shared.Network.Login;
import com.mygdx.Network.Shared.Network.MoveCharacter;
import com.mygdx.Network.Shared.Network.PlayerList;
import com.mygdx.Network.Shared.Network.Register;
import com.mygdx.Network.Shared.Network.RegistrationRequired;
import com.mygdx.Network.Shared.Network.SendDialogue;
import com.mygdx.Network.Shared.Network.StartFight;
import com.mygdx.Network.Shared.Network.TalkTo;
import com.mygdx.Network.Shared.Network.UpdateCharacter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

public class NetworkClient {

    private Client client;
    GameState state;
    static String name = Integer.toString(new Random().nextInt());

    public NetworkClient(GameState initState) {
        init();
        state = initState;

        // ThreadedListener runs the listener methods on a different thread.
        client.addListener(new ThreadedListener(new Listener() {

            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof RegistrationRequired) {
                    Register register = new Register();
                    register.name = name;
                    register.otherStuff = "Random";
                    client.sendTCP(register);
                }

                if (object instanceof UpdateCharacter) {
                    return;
                }

                if (object instanceof PlayerList) {
                    PlayerList msg = (PlayerList) object;
                    state.setPlayerList(new HashSet(msg.playerList));
                    return;
                }

                if (object instanceof SendDialogue) {
                    SendDialogue msg = (SendDialogue) object;
                    System.out.println(msg.dialogue.message);
                    System.out.println(msg.dialogue.answers.keySet() + " " + msg.dialogue.answers.values());
                    return;
                }

            }

            public void disconnected(Connection connection) {
                System.exit(0);
            }

        }));

        //String host = JOptionPane.showInputDialog("IP: ");
        String host = "localhost";
        //host = "91.157.237.40"; //Localhostin ulkoinen
        try {
            client.connect(10000, host, Network.portTCP, Network.portUDP);
            login();
        } catch (IOException ex) {
        }

    }

    private void login() {
        Login login = new Login();
        Random random = new Random();
        login.name = name;
        client.sendTCP(login);
    }

    private void init() {
        System.out.println("Client started");
        Log.set(Log.LEVEL_NONE);
        client = new Client();
        client.start();

        // For consistency, the classes to be sent over the network are
        // registered by the same method for both the client and server.
        Network.register(client);
    }

    //Methods for GameLogic to communicate with NetworkClient
    public void movePlayer(int x, int y) {
        x = x * 32;
        y = y * 32;
        MoveCharacter msg = new MoveCharacter();
        msg.y = y;
        msg.x = x;
        client.sendUDP(msg);
    }

    public void askForTick() {
        client.sendUDP(new AskForTick());
    }

    public void sendMessage(String message) {
        System.out.println(message);
        if (message.contains("FIGHT")) {
            StartFight msg = new StartFight();
            msg.name = "Cook";
            client.sendUDP(msg);
            System.out.println("DialogueFightSent");
        }
        if (message.contains("TALK")) {
            TalkTo msg = new TalkTo();
            msg.name = "Cook";
            client.sendUDP(msg);
            System.out.println("DialogueAskSent");
        } else if (message.contains("2")) {
            DialogueAnswer msg = new DialogueAnswer();
            msg.name = "Cook";
            msg.id = 2;
            client.sendUDP(msg);
            System.out.println("DialogueAnswerSent");
        } else if (message.contains("1")) {
            DialogueAnswer msg = new DialogueAnswer();
            msg.name = "Cook";
            msg.id = 1;
            client.sendUDP(msg);
            System.out.println("DialogueAnswerSent");
        } else if (message.contains("0")) {
            DialogueAnswer msg = new DialogueAnswer();
            msg.name = "Cook";
            msg.id = 0;
            client.sendUDP(msg);
            System.out.println("DialogueAnswerSent");
        } else if (message.contains("-1")) {
            DialogueAnswer msg = new DialogueAnswer();
            msg.name = "Cook";
            msg.id = -1;
            client.sendUDP(msg);
            System.out.println("DialogueAnswerSent");
        } else {
            UpdateCharacter msg = new UpdateCharacter();
            msg.message = message;
            client.sendUDP(msg);
        }
    }

    public static void main(String[] args) {
        new NetworkClient(new GameState(name));
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
