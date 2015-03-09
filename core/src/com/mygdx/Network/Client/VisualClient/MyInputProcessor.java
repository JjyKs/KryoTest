package com.mygdx.Network.Client.VisualClient;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class MyInputProcessor implements InputProcessor {

    String message = "";
    boolean sendMessage = false;
    boolean shiftPressed = false;

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ENTER) {
            sendMessage = true;
        }
        if (keycode == Keys.SHIFT_LEFT || keycode == Keys.SHIFT_RIGHT) {
            shiftPressed = true;
        }
        if (keycode == Keys.BACKSPACE) {
            shiftPressed = true;
            if (message.length() > 0) {
                message = message.substring(0, message.length() - 1);
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (!shiftPressed) {
            message += character;
        } else {
            shiftPressed = false;
        }

        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
