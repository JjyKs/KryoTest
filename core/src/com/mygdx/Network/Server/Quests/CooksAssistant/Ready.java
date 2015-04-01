package com.mygdx.Network.Server.Quests.CooksAssistant;

import com.mygdx.Network.Server.Dialogues.BaseDialogue;
import com.mygdx.Network.Server.Quests.CooksAssistant.NPCS.Dialogues.CookDialogueReady;
import com.mygdx.Network.Server.Quests.State;
import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class Ready implements State {

    BaseDialogue dialogue = new CookDialogueReady();
    boolean rewardsNotGiven = true;

    @Override
    public State proceed() {
        if (isProceedable()) {
            System.out.println("Pelaajalle annettiin kakun pala ! :)");
            rewardsNotGiven = false;
        } else {
            System.out.println("Pelaaja on jo saanut rewardit.");
        }
        return this;
    }

    @Override
    public boolean isProceedable() {
        return rewardsNotGiven;
    }

    @Override
    public void talkThrough(Player p) {
        if (isProceedable()) {
            proceed();
            answerThrough(p, 1);
        } else {
            dialogue.onTalk(p);
        }
    }

    @Override
    public void answerThrough(Player p, int id) {
        if (isProceedable()) {
            proceed();
            answerThrough(p, 1);
        } else {
            dialogue.onAnswer(p, id);
        }
    }

}
