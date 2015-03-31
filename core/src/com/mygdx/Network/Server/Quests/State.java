package com.mygdx.Network.Server.Quests;

/**
 *
 * @author Jyri
 */
public interface State {    
    State proceed();
    boolean isProceedable();
}
