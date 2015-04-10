package com.mygdx.Network.Server.Quests;

import com.mygdx.Network.Shared.Items.Item;
import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class QuestHolder {

    State currentState;
    int maxProgress;
    Player attachedPlayer;

    public QuestHolder(Player attachedPlayer, State state) {
        currentState = state;
        maxProgress = 1;
    }

    public void talkThroughState(Player p) {
        currentState.talkThrough(p);
    }

    public void answerThroughState(Player p, int id) {
        currentState.answerThrough(p, id);
    }

    public void proceed(Player p) {
        if (canProceed(p)) {
            currentState = currentState.proceed(p);
        }
    }
    
    

    public boolean canProceed(Player p) {
        return currentState.isProceedable(p);
    }
}
