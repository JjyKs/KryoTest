package com.mygdx.Network.Server;

import com.mygdx.Network.Server.DataStructureHandlers.PlayerHandler;
import com.mygdx.Network.Server.Quests.CooksAssistant.CooksAssistantInit;
import com.mygdx.Network.Server.Scripts.Daniel;
import com.mygdx.Network.Server.Scripts.DeathCheck;
import com.mygdx.Network.Server.Scripts.Test;
import com.mygdx.Network.Shared.Map;
import com.mygdx.Network.Shared.Player;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ServerLoop extends Thread {

    private Thread t;
    ConcurrentHashMap<String, Player> loggedIn;
    Map map;
    private String threadName = "serverLoop";

    public ServerLoop(ConcurrentHashMap<String, Player> loggedIn, Map map) {
        this.loggedIn = loggedIn;
        this.map = map;
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
            System.out.println(threadName + " Successfully initiated");
        } else {
            System.out.println("serverlooperror");
        }
    }

    //Removes too old messages from Player p
    public void hideOldMessages(Player p) {
        if (p.lastMessage < System.currentTimeMillis() - 1800) {
            p.message = "";
            PlayerHandler.setPlayerMessage(p, p.x, p.y, p.message);
        } else {
            PlayerHandler.setPlayerMessage(p, p.x, p.y, p.message);
        }
    }

    //Fixes player movement command to the grid if needed. Shouldn't happen ever without hacked client.
    //REFACTOR
    public void fixPlayerTargetToGrid(Player p) {
        if (p.xTarget % 32 != 0) {
            int smaller = p.xTarget;
            int larger = p.xTarget;
            while (smaller % 32 != 0 && larger % 32 != 0) {
                smaller--;
                larger++;
            }
            if (smaller % 32 == 0) {
                p.xTarget = smaller;
            } else if (larger % 32 == 0) {
                p.xTarget = larger;
            }
        }

        if (p.yTarget % 32 != 0) {
            int smaller = p.yTarget;
            int larger = p.yTarget;
            while (smaller % 32 != 0 && larger % 32 != 0) {
                smaller--;
                larger++;
            }
            if (smaller % 32 == 0) {
                p.yTarget = smaller;
            } else if (larger % 32 == 0) {
                p.yTarget = larger;
            }
        }
    }

    public void movePlayer(Player p, Map map, int amount) {
        boolean xMoved = false;
        p.targetRotation = 0;
        if (p.x < p.xTarget && map.map[(p.x + 33) / 32][(p.y) / 32].walkable) {
            PlayerHandler.movePlayer(p, amount, 0);
            xMoved = true;
            p.targetRotation = 1;
        }
        if (p.x >= 32 && p.x > p.xTarget && map.map[(p.x - 33) / 32][(p.y) / 32].walkable) {
            PlayerHandler.movePlayer(p, -amount, 0);
            xMoved = true;
            p.targetRotation = 2;
        }
        if (p.y < p.yTarget && map.map[(p.x) / 32][(p.y + 33) / 32].walkable) {
            PlayerHandler.movePlayer(p, 0, amount);
            p.targetRotation += 10;
        }
        if (p.y >= 32 && p.y > p.yTarget && map.map[(p.x) / 32][(p.y - 33) / 32].walkable) {
            PlayerHandler.movePlayer(p, 0, -amount);
            p.targetRotation += 20;
        }

    }

    @Override
    public void run() {
        CooksAssistantInit.init(loggedIn);
        Player daniel = new Player();
        daniel.health = 5;
        daniel.name = "Daniel";
        daniel.npc = true;
        daniel.variableTickedScripts.add(new Daniel(daniel));
        daniel.variableTickedScripts.add(new Test(daniel));
        daniel.variableTickedScripts.add(new DeathCheck(daniel));
        PlayerHandler.addPlayer(daniel);
        loggedIn.put(daniel.name, daniel);

        while (true) {
            long startTime = System.nanoTime();
            for (Player p : loggedIn.values()) {
                hideOldMessages(p);
                fixPlayerTargetToGrid(p);
                movePlayer(p, map, 2);
            }

            long endTime = System.nanoTime();
            long durationInMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            if (durationInMs > 0) {
                System.out.println("ServerLoop took: ");
                System.out.println(durationInMs);
            }
            try {
                Thread.sleep(Math.max(0, 33 - durationInMs));
            } catch (InterruptedException ex) {
            }
        }
    }
}
