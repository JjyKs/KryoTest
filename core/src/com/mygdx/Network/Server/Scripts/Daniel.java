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
    }

    @Override
    public void onUpdate() {
        if (enoughTimePassedForUpdate()) {
            super.onUpdate();
            attachedPlayer.xTarget += 32;
            attachedPlayer.yTarget += 32;
        }
    }

}
