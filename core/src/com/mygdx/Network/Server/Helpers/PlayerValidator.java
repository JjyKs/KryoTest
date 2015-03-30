package com.mygdx.Network.Server.Helpers;

import com.mygdx.Network.Shared.Player;

/**
 *
 * @author jjyks
 */
public class PlayerValidator{

    public static boolean isLoggedAndAllowedByTick(Player character) {
        return character == null
                || character.lastTick + character.tickRate - 20 > System.currentTimeMillis();
    }
}
