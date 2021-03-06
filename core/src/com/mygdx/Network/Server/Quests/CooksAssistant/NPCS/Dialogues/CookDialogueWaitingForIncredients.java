package com.mygdx.Network.Server.Quests.CooksAssistant.NPCS.Dialogues;

import com.mygdx.Network.Server.Dialogues.BaseDialogue;
import com.mygdx.Network.Server.MessageHandlers.QueuedMessage;
import com.mygdx.Network.Server.MessageHandlers.SentMessageHandler;
import com.mygdx.Network.Server.Quests.CooksAssistant.CooksAssistantInit;
import com.mygdx.Network.Shared.Dialogue;
import com.mygdx.Network.Shared.Network;
import com.mygdx.Network.Shared.Player;
import java.util.HashMap;

/**
 *
 * @author Jyri
 */
public class CookDialogueWaitingForIncredients extends BaseDialogue {

    @Override
    public void onTalk(Player target) {

        Network.SendDialogue msg = new Network.SendDialogue();
        msg.dialogue = new Dialogue();
        msg.dialogue.message = "Do you have the milk?";
        msg.dialogue.answers = new HashMap();

        if (!target.quests.get(CooksAssistantInit.Name).canProceed(target)) {
            msg.dialogue.answers.put(1, "No");
        } else {
            msg.dialogue.answers.put(2, "Yes");
        }

        QueuedMessage toSend = new QueuedMessage(target.connection, msg);
        SentMessageHandler.receivedMessages.add(toSend);
    }

    @Override
    public void onAnswer(Player target, int answerID) {
        //Coming from last dialogue
        if (answerID == 0) {
            onTalk(target);
        }

         //Yes
        if (answerID == 2) {
            target.quests.get(CooksAssistantInit.Name).proceed(target);
                    
            Network.SendDialogue msg = new Network.SendDialogue();
            msg.dialogue = new Dialogue();
            msg.dialogue.message = "DEBUG: Ok, let me make this cake.";
            msg.dialogue.answers = new HashMap();
            msg.dialogue.answers.put(0, "Continue..");

            QueuedMessage toSend = new QueuedMessage(target.connection, msg);
            SentMessageHandler.receivedMessages.add(toSend);
        }
        
        //No
        if (answerID == 1) {
            Network.SendDialogue msg = new Network.SendDialogue();
            msg.dialogue = new Dialogue();
            msg.dialogue.message = "DEBUG: Come back when you have: Milk";
            msg.dialogue.answers = new HashMap();
            msg.dialogue.answers.put(-1, "Continue..");

            QueuedMessage toSend = new QueuedMessage(target.connection, msg);
            SentMessageHandler.receivedMessages.add(toSend);
        }
       
    }
}
