package com.mygdx.Network.Server;

import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Connection;
import com.mygdx.Network.Shared.Player;

/**
 *
 * @author Jyri
 */
public class QueuedMessage {
    public Connection c;
    public Object object;
    
    public QueuedMessage(Connection c, Object object){
        this.c = c;
        this.object = object;
    }
}
