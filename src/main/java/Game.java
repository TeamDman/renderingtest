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
        player.moveTowards(player.x + player.nextX, player.y + player.nextY);

        EntityManager.dirty.forEach(EntityManager.enemies::remove);
        EntityManager.dirty.forEach(EntityManager.lasers::remove);
        EntityManager.dirty.clear();
        //TODO: Minkowski difference for collision detection
    }

    public static void stop() {
        RenderManager.stop();
        running = false;
    }

    public static void onKeyboard(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
            stop();
        }
        EntityManager.Player player = EntityManager.player;
        if (key == GLFW_KEY_A)
            player.nextX = -1;
        if (key == GLFW_KEY_D)
            player.nextX = 1;
        if (key == GLFW_KEY_S)
            player.nextY = -1;
        if (key == GLFW_KEY_W)
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

    private class Config {
        int keyLeft = GLFW_KEY_A;
        int keyRight = GLFW_KEY_D;
    }

}