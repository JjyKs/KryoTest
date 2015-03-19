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
        updateTimer = 5000;
    }

    @Override
    public void onUpdate() {
        if (readyToRun()) {
            super.onUpdate();
            Player attachedPlayer = attachedPlayers.get(0);
            
            attachedPlayer.message = "Moi: " + attachedPlayer.health;
            attachedPlayer.lastMessage = System.currentTimeMillis();
        }
    }

}
