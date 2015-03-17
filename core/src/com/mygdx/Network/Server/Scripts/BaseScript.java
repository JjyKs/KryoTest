package com.mygdx.Network.Server.Scripts;

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
public class BaseScript {

    long lastUpdated;       // Last time the script ran
    int updateTimer;        // How much the script waits before running again.
    Player attachedPlayer;  // Player currently running this script.
    boolean blocksPlayerCommands;
    boolean interruptible;  // Does player commands interrupt this script (follow etc. should be interruptible)
    boolean hasDialogue;    // Does this script allow other players to talk to you (Mainly for NPC purposes but who knows)

    public BaseScript() {
        updateTimer = 1000;
        lastUpdated = System.currentTimeMillis();
        blocksPlayerCommands = false;
        interruptible = true;
        hasDialogue = true;
    }

    public boolean isBlocking() {
        return blocksPlayerCommands;
    }

    public boolean isInterruptible() {
        return interruptible;
    }

    public boolean hasDialogue() {
        return hasDialogue;
    }

    public BaseScript(Player player) {
        this();
        attachedPlayer = player;
    }

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

    public void onAnswer(Player target, int response) {
        if (response == 0) {
            Network.SendDialogue msg = new Network.SendDialogue();
            msg.dialogue = new Dialogue();
            msg.dialogue.message = "DEBUG: BaseScript saying BYE ! ";
            msg.dialogue.answers = new HashMap();           

            QueuedMessage toSend = new QueuedMessage(target.connection, msg);
            SentMessageHandler.receivedMessages.add(toSend);
        }
        
        if (response == 1) {
            Network.SendDialogue msg = new Network.SendDialogue();
            msg.dialogue = new Dialogue();
            msg.dialogue.message = "DEBUG: BaseScript answering to continue";
            msg.dialogue.answers = new HashMap();
            msg.dialogue.answers.put(0, "Continue..");            

            QueuedMessage toSend = new QueuedMessage(target.connection, msg);
            SentMessageHandler.receivedMessages.add(toSend);
        }
    }

    public void onUpdate() {
        lastUpdated = System.currentTimeMillis();
        //Extend this method for the functionality you want.
    }

    protected boolean enoughTimePassedForUpdate() {
        return lastUpdated < System.currentTimeMillis() - updateTimer;
    }
}
