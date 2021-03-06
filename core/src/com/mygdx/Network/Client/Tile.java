/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.Network.Client;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.mygdx.Network.Shared.Player;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Jyri
 */
public class Tile {

    public int x, y, z;
    boolean kayty = false;
    HashSet<Tile> kaaret = new HashSet();
    Tile edellinen;
    public boolean walkable = true;
    ModelInstance model = null;

    public ModelInstance getOrCreateModel(ModelBuilder modelBuilder, PathFinder worldHandler, ArrayList<ModelInstance> instances, Player currentPlayer) {
        if (model != null) {
            Color asd = new Color();
            asd.r += (float) x / 48 - ((float)currentPlayer.x-32) / 32 / 48 + 0.3;
            asd.g += (float) y / 48 - ((float)currentPlayer.y-32) / 32 / 48 + 0.3;
            asd.b += (float) 0;

            Material Material = model.materials.get(0);
            Material.set(new Material(ColorAttribute.createDiffuse(asd), ColorAttribute.createSpecular(asd), FloatAttribute.createShininess(0)));

            return model;
        }

        float pz = -y * 32;
        float px = x * 32;
        Color asd = new Color();
        asd.r += (float) ((x - currentPlayer.x / 32) + 10) / 100;
        asd.g += (float) ((y - currentPlayer.y / 32) + 10) / 100;
        asd.b += (float) 0;

        Model floor = modelBuilder.createRect(
                px, worldHandler.getMap()[x][y].z, pz,
                px + 32, worldHandler.getMap()[x + 1][y].z, pz,
                px + 32, worldHandler.getMap()[x + 1][y + 1].z, pz - 32,
                px, worldHandler.getMap()[x][y + 1].z, pz - 32,
                1f, 1f, -1f,
                new Material(ColorAttribute.createDiffuse(asd), ColorAttribute.createSpecular(asd), FloatAttribute.createShininess(0)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
        );

        model = new ModelInstance(floor);
        return model;

    }
}

/*Model floor = modelBuilder.createRect(
 px, worldHandler.getMap()[x][y].z, pz,
 px + 32, worldHandler.getMap()[x + 1][y].z, pz,
 px + 32, worldHandler.getMap()[x + 1][y + 1].z, pz - 32,
 px, worldHandler.getMap()[x][y + 1].z, pz - 32,
 1f, 1f, -1f,
 new Material(ColorAttribute.createDiffuse(asd), ColorAttribute.createSpecular(asd), FloatAttribute.createShininess(0)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
 );*/
