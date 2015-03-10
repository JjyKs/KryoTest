package com.mygdx.Network.Server.Scripts;

import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class BaseScript {

    long lastUpdated;       // Last time the script ran
    int updateTimer;        // How much the script waits before running again.
    Player attachedPlayer;  // Player currently running this script. Must be manually attached
    boolean blocksPlayerCommands; 
    boolean interruptible;  // Does player commands interrupt this script (follow etc. should be interruptible)
    

    public BaseScript() {
        updateTimer = 1000;
        lastUpdated = System.currentTimeMillis(); 
        blocksPlayerCommands = false;
        interruptible = true;
    }
    
    public boolean isBlocking(){
        return blocksPlayerCommands;
    }
    
    public boolean isInterruptible(){
        return interruptible;
    }
    
    public BaseScript(Player player) {
        this();
        attachedPlayer = player;
    }

    public void onUpdate() {
        lastUpdated = System.currentTimeMillis();
        //Extend this method for the functionality you want.
    }   
    
    protected boolean enoughTimePassedForUpdate(){
        return lastUpdated < System.currentTimeMillis() - updateTimer;
    }
}
