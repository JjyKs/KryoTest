package com.mygdx.Network.Server.Helpers;

import com.mygdx.Network.Server.DataStructureHandlers.PlayerHandler;
import com.mygdx.Network.Server.Scripts.BaseScript;
import com.mygdx.Network.Server.Scripts.Daniel;
import com.mygdx.Network.Server.Scripts.DeathCheck;
import com.mygdx.Network.Server.Scripts.Test;
import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class NewPlayerInitiator {

    static public void initPlayer(Player p) {
        for (BaseScript s : p.scripts){
            s.removePlayerFromScript(p);
        }        
        p.scripts.clear();
        p.health = 20;
        p.scripts.add(new DeathCheck(p));
        p.scripts.add(new Daniel(p));
        p.scripts.add(new Test(p));
        PlayerHandler.resetPlayer(p);
    }
}
