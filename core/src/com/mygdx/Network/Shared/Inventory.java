package com.mygdx.Network.Shared;

import com.mygdx.Network.Shared.Items.Item;

/**
 *
 * @author Jyri
 */
public class Inventory {

    Item[] items;

    public Inventory() {
        items = new Item[28];
    }

    public boolean containsItem(Item item) {
        for (Item i : items) {
            if (i.name().equals(item.name())) {
                return true;
            }
        }
        return false;
    }

    public void swapItem(Item fromInventory, Item toInventory) {
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            if (item.name().equals(fromInventory.name())) {
                items[i] = toInventory;
                break;  
            }
        }
    }

    public boolean containsMultiple(Item item, int amount) {
        for (Item i : items) {
            if (i.name().equals(item.name())) {
                amount--;
            }
        }
        return amount <= 0;
    }

}
