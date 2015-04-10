package com.mygdx.Network.Server.Quests.CooksAssistant;

import com.mygdx.Network.Server.Dialogues.BaseDialogue;
import com.mygdx.Network.Server.Quests.CooksAssistant.NPCS.Dialogues.CookDialogueWaitingForIncredients;
import com.mygdx.Network.Server.Quests.State;
import com.mygdx.Network.Shared.Items.BucketWithMilk;
import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class WaitingForIncredients implements State {

    BaseDialogue dialogue = new CookDialogueWaitingForIncredients();

    @Override
    public State proceed(Player p) {
        if (isProceedable(p)) {
            return new Ready();
        } else {
            return this;
        }
    }

    @Override
    public boolean isProceedable(Player p) {
        return p.inventory.containsItem(new BucketWithMilk());
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
