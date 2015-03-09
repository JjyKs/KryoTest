package com.mygdx.Network.Server.NPCScripts;

import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class BaseScript {

    long lastUpdated;
    Player attachedPlayer;

    public BaseScript() {
        lastUpdated = System.currentTimeMillis();
    }

    public void onUpdate() {
        lastUpdated = System.currentTimeMillis();
    }

    public void setAttachedPlayer(Player player) {
        attachedPlayer = player;
    }
}
