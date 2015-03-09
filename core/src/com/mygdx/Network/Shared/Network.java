package com.mygdx.Network.Shared;

import com.esotericsoftware.kryo.Kryo;
import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.EndPoint;
import java.util.ArrayList;
import java.util.HashSet;

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
    }

    static public class Login {

        public String name;
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
}
