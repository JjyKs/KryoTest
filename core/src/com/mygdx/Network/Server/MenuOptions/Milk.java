package com.mygdx.Network.Server.MenuOptions;

import com.mygdx.Network.Shared.Items.Bucket;
import com.mygdx.Network.Shared.Items.BucketWithMilk;
import com.mygdx.Network.Shared.Player;

/**
 *
 * @author jyrisauk
 */
public class Milk implements MenuOption{
    Bucket bucket = new Bucket();
    String name = "Milk";
    
    @Override
    public void onAction(Player user) {
        if (user.inventory.containsItem(bucket)){
            System.out.println("Item given");
            user.inventory.swapItem(bucket, new BucketWithMilk());
        } else {
            System.out.println("Item not given");
        }
    }

    @Override
    public String getName() {
       return name;
    }
    
}
