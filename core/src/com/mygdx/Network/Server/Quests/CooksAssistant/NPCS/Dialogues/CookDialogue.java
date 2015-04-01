package com.mygdx.Network.Server.Quests.CooksAssistant.NPCS.Dialogues;

import com.mygdx.Network.Server.Dialogues.BaseDialogue;
import com.mygdx.Network.Server.MessageHandlers.QueuedMessage;
import com.mygdx.Network.Server.MessageHandlers.SentMessageHandler;
import com.mygdx.Network.Server.Quests.CooksAssistant.CooksAssistantInit;
import com.mygdx.Network.Server.Quests.CooksAssistant.*;
import com.mygdx.Network.Server.Quests.QuestHolder;
import com.mygdx.Network.Shared.Dialogue;
import com.mygdx.Network.Shared.Network;
import com.mygdx.Network.Shared.Player;
import java.util.HashMap;

/**
 *
 * @author Jyri
 */
public class CookDialogue extends BaseDialogue {

    @Override
    public void onTalk(Player target) {
        if (target.quests.containsKey(CooksAssistantInit.Name)) {
            target.quests.get(CooksAssistantInit.Name).talkThroughState(target);
        } else {
            Network.SendDialogue msg = new Network.SendDialogue();
            msg.dialogue = new Dialogue();
            msg.dialogue.message = "Hello, can you collect these incredients for me?";

            msg.dialogue.answers = new HashMap();
            msg.dialogue.answers.put(0, "No problem");
            msg.dialogue.answers.put(1, "Nah");

            QueuedMessage toSend = new QueuedMessage(target.connection, msg);
            SentMessageHandler.receivedMessages.add(toSend);
        }
    }

    @Override
    public void onAnswer(Player target, int answerID) {
      
        if (target.quests.containsKey(CooksAssistantInit.Name)) {
            target.quests.get(CooksAssistantInit.Name).answerThroughState(target, answerID);
        } else {
            //No problem
            if (answerID == 0) {
                target.quests.put(CooksAssistantInit.Name, new QuestHolder(target, new Start()));
                Network.SendDialogue msg = new Network.SendDialogue();
                msg.dialogue = new Dialogue();
                msg.dialogue.message = "DEBUG: Quest added!";
                msg.dialogue.answers = new HashMap();
                msg.dialogue.answers.put(0, "Continue..");

                QueuedMessage toSend = new QueuedMessage(target.connection, msg);
                SentMessageHandler.receivedMessages.add(toSend);
            }

            //Nah
            if (answerID == 1) {
                Network.SendDialogue msg = new Network.SendDialogue();
                msg.dialogue = new Dialogue();
                msg.dialogue.message = "DEBUG: ASD";
                msg.dialogue.answers = new HashMap();
                msg.dialogue.answers.put(-1, "Continue..");

                QueuedMessage toSend = new QueuedMessage(target.connection, msg);
                SentMessageHandler.receivedMessages.add(toSend);
            }
        }
    }
}
