package com.mygdx.Network.Server.Misc;


import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Connection;
import com.mygdx.Network.Shared.Player;

// This holds per connection state.
public class CharacterConnection extends Connection {

    public Player character;
}