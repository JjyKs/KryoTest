package com.mygdx.Network.Client.VisualClient;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.Network.Client.GameLogic;
import static com.mygdx.Network.Client.GameLogic.randInt;
import com.mygdx.Network.Client.GameState;
import com.mygdx.Network.Client.PathFinder;
import com.mygdx.Network.Shared.Player;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

//PLACEHOLDER CLIENT FOR TESTING.
public class KryoClient extends ApplicationAdapter {

    public ModelBuilder modelBuilder = new ModelBuilder();

    public KryoClient() {

    }
    Model model;
    SpriteBatch batch;
    Texture img;
    Texture img2;
    GameLogic game;
    BitmapFont font;
    GameState state;
    MyInputProcessor inputProcessor;
    PathFinder worldHandler;

    public Environment environment;
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;
    public ArrayList<ModelInstance> instances;
    public Decal sprite;
    public DecalBatch decalBatch;
    Texture image;

    @Override
    public void create() {
        instances = new ArrayList();
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        img2 = new Texture("background.jpg");
        game = new GameLogic();
        font = new BitmapFont();
        inputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(inputProcessor);
        worldHandler = game.worldHandler;
        image = new Texture("background.jpg");

        // Load a Texture
        sprite = Decal.newDecal(32, 32, new TextureRegion(image), true);
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 220f, 100f);

        GroupStrategy asd = new CameraGroupStrategy(cam);
        // create a DecalBatch to render them with just once at startup
        decalBatch = new DecalBatch(asd);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();

        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 500f;
        cam.update();

        model = modelBuilder.createBox(
                32, 32, 32,
                new Material(ColorAttribute.createDiffuse(Color.GREEN), ColorAttribute.createSpecular(Color.WHITE), FloatAttribute.createShininess(16f)), Usage.Position | Usage.Normal);

    }

    public void update() {
        game.update();
        state = game.getGameState();
    }

    boolean mouseDown = false;

    public void simulatePlayer() {
        if (worldHandler.routeToFollow.isEmpty() && state.currentPlayer.x == state.currentPlayer.xTarget && state.currentPlayer.y == state.currentPlayer.y) {
            worldHandler.searchRoute(state.currentPlayer.x / 32, state.currentPlayer.y / 32, state.currentPlayer.xTarget = randInt(0, 40), state.currentPlayer.yTarget = randInt(0, 8));
            game.newMoveCommandIssued = true;
        }
    }

    @Override
    public void render() {

        update();
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

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

        //simulatePlayer();
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

        buildDecalBatch(decalBatch, pelaajat);

        decalBatch.flush();

        //camController.update();
        modelBatch.begin(cam);

        for (ModelInstance instance : instances) {
            modelBatch.render(instance, environment);
        }

        modelBatch.end();

    }

    float asd = 110;
    float omg = 0;
    int a = 1;
    int b = 1;

    float CAM_PATH_RADIUS = 250f;
    static float CAM_HEIGHT = 0;
    float camPathAngle = 10;

    void updateTreeCamera(Vector3 treeCenterPosition) {
        Vector3 camPosition = cam.position;

        camPosition.set(CAM_PATH_RADIUS, CAM_HEIGHT, 0); //Move camera to default location on circle centered at origin
        camPosition.rotate(Vector3.Y, camPathAngle); //Rotate the position to the angle you want. Rotating this vector about the Y axis is like walking along the circle in a counter-clockwise direction.
        camPosition.add(treeCenterPosition); //translate the circle from origin to tree center
        cam.up.set(Vector3.Y); //Make sure camera is still upright, in case a previous calculation caused it to roll or pitch
        cam.lookAt(treeCenterPosition);
        cam.update(); //Register the changes to the camera position and direction
    }

    int angle = 89;
    int suunta = 1;

    public void buildDecalBatch(DecalBatch batch, ConcurrentHashMap<String, Player> pelaajat) {
        updateTreeCamera(new Vector3(state.currentPlayer.x, state.currentPlayer.camHeight, -state.currentPlayer.y));

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camPathAngle += 0.5f;
        }
        if (camPathAngle >= 360) {
            camPathAngle = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            angle += suunta;
        }

        if (angle >= 90 || angle <= 30) {
            suunta = -suunta;
        }

        CAM_HEIGHT = (float) Math.sin(Math.toRadians(angle)) * (300);
        CAM_PATH_RADIUS = (float) Math.cos(Math.toRadians(angle)) * 300;

        instances.clear();

        for (int x = Math.max(state.currentPlayer.x / 32 - 16, 0); x < Math.min(state.currentPlayer.x / 32 + 14, 319); x++) {
            for (int y = Math.max(state.currentPlayer.y / 32 - 16, 0); y < Math.min(state.currentPlayer.y / 32 + 14, 319); y++) {
                if (worldHandler.getMap()[x][y].walkable) {
                    ModelInstance rofl = worldHandler.getMap()[x][y].getOrCreateModel(modelBuilder, worldHandler, instances);
                    instances.add(rofl);
                }
            }
        }

        for (Player p : pelaajat.values()) {
            ModelInstance rofl = new ModelInstance(model);

            float height1 = worldHandler.getMap()[p.x / 32][p.y / 32].z;
            float height2 = worldHandler.getMap()[p.x / 32 + 1][p.y / 32].z;

            float test = (float) p.x / 32;
            test -= (int) test;

            height1 = height1 * (1 - test);
            height2 = height2 * test;

            float height3 = worldHandler.getMap()[p.x / 32][p.y / 32 + 1].z;
            float height4 = worldHandler.getMap()[p.x / 32 + 1][p.y / 32 + 1].z;

            float test2 = (float) p.x / 32;
            test2 -= (int) test2;

            height3 = height3 * (1 - test2);
            height4 = height4 * test2;

            float height5 = height1 + height2;
            float height6 = height3 + height4;

            float test3 = (float) p.y / 32;
            test3 -= (int) test3;

            height5 = height5 * (1 - test3);
            height6 = height6 * test3;

            if (p.name.equals(state.currentPlayer.name)) {
                state.currentPlayer.camHeight = height5 + height6;
            }
            System.out.println("");

            
            boolean changed = false;
            System.out.println(p.targetRotation);
            if (p.targetRotation == 0 && p.rotation != 180 && !changed) {
                if (p.rotation < 180) {
                    p.rotation += 6;
                } else {
                    p.rotation -= 6;
                }
                changed = true;
            }

            if (p.targetRotation == 1 && p.rotation != 0 && !changed) {
                if (p.rotation > 180) {
                    p.rotation += 6;
                } else {
                    p.rotation -= 6;
                }
                changed = true;
            }

            if (p.targetRotation == 2 && p.rotation != 270 && !changed) {
                if (p.rotation > 90 && p.rotation < 270) {
                    p.rotation += 6;
                } else {
                    p.rotation -= 6;
                }
                changed = true;
            }
            if (p.targetRotation == 3 && p.rotation != 90 && !changed) {
                if (p.rotation > 270 || p.rotation < 90) {
                    p.rotation += 6;
                } else {
                    p.rotation -= 6;
                }
                changed = true;
            }

            if (p.rotation < 0) {
                p.rotation = 360 + p.rotation;
            }
            if (p.rotation >= 360) {
                p.rotation = p.rotation - 360;
            }

            rofl.transform.rotate(Vector3.Y, p.rotation);
            rofl.transform.setTranslation(p.x, height5 + height6, -p.y);
            instances.add(rofl);
        }

    }

}
