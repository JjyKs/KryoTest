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
            user.inventory.swapItem(bucket, new BucketWithMilk());
        }
    }

    @Override
    public String getName() {
       return name;
    }
    
}
