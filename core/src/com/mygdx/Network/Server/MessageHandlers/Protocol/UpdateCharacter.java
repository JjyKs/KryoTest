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
public class UpdateCharacter {

    public static void process(Connection c, Object object) {
        CharacterConnection connection = (CharacterConnection) c;
        Player character = connection.character;

        if (character == null) {
            return;
        }
        Network.UpdateCharacter msg = (Network.UpdateCharacter) object;
        character.message = msg.message;
        character.lastMessage = System.currentTimeMillis();
    }
}
