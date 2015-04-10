package com.mygdx.Network.Server.Misc;

import java.util.Random;

/**
 *
 * @author jyrisauk
 */
public class Area {

    private int x;
    private int y;
    private int xMax;
    private int yMax;
    private Random rand;

    public Area(int x, int y, int xMax, int yMax) {
        this.x = x;
        this.y = y;
        this.yMax = yMax;
        this.xMax = xMax;
        rand = new Random();
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int xMax() {
        return xMax;
    }

    public int yMax() {
        return yMax;
    }

    public Vector2i getNewPosition() {
        return new Vector2i(randInt(x, xMax), randInt(y, yMax));
    }

    public int randInt(int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
