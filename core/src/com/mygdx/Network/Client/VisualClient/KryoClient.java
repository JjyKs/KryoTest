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
import java.util.concurrent.ConcurrentHashMap;

//PLACEHOLDER CLIENT FOR TESTING.
public class KryoClient extends ApplicationAdapter {

    public ModelBuilder modelBuilder = new ModelBuilder();

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

    public Environment environment;
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;
    public ModelInstance instance;
    public Decal sprite;
    public DecalBatch decalBatch;
    Texture image;

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

        final Model model = modelBuilder.createRect(
                6f,
                0f,
                -6f,
                -6f,
                0f,
                -6f,
                -6f,
                0f,
                6f,
                6f,
                0f,
                6f,
                0,
                1,
                0,
                new Material(ColorAttribute.createDiffuse(Color.GREEN), ColorAttribute.createSpecular(Color.WHITE), FloatAttribute.createShininess(16f)), Usage.Position | Usage.Normal);

        instance = new ModelInstance(model);

    }

    public void update() {
        game.update();
        state = game.getGameState();
        instance.transform.translate(0.0f, 0.0f, 0f);
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

        //camController.update();
        modelBatch.begin(cam);
        //modelBatch.render(instance, environment);
        modelBatch.end();

        buildDecalBatch(decalBatch, pelaajat);

        decalBatch.flush();

    }

    float asd = 110;
    float omg = 0;
    int a = 1;
    int b = 1;

    final float CAM_PATH_RADIUS = 180f;
    static float CAM_HEIGHT = 0;
    float camPathAngle = 10;

    void updateTreeCamera(Vector3 treeCenterPosition, float CAM_PATH_RADIUS) {
        Vector3 camPosition = cam.position;
        if (CAM_HEIGHT >= 160) {
            CAM_HEIGHT = 160;
        }
        camPosition.set(CAM_PATH_RADIUS, CAM_HEIGHT, 0); //Move camera to default location on circle centered at origin
        camPosition.rotate(Vector3.Y, camPathAngle); //Rotate the position to the angle you want. Rotating this vector about the Y axis is like walking along the circle in a counter-clockwise direction.
        camPosition.add(treeCenterPosition); //translate the circle from origin to tree center
        cam.up.set(Vector3.Y); //Make sure camera is still upright, in case a previous calculation caused it to roll or pitch
        cam.lookAt(treeCenterPosition);
        cam.update(); //Register the changes to the camera position and direction
    }

    int angle = 160;
    int suunta = 2;

    public void buildDecalBatch(DecalBatch batch, ConcurrentHashMap<String, Player> pelaajat) {
        updateTreeCamera(new Vector3(state.currentPlayer.x, 16, -state.currentPlayer.y), Math.max(CAM_PATH_RADIUS - CAM_HEIGHT, 3));

        camPathAngle++;
        if (camPathAngle >= 360) {
            camPathAngle = 0;
        }

        angle += suunta;

        if (angle > 160 || angle <= 60) {
            suunta = -suunta;
        }
        CAM_HEIGHT = angle;

        for (int x = Math.max(state.currentPlayer.x / 32 - 7, 0); x < Math.min(state.currentPlayer.x / 32 + 14, 319); x++) {
            for (int y = Math.max(state.currentPlayer.y / 32 - 7, 0); y < Math.min(state.currentPlayer.y / 32 + 14, 319); y++) {
                if (worldHandler.getMap()[x][y].walkable) {
                    Decal sprite;
                    sprite = Decal.newDecal(32, 32, new TextureRegion(image), true);
                    sprite.setPosition(x * 32, 0, -y * 32);
                    sprite.setRotation(0, 90, 0);
                    decalBatch.add(sprite);
                }
            }
        }

        for (Player p : pelaajat.values()) {
            Decal sprite;
            sprite = Decal.newDecal(32, 32, new TextureRegion(img), true);
            sprite.setPosition(p.x, 16, -p.y);
            sprite.lookAt(cam.position, Vector3.Y);
            decalBatch.add(sprite);

        }

    }

}
