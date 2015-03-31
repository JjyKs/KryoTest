package com.mygdx.Network.Server.Dialogues;

import com.mygdx.Network.Server.MessageHandlers.QueuedMessage;
import com.mygdx.Network.Server.MessageHandlers.SentMessageHandler;
import com.mygdx.Network.Shared.Dialogue;
import com.mygdx.Network.Shared.Network;
import com.mygdx.Network.Shared.Player;
import java.util.HashMap;

/**
 *
 * @author Jyri
 */
public class BaseDialogue {

    public void onTalk(Player target) {
        Network.SendDialogue msg = new Network.SendDialogue();
        msg.dialogue = new Dialogue();
        msg.dialogue.message = "DEBUG: BaseScript talking";
        msg.dialogue.answers = new HashMap();
        msg.dialogue.answers.put(0, "Cancel..");
        msg.dialogue.answers.put(1, "Continue..");

        QueuedMessage toSend = new QueuedMessage(target.connection, msg);
        SentMessageHandler.receivedMessages.add(toSend);
    }
    
    public void onAnswer(Player target, int answerID){
         if (answerID == 0) {
            Network.SendDialogue msg = new Network.SendDialogue();
            msg.dialogue = new Dialogue();
            msg.dialogue.message = "DEBUG: BaseScript saying BYE ! ";
            msg.dialogue.answers = new HashMap();

            QueuedMessage toSend = new QueuedMessage(target.connection, msg);
            SentMessageHandler.receivedMessages.add(toSend);
        }

        if (answerID == 1) {
            Network.SendDialogue msg = new Network.SendDialogue();
            msg.dialogue = new Dialogue();
            msg.dialogue.message = "DEBUG: BaseScript answering to continue";
            msg.dialogue.answers = new HashMap();
            msg.dialogue.answers.put(0, "Continue..");

            QueuedMessage toSend = new QueuedMessage(target.connection, msg);
            SentMessageHandler.receivedMessages.add(toSend);
        }
    }
}
