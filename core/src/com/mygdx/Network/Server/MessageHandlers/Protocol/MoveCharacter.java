package com.mygdx.Network.Server.MessageHandlers.Protocol;

import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Connection;
import com.mygdx.Network.Server.Helpers.ScriptHelper;
import com.mygdx.Network.Server.Misc.CharacterConnection;
import com.mygdx.Network.Shared.Network;
import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class MoveCharacter {

    public static void process(Connection c, MessageOperator operator, Object object) {
        CharacterConnection connection = (CharacterConnection) c;
        Player character = connection.character;
        // Ignore if not logged in.
        if (character == null) {
            return;
        }
        
        //If script blocks playermovement, we shouldn't process this packet
        if (ScriptHelper.hasBlockingScript(character)){
            return;
        }

        Network.MoveCharacter msg = (Network.MoveCharacter) object;

        if (!operator.map.map[msg.x / 32][msg.y / 32].walkable) {
            return;
        }
        if (msg.x >= 0 && msg.y >= 0) {
            character.xTarget = msg.x;
            character.yTarget = msg.y;
        }

    }
}
