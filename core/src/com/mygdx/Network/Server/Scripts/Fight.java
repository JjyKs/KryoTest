package com.mygdx.Network.Server.Scripts;

import com.mygdx.Network.Shared.Player;
import java.util.Random;

/**
 *
 * @author Jyri
 */
public class Fight extends BaseScript {

    Random rand = new Random();
    //Emit these into the player
    int attackSpeed = 5;
    int attackTimer = 0;
    boolean firstRun = true;

    public Fight(Player player, Player player2) {
        super();
        attachedPlayers.add(player);
        attachedPlayers.add(player2);
        updateTimer = 33;
    }

    @Override
    public void onUpdate() {
        if (firstRun) {
            for (Player player : attachedPlayers) {
                for (BaseScript s : player.variableTickedScripts) {
                    if (s.isInterruptible()) {
                        s.interrupt(this);
                    }
                }
            }
            firstRun = false;
        }
        
        if (attachedPlayers.size() < 2) {
            for (Player p : attachedPlayers) {
                for (BaseScript s : p.variableTickedScripts) {
                    s.release();
                }
            }
            attachedPlayers.clear();
            System.out.println("Empty script running: Fight");
            return;
        }

        if (readyToRun()) {
            super.onUpdate();
            if (attackTimer == attackSpeed) {
                attachedPlayers.get(0).health -= Math.max(0, randInt(0, 10) - 5);
                attachedPlayers.get(1).health -= Math.max(0, randInt(0, 10) - 5);
                System.out.println("");
                System.out.println(attachedPlayers.get(0).health);
                System.out.println(attachedPlayers.get(0).health);
                attackTimer = 0;
            } else {
                attackTimer++;
            }
        }
    }

    public int randInt(int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    @Override
    public void removePlayerFromScript(Player player) {
        super.removePlayerFromScript(player);
    }

}
