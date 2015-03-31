package com.mygdx.Network.Server.Scripts;

import com.mygdx.Network.Server.Dialogues.BaseDialogue;
import com.mygdx.Network.Shared.Player;
import java.util.ArrayList;

/**
 *
 * @author Jyri
 */
public class BaseScript {

    long lastUpdated;           // Last time the script ran
    int updateTimer;            // How much the script waits before running again.

    //You should spawn new instance for every player, unless they're interacting through this script (Fighting, trading, etc.) Single user scripts only use first Player of this array
    ArrayList<Player> attachedPlayers; // Player(s) currently running this script. 
    boolean blocksPlayerCommands;
    BaseDialogue dialogue;        // Does this script allow other players to talk to you (Mainly for NPC purposes but who knows)    

    //Interrupt related
    boolean interruptible;      // Does player commands interrupt this script (follow etc. should be interruptible)
    boolean continuable;        // Is it possible to continue this script after interruption
    boolean interrupted;        // Is this script interrupted
    BaseScript interruptor;     // The script that interrupted this script.

    //Basescript doesn't do anything on it's own. You should manually set these TRUE if you need them in your own script.
    public BaseScript() {
        attachedPlayers = new ArrayList();
        updateTimer = 1000;
        lastUpdated = System.currentTimeMillis();
        blocksPlayerCommands = false;
        interruptible = false;
        interrupted = false;
        continuable = false;
        dialogue = null;
    }

    public boolean isBlocking() {
        return blocksPlayerCommands;
    }

    public boolean hasDialogue() {
        return dialogue != null;
    }

    public BaseScript(Player player) {
        this();
        attachedPlayers.add(player);
    }

    public void removePlayerFromScript(Player player) {
        attachedPlayers.remove(player);
    }

    //
    public void onTalk(Player target) {
        if (hasDialogue()) {
            dialogue.onTalk(target);
        }
    }

    public void onAnswer(Player target, int response) {
        if (hasDialogue()) {
            dialogue.onAnswer(target, response);
        }
    }

    public ArrayList<Player> hasPlayers() {
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
                    p.variableTickedScripts.remove(this);
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

    protected void isThisScriptReferencedInInterrupter() {
        boolean scriptReferenced = false;

        for (Player p : interruptor.attachedPlayers) {
            if (p.variableTickedScripts.contains(interruptor)) {
                scriptReferenced = true;
            }
        }
        if (!scriptReferenced) {
            interruptor = null;
            interrupted = false;
        }
    }

    protected void isInterrupterStillAlive() {
        if (interruptor != null) {
            if (interruptor.attachedPlayers.isEmpty()) {
                interruptor = null;
            } else {
                isThisScriptReferencedInInterrupter();
            }
        } else {
            interrupted = false;
        }
    }

    protected boolean readyToRun() {
        if (interrupted) {
            isInterrupterStillAlive();
        }

        return lastUpdated < System.currentTimeMillis() - updateTimer && !interrupted;
    }
}
