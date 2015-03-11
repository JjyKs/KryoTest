package com.mygdx.Network.Server.MessageHandlers;

import com.mygdx.Network.KryoNetBase.esotericsoftware.kryonet.Connection;

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
