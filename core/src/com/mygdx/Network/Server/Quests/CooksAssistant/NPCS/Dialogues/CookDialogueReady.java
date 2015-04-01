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
public class CookDialogueReady extends BaseDialogue {

    @Override
    public void onTalk(Player target) {

        Network.SendDialogue msg = new Network.SendDialogue();
        msg.dialogue = new Dialogue();
        msg.dialogue.message = "Thanks for your help again :)";
        msg.dialogue.answers = new HashMap();

        if (!target.quests.get(CooksAssistantInit.Name).canProceed()) {
            msg.dialogue.answers.put(-1, "Np bro");
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

        //Rewarding
        if (answerID == 1) {
            target.quests.get(CooksAssistantInit.Name).proceed();
            Network.SendDialogue msg = new Network.SendDialogue();
            msg.dialogue = new Dialogue();
            msg.dialogue.message = "DEBUG: Here's your reward.";
            msg.dialogue.answers = new HashMap();
            msg.dialogue.answers.put(-1, "Continue..");

            QueuedMessage toSend = new QueuedMessage(target.connection, msg);
            SentMessageHandler.receivedMessages.add(toSend);
        }
       
    }
}
