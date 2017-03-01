import com.sun.javafx.geom.Vec2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static org.lwjgl.glfw.GLFW.*;

public class Game {
    public static double mouseX, mouseY;
    public static double mouseXAdj, mouseYAdj;
    public static int windowX = 500;
    public static int windowY = 500;
    public static boolean fullscreen = false;
    public static boolean running = true;
    public static boolean allowCombine = false;
    public static ArrayList<Vec2d> vertices;
    public static HashMap<Key, Boolean> controls = new HashMap<>();

    static {
        for (Key key : Key.values()) {
            controls.put(key, false);
        }
    }

    public static void run() {
        EntityManager.spawnNewPlayer();
        for (int i = 0; i < 10; i++)
            EntityManager.spawnNewEnemy();

        new Thread(RenderManager::init).start();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!running)
                    System.exit(1);
                loop();
            }
        }, 2000, 20);
    }

    public static void loop() {
        EntityManager.Player player = EntityManager.player;
        EntityManager.enemies.forEach(e -> e.moveTowards(player));
        player.move(player.nextX, player.nextY);

        EntityManager.dirty.forEach(EntityManager.enemies::remove);
        EntityManager.dirty.forEach(EntityManager.lasers::remove);
        EntityManager.dirty.clear();
        //TODO: Minkowski difference for collision detection
        //TODO: http://www.metanetsoftware.com/technique/tutorialA.html#section1
    }

    public static void stop() {
        RenderManager.stop();
        running = false;
    }

    public static void onKeyboard(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
            stop();
        }
        controls.put(Key.getFromId(key), action != GLFW_RELEASE);
        EntityManager.Player player = EntityManager.player;
        player.nextX = 0;
        player.nextY = 0;

        if (controls.get(Key.LEFT))
            player.nextX = -1;
        if (controls.get(Key.RIGHT))
            player.nextX = 1;
        if (controls.get(Key.DOWN))
            player.nextY = -1;
        if (controls.get(Key.UP))
            player.nextY = 1;

    }

    public static void onMouse(long window, int button, int action, int mods) {
        if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE) {
//            stop();
            EntityManager.player.pew(mouseXAdj, mouseYAdj);
        }
    }

    public static void onMouseMove(long window, double x, double y) {
        mouseX = x;
        mouseY = y;
        mouseXAdj = -1 + 2 * mouseX / windowX;
        mouseYAdj = -(-1 + 2 * mouseY / windowY);
    }

    public static void main(String[] args) {
        run();
    }

    public enum Key {
        LEFT(GLFW_KEY_A),
        RIGHT(GLFW_KEY_D),
        UP(GLFW_KEY_W),
        DOWN(GLFW_KEY_S);

        int id;

        Key(int id) {
            this.id = id;
        }

        static Key getFromId(int id) {
            for (Key key : values()) {
                if (key.id == id)
                    return key;
            }
            return null;
        }
    }

    private class Config {
        int keyLeft = GLFW_KEY_A;
        int keyRight = GLFW_KEY_D;
    }

}