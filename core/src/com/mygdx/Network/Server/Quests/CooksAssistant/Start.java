/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.Network.Server.Quests.CooksAssistant;

import com.mygdx.Network.Server.Dialogues.BaseDialogue;
import com.mygdx.Network.Server.Quests.CooksAssistant.NPCS.Dialogues.CookDialogueStart;
import com.mygdx.Network.Server.Quests.State;
import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class Start implements State {
    
    BaseDialogue dialogue = new CookDialogueStart();
    
    @Override
    public State proceed(Player p) {
        if (isProceedable(p)) {
            return new WaitingForIncredients();
        } else {            
            return this;
        }
    }

    @Override
    public boolean isProceedable(Player p) {
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
