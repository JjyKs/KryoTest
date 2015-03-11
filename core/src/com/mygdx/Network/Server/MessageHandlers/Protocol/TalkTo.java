/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    public static void process(Connection c, MessageOperator operator, Object object) {
        CharacterConnection connection = (CharacterConnection) c;
        Player character = connection.character;
        
        // Ignore if not logged in.
        if (character == null) {
            return;
        }

        Network.TalkTo msg = (Network.TalkTo) object;
        Player target = operator.loggedIn.get(msg.name);
        System.out.println(target.name);
        if (target.script != null && target.script.hasDialogue()) {
            target.script.onTalk(character);
        }
    }
}
