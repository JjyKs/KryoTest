package com.mygdx.Network.Server.Scripts;

import com.mygdx.Network.Server.Helpers.NewPlayerInitiator;
import com.mygdx.Network.Shared.Player;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jyri
 */
public class DeathCheck extends BaseScript {

    Random rand = new Random();

    public DeathCheck(Player player) {
        super();
        attachedPlayers.add(player);
        updateTimer = 33;
    }

    @Override
    public void onUpdate() {
        if (readyToRun()) {
            Player p = attachedPlayers.get(0);
            super.onUpdate();
            if (p.health <= 0) {
                //Drop items  TODO TODO TODO
                
                NewPlayerInitiator.initPlayer(p);
            }
        }
    }

    public int randInt(int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

}
