package com.mygdx.Network.Server.Scripts;

import com.mygdx.Network.Server.Scripts.BaseScript;
import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class Daniel extends BaseScript {

    public Daniel(Player player) {
        super(player);
        updateTimer = 5000;
        interruptible = true;
        continuable = true;
    }

    @Override
    public void onUpdate() {
        if (readyToRun()) {
            super.onUpdate();
            attachedPlayers.get(0).xTarget += 32;
            attachedPlayers.get(0).yTarget += 32;
        }
    }

}
