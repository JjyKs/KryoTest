package com.mygdx.Network.Client;

import com.mygdx.Network.Shared.Player;
import com.mygdx.Network.Shared.PlayerOverNetwork;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameState {

    ConcurrentHashMap<String, Player> playerList = new ConcurrentHashMap();
    ConcurrentHashMap<String, Player> playerListToDraw = new ConcurrentHashMap();

    public Player currentPlayer = new Player();

    public GameState(String name) {
        currentPlayer.name = name;
    }

    public void setPlayerList(HashSet<PlayerOverNetwork> playerListFromServer) {
        playerList.clear();
        for (PlayerOverNetwork p : playerListFromServer) {
            if (p.name.equals(currentPlayer.name)) {
                currentPlayer.xTarget = p.xTarget;
                currentPlayer.yTarget = p.yTarget;
                currentPlayer.tickRate = p.tickRate;
            }
            Player newPlayer = new Player();
            newPlayer.name = p.name;
            newPlayer.x = p.x;
            newPlayer.y = p.y;
            newPlayer.yTarget = p.yTarget;
            newPlayer.xTarget = p.xTarget;
            newPlayer.message = p.message;
            
            newPlayer.timeToLive = 2;
            
            System.out.println(newPlayer.customMenuOptions);
            playerList.put(newPlayer.name, newPlayer);
        }
    }

    public void interpolate() {
        for (Player p : playerList.values()) {
            if (playerListToDraw.containsKey(p.name)) {
                Player toDraw = playerListToDraw.get(p.name);
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

                toDraw.message = p.message;
            } else {
                playerListToDraw.put(p.name, p);
            }
        }

        for (Iterator<Map.Entry<String, Player>> it = playerListToDraw.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Player> entry = it.next();
            if (!playerList.containsKey(entry.getKey())) {
                entry.getValue().timeToLive--;
                if (entry.getValue().timeToLive == 0) {
                    it.remove();
                }
            } else {
                entry.getValue().timeToLive = 16;
                if (entry.getKey().equals(currentPlayer.name)) {
                    currentPlayer.x = entry.getValue().x;
                    currentPlayer.y = entry.getValue().y;
                }
            }
        }
    }

    public ConcurrentHashMap<String, Player> getPlayerList() {
        return playerListToDraw;
    }
}
