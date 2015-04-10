package com.mygdx.Network.Server.Quests.CooksAssistant.NPCS.Scripts;

import com.mygdx.Network.Server.Quests.CooksAssistant.NPCS.Dialogues.CookDialogue;
import com.mygdx.Network.Server.Scripts.BaseScript;
import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class CookScript extends BaseScript {

    public CookScript(Player p) {
        super(p);
        updateTimer = 5000;
        attachedPlayers.add(p);
        dialogue = new CookDialogue();
    }

    @Override
    public void onUpdate() {
        if (readyToRun()) {
            super.onUpdate();
            attachedPlayers.get(0).message = "OLEN KOKKI";
            attachedPlayers.get(0).lastMessage = System.currentTimeMillis();
        }
    }

}
