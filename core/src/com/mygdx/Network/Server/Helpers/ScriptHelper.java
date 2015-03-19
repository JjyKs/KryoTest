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
        int startSize = character.scripts.size();
        for (int i = 0; i < character.scripts.size(); i++) {
            character.scripts.get(i).onUpdate();
            if (character.scripts.get(i).hasPlayers().isEmpty()) {
                character.scripts.remove(character.scripts.get(i));
            }
            if (character.scripts.size() != startSize) {
                startSize = character.scripts.size();
                i = -1;
            }

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
