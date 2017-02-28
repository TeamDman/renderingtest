import com.sun.javafx.geom.Vec2d;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by User on 14/02/2017.
 */
public class RenderManager {
    private static long window;
    private static GLFWVidMode videomode;
    private static long monitor;

    public static void init() {
        System.out.println("Using LWJGL " + Version.getVersion() + "!");

        load();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private static void load() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        monitor = glfwGetPrimaryMonitor();
        videomode = glfwGetVideoMode(monitor);
        if (Game.fullscreen) {
            Game.windowX = videomode.width();
            Game.windowY = videomode.height();
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
//        glfwWindowHint(GLFW_FLOATING, GLFW_TRUE);
        window = glfwCreateWindow(Game.windowX, Game.windowY, "Hello World!", Game.fullscreen ? monitor : NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(window, Game::onKeyboard);
        glfwSetMouseButtonCallback(window, Game::onMouse);
        glfwSetCursorPosCallback(window, Game::onMouseMove);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); //v-sync
//        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        glfwShowWindow(window);


    }

    private static void loop() {
        GL.createCapabilities();
        glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            drawNewBuffer();

            glfwSwapBuffers(window); // swap the color buffers
//            glfwSetWindowPos(window, (int) (Math.sin(System.currentTimeMillis() / 100.0) * 50) + 500, 500);//(int) Math.cos(System.currentTimeMillis()*100)*500+500);
            glfwSetWindowPos(window, videomode.width() / 2 - Game.windowX / 2, videomode.height() / 2 - Game.windowY / 2);
            glfwPollEvents();
        }
    }

    public static void stop() {
        glfwSetWindowShouldClose(window, true);
    }

    private static void drawNewBuffer() {

        glColor3f(1.0f, 0.0f, 1.0f);
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        drawCrosshair();

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        drawPlayer();


        glColor3f(1.0f, 0.0f, 0.0f);
        EntityManager.enemies.forEach(RenderManager::drawEnemy);

        glColor3f(1f, 0.1f, 0.1f);
        EntityManager.lasers.forEach(RenderManager::drawLaser);
    }

    private static void drawEnemy(EntityManager.Enemy enemy) {
        double size = Math.min(enemy.size / 10d, 0.2);
        double x = enemy.x;
        double y = enemy.y;
        double theta = -Math.PI / 2 + Math.atan2(EntityManager.player.y - y, EntityManager.player.x - x);
//        double theta = -Math.PI/2 +  Math.atan2(Game.mouseYAdj - y, Game.mouseXAdj - x);
        Vector2 a = new Vector2(-size, 0).rotate(theta).translate(x, y);
        Vector2 b = new Vector2(size, 0).rotate(theta).translate(x, y);
        Vector2 c = new Vector2(0, size).rotate(theta).translate(x, y);

        glBegin(GL_POLYGON);
        {
            vert2d(a);
            vert2d(b);
            vert2d(c);
        }
        glEnd();
    }

    private static void vert2d(Vec2d vec) {
        GL11.glVertex2d(vec.x, vec.y);
    }

    private static void drawPlayer() {
        double size = 0.1;
        double x = EntityManager.player.x;
        double y = EntityManager.player.y;
        glPushMatrix();
        glTranslated(x, y, 0);
        glRotatef(-90f + (float) Math.toDegrees(Math.atan2(Game.mouseYAdj - y, Game.mouseXAdj - x)), 0, 0, 1);
        glBegin(GL_POLYGON);
        {
            glVertex2d(-size, 0);
            glVertex2d(size, 0);
            glVertex2d(0, size);
        }
        glEnd();
        glPopMatrix();
    }

    private static void drawCrosshair() {
        double size = 0.05;
        glPushMatrix();
        glTranslated(Game.mouseXAdj - size / 2, Game.mouseYAdj - size / 2, 1);
        glBegin(GL_POLYGON);
        {
            glVertex2d(0, 0);
            glVertex2d(size, 0);
            glVertex2d(size, size);
            glVertex2d(0, size);
        }
        glEnd();
        glPopMatrix();
    }

    private static void drawLaser(EntityManager.Laserbeam laser) {
        glLineWidth(laser.lifecycle--);
        glBegin(GL_LINES);
        {
            glVertex2d(laser.x, laser.y);
            glVertex2d(laser.ax, laser.ay);
        }
        glEnd();
        if (laser.lifecycle <= 0)
            EntityManager.dirty.add(laser);
    }
}
