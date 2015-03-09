package com.mygdx.Network.Shared;

import com.mygdx.Network.Server.NPCScripts.BaseScript;

public class Player {
    public String name;
    public String message;
    public int id, x, y, xTarget, yTarget;
    public int timeToLive;
    public long lastTick;
    public long lastMessage;
    public int tickRate;
    public boolean npc;
    public BaseScript script;    
}
