package com.mygdx.Network.Server.MessageHandlers.Protocol;

import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Connection;
import com.mygdx.Network.Server.DataStructureHandlers.PlayerHandler;
import com.mygdx.Network.Server.Misc.CharacterConnection;
import com.mygdx.Network.Shared.Network;
import com.mygdx.Network.Shared.Player;
import com.mygdx.Network.Shared.PlayerOverNetwork;
import java.util.ArrayList;

/**
 *
 * @author Jyri
 */
public class AskForTick {

    public static void process(Connection c, MessageOperator operator, Object object) {
        CharacterConnection connection = (CharacterConnection) c;
        Player character = connection.character;
        // Ignore if not logged in.
        if (character == null || character.lastTick + character.tickRate - 20 > System.currentTimeMillis()) {
            return;
        }

        character.lastTick = System.currentTimeMillis();

        Network.PlayerList msg = new Network.PlayerList();
        msg.playerList = new ArrayList();
        PlayerOverNetwork pointerToPlayer = null;
        for (Player p : PlayerHandler.returnClosePlayers(character, true)) {
            msg.playerList.add(new PlayerOverNetwork());
            msg.playerList.get(msg.playerList.size() - 1).name = p.name;
            msg.playerList.get(msg.playerList.size() - 1).message = p.message;
            msg.playerList.get(msg.playerList.size() - 1).x = p.x;
            msg.playerList.get(msg.playerList.size() - 1).y = p.y;
            msg.playerList.get(msg.playerList.size() - 1).xTarget = p.xTarget;
            msg.playerList.get(msg.playerList.size() - 1).yTarget = p.yTarget;
            if (p.name.equals(character.name)) {
                pointerToPlayer = msg.playerList.get(msg.playerList.size() - 1);
            }
        }

        character.tickRate = Math.max(4, msg.playerList.size() * 4) - c.getReturnTripTime() - 4;
        if (character.tickRate > 1000) {
            character.tickRate = 1000;
        }
        if (pointerToPlayer != null) {
            pointerToPlayer.tickRate = character.tickRate;
        }
        operator.server.sendToUDP(connection.getID(), msg);
    }
}
