package com.mygdx.Network.Server.Scripts;

import com.mygdx.Network.Server.MessageHandlers.QueuedMessage;
import com.mygdx.Network.Server.MessageHandlers.SentMessageHandler;
import com.mygdx.Network.Shared.Dialogue;
import com.mygdx.Network.Shared.Network;
import com.mygdx.Network.Shared.Player;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Jyri
 */
public class BaseScript {

    long lastUpdated;           // Last time the script ran
    int updateTimer;            // How much the script waits before running again.
    //You should spawn new instance for every player, unless they're interacting through this script (Fighting, trading, etc.)
    ArrayList<Player> attachedPlayers; // Player(s) currently running this script. 
    boolean blocksPlayerCommands;
    boolean interruptible;      // Does player commands interrupt this script (follow etc. should be interruptible)
    boolean hasDialogue;        // Does this script allow other players to talk to you (Mainly for NPC purposes but who knows)
    boolean interrupted;
    BaseScript interruptor;
    boolean continuable;

    public BaseScript() {
        attachedPlayers = new ArrayList();
        updateTimer = 1000;
        lastUpdated = System.currentTimeMillis();
        blocksPlayerCommands = false;
        interruptible = false;
        hasDialogue = false;
        interrupted = false;
        continuable = false;
    }

    public boolean isBlocking() {
        return blocksPlayerCommands;
    }

    public boolean hasDialogue() {
        return hasDialogue;
    }

    public BaseScript(Player player) {
        this();
        attachedPlayers.add(player);
    }

    public void removePlayerFromScript(Player player) {
        attachedPlayers.remove(player);
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

    public ArrayList<Player> hasPlayers(){
        return attachedPlayers;
    }
    
    public void onUpdate() {
        lastUpdated = System.currentTimeMillis();

        //Extend this method for the functionality you want.
    }

    public void interrupt(BaseScript interruptor) {
        if (isInterruptible()) {
            if (continuable) {
                this.interruptor = interruptor;
                interrupted = true;
            } else {
                for (Player p : attachedPlayers) {
                    p.scripts.remove(this);
                    this.attachedPlayers.clear();
                }
            }
        }
    }

    public void release() {
        interrupted = false;
        interruptor = null;
    }

    public boolean isInterruptible() {
        return !interrupted && interruptible;
    }

    protected boolean readyToRun() {
        if (interrupted) {
            if (interruptor != null) {
                if (interruptor.attachedPlayers.isEmpty()) {
                    interruptor = null;
                } else {
                    boolean scriptReferenced = false;
                    for (Player p : interruptor.attachedPlayers) {
                        if (p.scripts.contains(interruptor)) {
                            scriptReferenced = true;
                        }
                    }
                    if (!scriptReferenced) {
                        interruptor = null;
                        interrupted = false;

                    }
                }
            } else {
                interrupted = false;
            }
        }

        return lastUpdated < System.currentTimeMillis() - updateTimer && !interrupted;
    }
}
