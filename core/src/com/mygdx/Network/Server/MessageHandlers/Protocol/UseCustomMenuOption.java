package com.mygdx.Network.Server.MessageHandlers.Protocol;

import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Connection;
import com.mygdx.Network.Server.Misc.CharacterConnection;
import com.mygdx.Network.Server.Scripts.BaseScript;
import com.mygdx.Network.Shared.Network;
import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class UseCustomMenuOption {

    public static boolean targetIsCloseEnough(Player source, Player target) {
        return Math.abs(source.x - target.x) <= 32 && Math.abs(source.y - target.y) <= 32;
    }

    public static boolean targetHasCustomOptionWithThisId(Player target, int id) {        
        System.out.println("Target name: " + target.id);
        System.out.println(target.customMenuOptions.size());
        System.out.println(id);
        return (target.customMenuOptions.size() >= id);
    }

    public static boolean targetIsValid(Player source, Player target, int id) {
        return targetIsCloseEnough(source, target) && targetHasCustomOptionWithThisId(target, id);
    }

    public static void process(Connection c, MessageOperator operator, Object object) {
        CharacterConnection connection = (CharacterConnection) c;
        Player character = connection.character;

        // Ignore if not logged in.
        if (character == null) {
            return;
        }

        Network.UseCustomOption msg = (Network.UseCustomOption) object;
        Player target = operator.loggedIn.get(msg.name);
        int id = msg.id;

        if (targetIsValid(character, target, id)) {
            System.out.println("Target is valid");
            System.out.println(target.id);
            System.out.println(target.customMenuOptions.get(id).getName());
            target.customMenuOptions.get(id).onAction(character);
        } else {
            System.out.println("Target is INvalid!");
        }

    }
}
