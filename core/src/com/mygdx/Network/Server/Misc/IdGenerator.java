/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.Network.Server.Misc;

/**
 *
 * @author JjyKs
 */
public class IdGenerator {

    static int currentID = 10;

    public synchronized static int getID() {
        currentID++;
        return currentID - 1;
    }
}
