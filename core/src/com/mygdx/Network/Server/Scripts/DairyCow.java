package com.mygdx.Network.Server.Scripts;

import com.mygdx.Network.Server.MenuOptions.Milk;
import com.mygdx.Network.Server.Misc.Area;
import com.mygdx.Network.Server.Misc.Vector2i;
import com.mygdx.Network.Shared.Player;

/**
 *
 * @author JjyKs
 */
public class DairyCow extends BaseScript {

    Area area;

    public DairyCow(Player player, Area area) {
        super(player);
        updateTimer = 10000;
        interruptible = true;
        continuable = true;
        this.area = area;
        this.attachedPlayers.get(0).customMenuOptions.add(new Milk());
    }

    @Override
    public void onUpdate() {
        if (readyToRun()) {
            super.onUpdate();
            Vector2i newPos = area.getNewPosition();
            attachedPlayers.get(0).xTarget = newPos.x;
            attachedPlayers.get(0).yTarget += newPos.y;
            attachedPlayers.get(0).message = "MOO";
            attachedPlayers.get(0).lastMessage = System.currentTimeMillis();
        }
    }
}
