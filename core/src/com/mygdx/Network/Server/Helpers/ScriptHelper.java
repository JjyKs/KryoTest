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
        int startSize = character.variableTickedScripts.size();
        for (int i = 0; i < character.variableTickedScripts.size(); i++) {
            character.variableTickedScripts.get(i).onUpdate();
            if (character.variableTickedScripts.get(i).hasPlayers().isEmpty()) {
                character.variableTickedScripts.remove(character.variableTickedScripts.get(i));
            }
            if (character.variableTickedScripts.size() != startSize) {
                startSize = character.variableTickedScripts.size();
                i = -1;
            }

        }

    }

    public static boolean hasBlockingScript(Player character) {
        for (Iterator<BaseScript> iterator = character.variableTickedScripts.iterator(); iterator.hasNext();) {
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
