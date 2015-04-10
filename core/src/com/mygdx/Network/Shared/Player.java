package com.mygdx.Network.Shared;

import com.mygdx.Network.Server.MenuOptions.MenuOption;
import com.mygdx.Network.Server.Misc.CharacterConnection;
import com.mygdx.Network.Server.Quests.QuestHolder;
import com.mygdx.Network.Server.Scripts.BaseScript;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Player {

    public int health;
    public String name;
    public String message;
    public int id, x, y, xTarget, yTarget;
    public int timeToLive;
    public long lastTick;
    public long lastMessage;
    public int tickRate;
    public boolean npc;
    public boolean fightable;
    public ConcurrentHashMap<String, QuestHolder> quests = new ConcurrentHashMap();
    public List<MenuOption> customMenuOptions = Collections.synchronizedList(new ArrayList());
    public Inventory inventory = new Inventory();
    
    //Scripts attached to the player. Variable ones run whenever enough time has passed, and PlayerHandler updates them
    //Fixed ones run according to the servers tickrate
    public ArrayList<BaseScript> variableTickedScripts = new ArrayList();
    public ArrayList<BaseScript> fixedTickedScripts = new ArrayList();

    public CharacterConnection connection;
}
