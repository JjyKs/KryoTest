package com.mygdx.Network.Server.NPCScripts;


/**
 *
 * @author Jyri
 */
public class Daniel extends BaseScript {  

    @Override
    public void onUpdate() {
       
        if (lastUpdated < System.currentTimeMillis() - 5000) {
            lastUpdated = System.currentTimeMillis();
            System.out.println("Running npc script Daniel");
            attachedPlayer.xTarget += 32;
            attachedPlayer.yTarget += 32;
            System.out.println(attachedPlayer.xTarget);
        }
    }
      
}
