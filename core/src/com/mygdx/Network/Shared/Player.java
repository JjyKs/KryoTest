package com.mygdx.Network.Shared;

import com.mygdx.Network.Server.Misc.CharacterConnection;
import com.mygdx.Network.Server.Scripts.BaseScript;
import java.util.ArrayList;

public class Player {
    public String name;
    public String message;
    public int id, x, y, xTarget, yTarget;
    public int timeToLive;
    public long lastTick;
    public long lastMessage;
    public int tickRate;
    public boolean npc;
    public ArrayList<BaseScript> scripts = new ArrayList();
    public CharacterConnection connection;
}
