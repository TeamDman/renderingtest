
import java.util.Random;
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

    private class Config {
        int keyLeft = GLFW_KEY_A;
        int keyRight = GLFW_KEY_D;
    }

    public static void run() {
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
        EntityManager.enemies.forEach(e -> e.moveTowards(Game.mouseXAdj, Game.mouseYAdj, 2));
        EntityManager.dirty.forEach(EntityManager.enemies::remove);
    }

    public static void stop() {
        RenderManager.stop();
        running = false;
    }

    public static void main(String[] args) {
        run();
    }

}