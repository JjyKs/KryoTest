package com.mygdx.Network.Client;

import java.util.Random;

public class GameLogic {

    NetworkClient network;//Connects to the server
    GameState state; //Contains current gamestate
    static Random rand = new Random();
    public PathFinder worldHandler = new PathFinder();
    int lastX, lastY;

    public static int randInt(int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public GameLogic() {
        state = new GameState(NetworkClient.name);
        network = new NetworkClient(state);
        lastX = state.currentPlayer.x;
        lastY = state.currentPlayer.y;
    }

    long lastTick = System.currentTimeMillis();

    public void movePlayer(int x, int y) {
        network.movePlayer(x, y);
    }

    public void sendMessage(String message) {
        network.sendMessage(message);
    }

    public boolean newMoveCommandIssued = false;

    public void update() {
       

        boolean skipFirstChecks = state.currentPlayer.x == lastX && state.currentPlayer.y == lastY;
        if (newMoveCommandIssued) {
            skipFirstChecks = true;
            newMoveCommandIssued = false;
        }

        if (Math.abs(state.currentPlayer.x - state.currentPlayer.xTarget) < 20 || skipFirstChecks) {
            if (Math.abs(state.currentPlayer.y - state.currentPlayer.yTarget) < 20 || skipFirstChecks) {
                if (worldHandler.routeToFollow.size() > 0) {
                    network.movePlayer(worldHandler.routeToFollow.get(0).x, worldHandler.routeToFollow.get(0).y);
                    worldHandler.routeToFollow.remove(0);
                }
            }
        }

        lastX = state.currentPlayer.x;
        lastY = state.currentPlayer.y;

        if (System.currentTimeMillis() > lastTick + state.currentPlayer.tickRate) {
            network.askForTick();
            lastTick = System.currentTimeMillis();
        }
        state.interpolate();
    }

    public GameState getGameState() {
        return state;
    }
}
