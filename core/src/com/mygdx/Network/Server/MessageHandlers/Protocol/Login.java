package com.mygdx.Network.Server.MessageHandlers.Protocol;

import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Connection;
import com.mygdx.Network.Server.Misc.CharacterConnection;
import com.mygdx.Network.Shared.Network;
import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class Login {

    public static void process(Connection c, MessageOperator operator, Object object) {
        CharacterConnection connection = (CharacterConnection) c;
        Player character = connection.character;
        
        // Ignore if already logged in.
        if (character != null) {
            return;
        }

        // Reject if the name is invalid.
        String name = ((Network.Login) object).name;
        if (!operator.isValid(name)) {
            c.close();
            return;
        }

        // Reject if already logged in.
        if (operator.loggedIn.contains(name)) {
            c.close();
            return;
        }
        c.sendTCP(new Network.RegistrationRequired());
        return;
    }
}
