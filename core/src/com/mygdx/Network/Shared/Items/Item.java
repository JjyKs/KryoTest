package com.mygdx.Network.Shared.Items;

/**
 *
 * @author Jyri
 */
public class Item {
    String name;
    String examine;
    
    public boolean combine(Item second){
        return false;
    }    
    
    public String name(){
        return name;
    }
}
