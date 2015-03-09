/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.Network.Client;

import java.util.HashSet;

/**
 *
 * @author Jyri
 */
public class Tile {
        public int x, y;
        boolean kayty = false;
        HashSet<Tile> kaaret = new HashSet();
        int lol = 0;
        Tile edellinen;
        public boolean walkable = true;
    }