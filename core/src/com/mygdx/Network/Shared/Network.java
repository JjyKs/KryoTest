package com.mygdx.Network.Shared;

import com.esotericsoftware.kryo.Kryo;
import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.EndPoint;
import java.util.ArrayList;

// This class is a convenient place to keep things common to both the client and server.
public class Network {

    static public final int portTCP = 54555;
    static public final int portUDP = 54556;

    // This registers objects that are going to be sent over the network.
    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Login.class);
        kryo.register(RegistrationRequired.class);
        kryo.register(Register.class);
        kryo.register(UpdateCharacter.class);
        kryo.register(Player.class);
        kryo.register(PlayerOverNetwork.class);
        kryo.register(MoveCharacter.class);
        kryo.register(PlayerList.class);
        kryo.register(AskForTick.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(AskForMap.class);
        kryo.register(Dialogue.class);
        kryo.register(SendDialogue.class);
        kryo.register(DialogueAnswer.class);
        kryo.register(TalkTo.class);
        kryo.register(StartFight.class);
        kryo.register(java.util.HashMap.class);
        kryo.register(UseCustomOption.class);
        kryo.register(String[].class);
    }

    static public class Login {

        public String id;
    }

    static public class RegistrationRequired {
    }

    static public class Register {

        public String name;
        public String otherStuff;
    }

    static public class UpdateCharacter {

        public String message;
    }

    static public class MoveCharacter {

        public int x, y;
    }

    static public class PlayerList {

        public ArrayList<PlayerOverNetwork> playerList;
    }

    static public class AskForTick {
    }

    static public class AskForMap {
    }

    static public class SendDialogue {

        public Dialogue dialogue;
    }

    static public class DialogueAnswer {

        public String name;
        public int id;
    }

    static public class TalkTo {

        public String name;
    }

    static public class StartFight {

        public String name;
    }

    static public class UseCustomOption {

        public String name;
        public int id;
    }

}
