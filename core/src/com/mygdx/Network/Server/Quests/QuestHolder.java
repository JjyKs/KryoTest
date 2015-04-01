package com.mygdx.Network.Server.Quests;

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

    public void proceed() {
        if (canProceed()) {
            currentState = currentState.proceed();
        }
    }

    public boolean canProceed() {
        return currentState.isProceedable();
    }
}
