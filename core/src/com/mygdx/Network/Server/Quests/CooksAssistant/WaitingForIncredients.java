package com.mygdx.Network.Server.Quests.CooksAssistant;

import com.mygdx.Network.Server.Dialogues.BaseDialogue;
import com.mygdx.Network.Server.Quests.CooksAssistant.NPCS.Dialogues.CookDialogueWaitingForIncredients;
import com.mygdx.Network.Server.Quests.State;
import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class WaitingForIncredients implements State {

    BaseDialogue dialogue = new CookDialogueWaitingForIncredients();

    @Override
    public State proceed() {
        if (isProceedable()) {
            return new Ready();
        } else {
            return this;
        }
    }

    @Override
    public boolean isProceedable() {
        //Checkkaa että pelaajalla on: Milk
        return true;
    }

    @Override
    public void talkThrough(Player p) {
        dialogue.onTalk(p);
    }

    @Override
    public void answerThrough(Player p, int id) {
        dialogue.onAnswer(p, id);
    }

}
