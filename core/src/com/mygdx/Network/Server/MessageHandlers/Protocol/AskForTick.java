package com.mygdx.Network.Server.MessageHandlers.Protocol;

import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Connection;
import com.mygdx.Network.Server.DataStructureHandlers.PlayerHandler;
import com.mygdx.Network.Server.Helpers.PlayerValidator;
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

    //Builds PlayerOverNetwork from provided player instance
    private static PlayerOverNetwork buildPON(Player p) {
        PlayerOverNetwork pon = new PlayerOverNetwork();
        pon.name = p.name;
        pon.message = p.message;
        pon.x = p.x;
        pon.y = p.y;
        pon.xTarget = p.xTarget;
        pon.yTarget = p.yTarget;
        pon.npc = p.npc;
        pon.fightable = p.fightable;

        return pon;

    }
    
    //Calculates new TickRate for player
    
    private static void setTick(Player character, int nearbyPlayerAmount, Connection c, PlayerOverNetwork pointerToPlayer){
        character.tickRate = Math.max(4, nearbyPlayerAmount * 4) - c.getReturnTripTime() - 4;
        if (character.tickRate > 1000) {
            character.tickRate = 1000;
        }
        if (pointerToPlayer != null) {
            pointerToPlayer.tickRate = character.tickRate;
        }
    }
    
    

    public static void process(Connection c, MessageOperator operator, Object object) {
        CharacterConnection connection = (CharacterConnection) c;
        Player character = connection.character;
        

        // Ignore if not logged in.
        if (PlayerValidator.isLoggedAndAllowedByTick(character)) {
            return;
        }

        character.lastTick = System.currentTimeMillis();

        Network.PlayerList msg = new Network.PlayerList();
        msg.playerList = new ArrayList();
        
        //Player also receives his own information over network. However we don't want to tell info about other players to the asker.
        PlayerOverNetwork pointerToPlayer = null;
        
        
        for (Player p : PlayerHandler.returnClosePlayers(character, true)) {
            //Adds PON instance build from Player to the answer packet.
            msg.playerList.add(buildPON(p));

            if (p.name.equals(character.name)) {
                pointerToPlayer = msg.playerList.get(msg.playerList.size() - 1);
            }
        }
        
        setTick(character, msg.playerList.size(), c, pointerToPlayer);
        operator.server.sendToUDP(connection.getID(), msg);
    }
}
