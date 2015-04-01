package com.mygdx.Network.Server.Quests;

import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public interface State {    
    State proceed();
    boolean isProceedable();
    void talkThrough(Player p);
    void answerThrough(Player p, int id);
}
