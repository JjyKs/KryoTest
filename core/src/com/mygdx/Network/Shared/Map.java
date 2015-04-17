package com.mygdx.Network.Shared;

import com.mygdx.Network.Client.Tile;

/**
 *
 * @author Jyri
 */
public class Map {

    public Tile[][] map = new Tile[320][320];

    public Map() {
        for (int i = 0; i < 320; i++) {
            for (int ii = 0; ii < 320; ii++) {
                map[i][ii] = new Tile();
                map[i][ii].x = i;
                map[i][ii].y = ii;
                map[i][ii].z = (int) (Math.random() * 35);
                if (i >= 0 && ii == 9 && i <= 35) {
                    map[i][ii].walkable = false;
                }

                if (i == 20 && ii >= 9 && ii <= 30) {
                    map[i][ii].walkable = false;
                }

                if (i >= 5 && ii == 30 && i <= 35) {
                    map[i][ii].walkable = false;
                }

                if (i >= 0 && ii == 25 && i <= 10) {
                    map[i][ii].walkable = false;
                }

            }
        }
    }
}
