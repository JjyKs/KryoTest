package com.mygdx.Network.Server.Quests.CooksAssistant;

import com.mygdx.Network.Server.DataStructureHandlers.PlayerHandler;
import com.mygdx.Network.Server.Misc.Area;
import com.mygdx.Network.Server.Misc.IdGenerator;
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

    public static void init(ConcurrentHashMap<Integer, Player> loggedIn) {
        Player cook = new Player();
        cook.health = 99;
        cook.id = IdGenerator.getID();
        cook.nameSWAP = "Cook";
        cook.npc = true;
        cook.variableTickedScripts.add(new CookScript(cook));

        Player dairyCow = new Player();
        dairyCow.health = 99;
        dairyCow.id = IdGenerator.getID();
        dairyCow.npc = true;
        dairyCow.nameSWAP = "Dairy Cow";
        dairyCow.variableTickedScripts.add(new DairyCow(dairyCow, new Area(32, 32, 96, 96)));

        PlayerHandler.addPlayer(cook);
        loggedIn.put(cook.id, cook);
        PlayerHandler.addPlayer(dairyCow);
        loggedIn.put(dairyCow.id, dairyCow);
    }
}
