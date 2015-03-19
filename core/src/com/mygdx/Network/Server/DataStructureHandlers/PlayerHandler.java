package com.mygdx.Network.Server.DataStructureHandlers;

import com.mygdx.Network.Server.Helpers.ScriptHelper;
import com.mygdx.Network.Shared.Player;
import java.util.HashSet;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerHandler {

    static final int mapSize = 1024;
    static ConcurrentHashMap<String, Player>[][] players = new ConcurrentHashMap[mapSize][mapSize];

    public static void init() {
        for (int i = 0; i < mapSize; i++) {
            for (int o = 0; o < mapSize; o++) {
                players[i][o] = new ConcurrentHashMap();
            }
        }
    }

    public static void setPlayerMessage(Player p, int x, int y, String message) {
        //Todo, overflow protection        
        Player target = players[p.x / 10][p.y / 10].get(p.name);
        if (target != null) {
            target.message = message;
        }
    }

    public static void movePlayer(Player p, int x, int y) {
        //Todo, overflow protection        
        if (players[p.x / 10][p.y / 10].get(p.name) != null) {
            players[p.x / 10][p.y / 10].remove(p.name);
            players[(p.x + x) / 10][(p.y + y) / 10].put(p.name, p);
            p.x += x;
            p.y += y;
        }
    }

    public static void addPlayer(Player p) {
        //Todo, overflow protection        
        players[p.x / 10][p.y / 10].put(p.name, p);
    }

    public static void removePlayer(Player p) {
        //Todo, overflow protection     
        if (players[p.x / 10][p.y / 10].get(p.name) != null) {
            players[p.x / 10][p.y / 10].remove(p.name);
            if (players[p.x / 10][p.y / 10].get(p.name) != null) {
                System.out.println("Fatal: player remove failed -- 2");
            }
        } else {
            System.out.println("Fatal: player remove failed");
        }
    }

    public static boolean isValidPosition(int x, int y) {
        return (x >= 0 && x < 1024 && y >= 0 && y < 1024);
    }

    static void getPlayersFromSlot(int x, int y, HashSet<Player> toReturn) {
        if (isValidPosition(x, y)) {
            for (Player p : players[x][y].values()) {                
                //We're updating playerscripts here, so it will only happen when somebody has actual vision of this player.
                ScriptHelper.updateScripts(p);
                toReturn.add(p);
            }
        }
    }

    //Returns players close to c
    public static HashSet<Player> returnClosePlayers(Player c, boolean removeSensitiveInformation) {

        int limit = 60;
        int maxPlayers = 100;

        HashSet<Player> toReturn = new HashSet();

        int x = c.x / 10;
        int y = c.y / 10;
        for (Player p : players[x][y].values()) {
            toReturn.add(p);
        }
        int amount = 1;
        int suunta = 1;
        while (x < c.x / 10 + limit && toReturn.size() < maxPlayers) {
            if (suunta == 1) {
                int target = x - amount;
                while (x > target) {
                    getPlayersFromSlot(x, y, toReturn);
                    x--;
                }
                suunta++;
            }

            if (suunta == 2) {
                int target = y - amount;
                while (y > target) {
                    getPlayersFromSlot(x, y, toReturn);
                    y--;
                }
                suunta++;
                amount++;
            }

            if (suunta == 3) {
                int target = x + amount;
                while (x < target) {
                    getPlayersFromSlot(x, y, toReturn);
                    x++;
                }
                suunta++;
            }
            if (suunta == 4) {
                int target = y + amount;
                while (y < target) {
                    getPlayersFromSlot(x, y, toReturn);
                    y++;
                }
                suunta = 1;
                amount++;
            }
        }
        return toReturn;
    }
}
