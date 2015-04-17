package com.mygdx.Network.Server.MessageHandlers.Protocol;

import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Connection;
import com.mygdx.Network.Server.Misc.CharacterConnection;
import com.mygdx.Network.Server.Misc.IdGenerator;
import com.mygdx.Network.Shared.Network;
import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class Register {

    public static void process(Connection c, MessageOperator operator, Object object) {
        CharacterConnection connection = (CharacterConnection) c;
        Player character = connection.character;

        // Ignore if already logged in.
        if (character != null) {
            return;
        }

        Network.Register register = (Network.Register) object;

        // Reject if the login is invalid.
        if (!operator.isValid(register.name)) {
            c.close();
            return;
        }
        /*if (!operator.isValid(register.otherStuff)) {
            c.close();
            return;
        }*/

        character = new Player();
        character.id = IdGenerator.getID();
        character.nameSWAP = register.name;
        character.message = "";
        character.x = 0;
        character.y = 0;
        character.connection = connection;
        operator.loggedIn(connection, character);
    }
}
