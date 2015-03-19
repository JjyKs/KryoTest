package com.mygdx.Network.Server.Helpers;

import com.mygdx.Network.Server.Scripts.BaseScript;
import com.mygdx.Network.Shared.Player;
import java.util.Iterator;

/**
 *
 * @author Jyri
 */
public class ScriptHelper {

    public static void updateScripts(Player character) {
        for (BaseScript script : character.scripts){
            script.onUpdate();
        }
    }

    public static boolean hasBlockingScript(Player character) {
        for (Iterator<BaseScript> iterator = character.scripts.iterator(); iterator.hasNext();) {
            BaseScript script = iterator.next();
            if (script != null) {
                if (script.isBlocking()) {
                    return true;
                } else if (script.isInterruptible()) {
                    iterator.remove();
                }
            }
        }
        return false;
    }

}
