package com.mygdx.Network.Client.VisualClient;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.Network.Client.GameLogic;
import com.mygdx.Network.Client.GameState;
import com.mygdx.Network.Client.NetworkClient;
import com.mygdx.Network.Client.PathFinder;
import com.mygdx.Network.Shared.Player;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class KryoClient extends ApplicationAdapter {

    public KryoClient() {

    }

    SpriteBatch batch;
    Texture img;
    Texture img2;
    GameLogic game;
    BitmapFont font;
    GameState state;
    MyInputProcessor inputProcessor;
    PathFinder worldHandler;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        img2 = new Texture("background.jpg");
        game = new GameLogic();
        font = new BitmapFont();
        inputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(inputProcessor);
        worldHandler = game.worldHandler;
    }

    public void update() {
        game.update();
        state = game.getGameState();
    }

    boolean mouseDown = false;

    @Override
    public void render() {
        update();
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ConcurrentHashMap<String, Player> pelaajat = new ConcurrentHashMap(state.getPlayerList());
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (!mouseDown) {
                game.newMoveCommandIssued = true;
                worldHandler.searchRoute(state.currentPlayer.x / 32, state.currentPlayer.y / 32, (Gdx.input.getX() + state.currentPlayer.x - 224) / 32, (480 - Gdx.input.getY() + state.currentPlayer.y - 224) / 32);
                mouseDown = true;
            }
        } else {
            mouseDown = false;
        }
        if (inputProcessor.sendMessage) {
            inputProcessor.sendMessage = false;
            game.sendMessage(inputProcessor.message);
            inputProcessor.message = "";
        }
        batch.begin();

        for (int x = Math.max(state.currentPlayer.x / 32 - 7, 0); x < Math.min(state.currentPlayer.x / 32 + 14, 319); x++) {
            for (int y = Math.max(state.currentPlayer.y / 32 - 7, 0); y < Math.min(state.currentPlayer.y / 32 + 14, 319); y++) {
                if (worldHandler.getMap()[x][y].walkable) {
                    batch.draw(img2, 224 + x * 32 - state.currentPlayer.x, 224 + y * 32 - state.currentPlayer.y);
                }
            }
        }

        for (Player p : pelaajat.values()) {
            batch.draw(img, p.x + 224 - state.currentPlayer.x, p.y + 224 - state.currentPlayer.y);
            font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            font.draw(batch, p.name + " ", p.x + 224 - state.currentPlayer.x, p.y + 224 - state.currentPlayer.y);
            if (p.message != null) {
                font.draw(batch, p.message, p.x + 224 - state.currentPlayer.x, p.y + 274 - state.currentPlayer.y);
            }
        }

        font.draw(batch, "Chat: " + inputProcessor.message, 32, 32);
        batch.end();
    }
}
