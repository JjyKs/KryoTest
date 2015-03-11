package com.mygdx.Network.Server.MessageHandlers.Protocol;

import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Connection;
import com.mygdx.Network.Server.Misc.CharacterConnection;
import com.mygdx.Network.Shared.Network;
import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class TalkTo {

    public static boolean targetIsCloseEnough(Player source, Player target) {
        return Math.abs(source.x - target.x) <= 32 && Math.abs(source.y - target.y) <= 32;
    }

    public static boolean targetHasDialogue(Player target) {
        return target.script != null && target.script.hasDialogue();
    }

    public static boolean targetIsValid(Player source, Player target) {
        return targetIsCloseEnough(source, target) && targetHasDialogue(target);
    }

    public static void process(Connection c, MessageOperator operator, Object object) {
        CharacterConnection connection = (CharacterConnection) c;
        Player character = connection.character;

        // Ignore if not logged in.
        if (character == null) {
            return;
        }

        Network.TalkTo msg = (Network.TalkTo) object;
        Player target = operator.loggedIn.get(msg.name);

        if (targetIsValid(character, target)) {
            target.script.onTalk(character);
        }
    }
}
