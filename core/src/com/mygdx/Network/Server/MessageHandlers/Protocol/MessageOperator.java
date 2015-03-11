package com.mygdx.Network.Server.MessageHandlers.Protocol;

import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Connection;
import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Server;
import com.mygdx.Network.Server.DataStructureHandlers.PlayerHandler;
import com.mygdx.Network.Server.Misc.CharacterConnection;
import com.mygdx.Network.Shared.Map;
import com.mygdx.Network.Shared.Network;
import com.mygdx.Network.Shared.Player;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Jyri
 */
public class MessageOperator {

    ConcurrentHashMap<String, Player> loggedIn;
    Map map;
    Server server;

    public MessageOperator(ConcurrentHashMap<String, Player> loggedIn, Map map, Server server) {
        this.loggedIn = loggedIn;
        this.map = map;
        this.server = server;
    }

    protected boolean isValid(String value) {
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

        loggedIn.put(character.name, character);
    }

    public void process(Object object, Connection c) {
        if (object instanceof Network.Login) {                  //Login
            Login.process(c, this, object);
        } else if (object instanceof Network.Register) {        //Register
            Register.process(c, this, object);
        } else if (object instanceof Network.MoveCharacter) {   //MoveCharacter
            MoveCharacter.process(c, this, object);
        } else if (object instanceof Network.UpdateCharacter) { //UpdateCharacter
            UpdateCharacter.process(c, object);
        } else if (object instanceof Network.TalkTo) {          //TalkTo (RequestDialog)
            TalkTo.process(c, this, object);
        } else if (object instanceof Network.AskForTick) {      //AskForTick
            AskForTick.process(c, this, object);
        }
    }
}
