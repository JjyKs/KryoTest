package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.mygdx.Network.Client.VisualClient.KryoClient;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Keravascape";
        config.height = 768;
        config.width = 1024;

        //NetworkClient network = new NetworkClient(new GameState());
        new LwjglApplication(new KryoClient(), config);
    }
}
