package com.mygdx.Network.Server.Quests.CooksAssistant;

import com.mygdx.Network.Server.DataStructureHandlers.PlayerHandler;
import com.mygdx.Network.Server.Misc.Area;
import com.mygdx.Network.Server.Quests.CooksAssistant.NPCS.Scripts.CookScript;
import com.mygdx.Network.Server.Scripts.DairyCow;
import com.mygdx.Network.Shared.Player;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Jyri
 */
public class CooksAssistantInit {

    public static String Name = "Cook's Assistant";

    public static void init(ConcurrentHashMap<String, Player> loggedIn) {
        Player cook = new Player();
        cook.health = 99;
        cook.name = "Cook";
        cook.npc = true;
        cook.variableTickedScripts.add(new CookScript(cook));

        Player dairyCow = new Player();
        dairyCow.health = 99;
        dairyCow.name = "Dairy Cow";
        dairyCow.npc = true;
        dairyCow.variableTickedScripts.add(new DairyCow(dairyCow, new Area(32, 32, 96, 96)));

        PlayerHandler.addPlayer(cook);
        loggedIn.put(cook.name, cook);
        PlayerHandler.addPlayer(dairyCow);
        loggedIn.put(dairyCow.name, dairyCow);
    }
}
