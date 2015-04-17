package com.mygdx.Network.Client;

import com.mygdx.Network.Shared.Map;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Jyri
 */

// TODO, REWORK WHOLE CLASS. It's working but very messy hack quickly thrown together.
public class PathFinder {

    public ArrayList<Tile> routeToFollow = new ArrayList();

    public Map mapHandler = new Map();
    public Tile[][] map;

    ArrayList<Tile> tilet;

    public Tile[][] getMap() {
        return map;
    }

    public void searchRoute(int oldX, int oldY, int x, int y) {
        routeToFollow.clear();
        tilet = new ArrayList<Tile>();
        for (int i = 0; i < 320; i++) {
            for (int ii = 0; ii < 320; ii++) {
                map[i][ii].kayty = false;
                map[i][ii].edellinen = null;
            }
        }
        if (x < 0) {
            x = 0;
        }
        if (x > 319) {
            x = 319;
        }

        if (y < 0) {
            y = 0;
        }
        if (y > 319) {
            y = 319;
        }
        tilet.add(map[oldX][oldY]);
        map[oldX][oldY].kayty = true;
        haku(x, y);
    }


    public void haku(int targetX, int targetY) {
        int maxAmount = 10000;
        int calculator = 0;

        for (int i = 0; i < tilet.size(); i++) {
            calculator++;
            for (Tile t : tilet.get(i).kaaret) {                
                if (!t.kayty) {
                    t.kayty = true;
                    tilet.add(t);
                    tilet.get(tilet.size() - 1).edellinen = tilet.get(i);
                }
            }
            if (calculator == maxAmount) {
                break;
            }
            if (map[targetX][targetY].edellinen != null) {
                break;
            }
        }

        Tile asd = map[targetX][targetY];
        routeToFollow.clear();
        while (asd.edellinen != null) {
            routeToFollow.add(asd);
            asd = asd.edellinen;
        }
        Collections.reverse(routeToFollow);
    }

    private void addKaari(Tile t1, Tile t2) {       
        if (t1.walkable && t2.walkable) {
            t1.kaaret.add(t2);
        } 
    }

    public PathFinder() {
        map = mapHandler.map;

        for (int i = 0; i < 320; i++) {
            for (int ii = 0; ii < 320; ii++) {

                if (i < 320 - 1) {
                    addKaari(map[i][ii], map[i + 1][ii]);
                }
                if (i > 0) {
                    addKaari(map[i][ii], map[i - 1][ii]);
                }
                if (ii < 320 - 1) {
                    addKaari(map[i][ii], map[i][ii + 1]);
                }
                if (ii > 0) {
                    addKaari(map[i][ii], map[i][ii - 1]);
                }

            }
        }
    }

    public static void main(String[] args) {
        PathFinder asd = new PathFinder();
        asd.searchRoute(5, 9, 4, 9);
    }

}
