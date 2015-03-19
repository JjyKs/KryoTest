package com.mygdx.Network.Server.Scripts;

import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class Test extends BaseScript {

    public Test(Player player) {
        super(player);
        interruptible = false;
        hasDialogue = false;
        updateTimer = 6000;
    }

    @Override
    public void onUpdate() {
        if (enoughTimePassedForUpdate()) {
            super.onUpdate();
            attachedPlayer.message = "Moi";
            attachedPlayer.lastMessage = System.currentTimeMillis();
        }
    }

}
