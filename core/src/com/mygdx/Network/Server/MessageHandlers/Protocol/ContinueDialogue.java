/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.Network.Server.MessageHandlers.Protocol;

import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Connection;
import static com.mygdx.Network.Server.MessageHandlers.Protocol.TalkTo.targetIsValid;
import com.mygdx.Network.Server.Misc.CharacterConnection;
import com.mygdx.Network.Server.Scripts.BaseScript;
import com.mygdx.Network.Shared.Network;
import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class ContinueDialogue {

    public static boolean targetIsCloseEnough(Player source, Player target) {
        return Math.abs(source.x - target.x) <= 32 && Math.abs(source.y - target.y) <= 32;
    }

     public static boolean targetHasDialogue(Player target) {
        for (BaseScript script : target.variableTickedScripts) {
            if (script.hasDialogue()) {
                return true;
            }
        }
        return false;
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

        Network.DialogueAnswer msg = (Network.DialogueAnswer) object;
        Player target = operator.loggedIn.get(msg.name);

        if (targetIsValid(character, target)) {
            for (BaseScript script : target.variableTickedScripts) {
               script.onAnswer(character, msg.id);               
            }
        }
    }
}
