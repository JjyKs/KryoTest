
package com.mygdx.Network.Server.Scripts;

import com.mygdx.Network.Shared.Player;

/**
 *
 * @author JjyKs
 */
public class DairyCow extends BaseScript {

    public DairyCow(Player player) {
        super(player);
        updateTimer = 1000;
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
