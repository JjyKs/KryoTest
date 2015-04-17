package com.mygdx.Network.Client;

import com.mygdx.Network.Shared.Player;
import com.mygdx.Network.Shared.PlayerOverNetwork;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameState {

    ConcurrentHashMap<Integer, Player> playerList = new ConcurrentHashMap();
    ConcurrentHashMap<Integer, Player> playerListToDraw = new ConcurrentHashMap();

    public Player currentPlayer = new Player();

    public GameState(String name) {
        currentPlayer.nameSWAP = name;
    }

    public void setPlayerList(HashSet<PlayerOverNetwork> playerListFromServer) {
        playerList.clear();
        for (PlayerOverNetwork p : playerListFromServer) {
            if (p.nameSwap.equals(currentPlayer.nameSWAP)) {
                currentPlayer.xTarget = p.xTarget;
                currentPlayer.yTarget = p.yTarget;
                currentPlayer.tickRate = p.tickRate;
                currentPlayer.id = p.id;
            }
            Player newPlayer = new Player();
            newPlayer.id = p.id;
            newPlayer.x = p.x;
            newPlayer.y = p.y;
            newPlayer.yTarget = p.yTarget;
            newPlayer.xTarget = p.xTarget;
            newPlayer.message = p.message;
            newPlayer.targetRotation = p.targetRotation;
            
       
            
            newPlayer.timeToLive = 2;
            
            //for (String s : p.customMenuOptions){
            //    System.out.println(s);
            //}
            
            
            playerList.put(newPlayer.id, newPlayer);
        }
    }

    public void interpolate() {
        for (Player p : playerList.values()) {
            if (playerListToDraw.containsKey(p.id)) {
                Player toDraw = playerListToDraw.get(p.id);
                if (toDraw.x < p.x) {
                    toDraw.x += Math.max(1, (p.x - toDraw.x) / 128);
                } else if (toDraw.x > p.x) {
                    toDraw.x -= Math.max(1, (toDraw.x - p.x) / 128);
                }

                if (toDraw.y < p.y) {
                    toDraw.y += Math.max(1, (p.y - toDraw.y) / 128);
                } else if (toDraw.y > p.y) {
                    toDraw.y -= Math.max(1, (toDraw.y - p.y) / 128);
                }
                int maxWarpDistance = 4;
                if (Math.abs(toDraw.x - p.x) > maxWarpDistance) {
                    toDraw.x = p.x;
                }

                if (Math.abs(toDraw.y - p.y) > maxWarpDistance) {
                    toDraw.y = p.y;
                }
                
                toDraw.xTarget = p.xTarget;
                toDraw.yTarget = p.yTarget;

                toDraw.message = p.message;
                toDraw.nameSWAP = p.nameSWAP;
                toDraw.targetRotation = p.targetRotation;
                toDraw.id = p.id;
            } else {
                
                playerListToDraw.put(p.id, p);
            }
        }

        for (Iterator<Map.Entry<Integer, Player>> it = playerListToDraw.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Integer, Player> entry = it.next();
            if (!playerList.containsKey(entry.getKey())) {
                entry.getValue().timeToLive--;
                if (entry.getValue().timeToLive == 0) {
                    it.remove();
                }
            } else {
                entry.getValue().timeToLive = 16;
                if (entry.getKey() == (currentPlayer.id)) {
                    currentPlayer.x = entry.getValue().x;
                    currentPlayer.y = entry.getValue().y;
                }
            }
        }
    }

    public ConcurrentHashMap<Integer, Player> getPlayerList() {
        return playerListToDraw;
    }
}
