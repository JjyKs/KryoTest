package com.mygdx.Network.Server.Quests;

import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class QuestHolder {

    int currentProgress;
    int maxProgress;
    Player attachedPlayer;

    public QuestHolder(Player attachedPlayer) {
        currentProgress = 0;
        maxProgress = 1;
        
    }
    
    public void proceed(){
        
    }

    private void carProceed() {
        
    }

}
