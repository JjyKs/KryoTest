package com.mygdx.Network.Server.MenuOptions;

import com.mygdx.Network.Shared.Player;

/**
 *
 * @author jyrisauk
 */
public interface MenuOption {
    public void onAction(Player user);
    public String getName();
}
