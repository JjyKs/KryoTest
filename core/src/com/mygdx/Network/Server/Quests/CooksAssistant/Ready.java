package com.mygdx.Network.Server.Quests.CooksAssistant;

import com.mygdx.Network.Server.Quests.State;

/**
 *
 * @author Jyri
 */
public class Ready implements State {

    @Override
    public State proceed() {
        if (isProceedable()) {
            return new WaitingForIncredients();
        } else {
            
            return this;
        }
    }

    @Override
    public boolean isProceedable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
