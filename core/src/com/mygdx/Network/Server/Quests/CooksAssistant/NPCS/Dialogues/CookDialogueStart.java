package com.mygdx.Network.Server.Quests.CooksAssistant.NPCS.Dialogues;

import com.mygdx.Network.Server.Dialogues.BaseDialogue;
import com.mygdx.Network.Server.MessageHandlers.QueuedMessage;
import com.mygdx.Network.Server.MessageHandlers.SentMessageHandler;
import com.mygdx.Network.Server.Quests.CooksAssistant.CooksAssistantInit;
import com.mygdx.Network.Shared.Dialogue;
import com.mygdx.Network.Shared.Items.Bucket;
import com.mygdx.Network.Shared.Network;
import com.mygdx.Network.Shared.Player;
import java.util.HashMap;

/**
 *
 * @author Jyri
 */
public class CookDialogueStart extends BaseDialogue {

    @Override
    public void onTalk(Player target) {

        Network.SendDialogue msg = new Network.SendDialogue();
        msg.dialogue = new Dialogue();
        msg.dialogue.message = "So, you have to collect these things for me: Milk";

        msg.dialogue.answers = new HashMap();
        msg.dialogue.answers.put(1, "No problem");
        msg.dialogue.answers.put(2, "Nah");

        QueuedMessage toSend = new QueuedMessage(target.connection, msg);
        SentMessageHandler.receivedMessages.add(toSend);
    }

    @Override
    public void onAnswer(Player target, int answerID) {
        //Coming from last dialogue
        if (answerID == 0) {
            onTalk(target);
        }

        if (answerID == 1) {
            target.quests.get(CooksAssistantInit.Name).proceed(target);
            target.inventory.giveItem(new Bucket());
            Network.SendDialogue msg = new Network.SendDialogue();
            msg.dialogue = new Dialogue();
            msg.dialogue.message = "DEBUG: Proceeded.";
            msg.dialogue.answers = new HashMap();
            msg.dialogue.answers.put(-1, "Continue..");

            QueuedMessage toSend = new QueuedMessage(target.connection, msg);
            SentMessageHandler.receivedMessages.add(toSend);
        }
        //Nah
        if (answerID == 2) {
            Network.SendDialogue msg = new Network.SendDialogue();
            msg.dialogue = new Dialogue();
            msg.dialogue.message = "DEBUG: :(((";
            msg.dialogue.answers = new HashMap();
            msg.dialogue.answers.put(-1, "Continue..");

            QueuedMessage toSend = new QueuedMessage(target.connection, msg);
            SentMessageHandler.receivedMessages.add(toSend);
        }
    }
}
