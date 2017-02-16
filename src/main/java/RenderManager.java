import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

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
        glfwWindowHint(GLFW_FLOATING, GLFW_TRUE);
        window = glfwCreateWindow(Game.windowX, Game.windowY, "Hello World!", Game.fullscreen ? monitor : NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                Game.stop();
            }
        });
        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE) {
                Game.stop();
            }
        });
        glfwSetCursorPosCallback(window, (window,x,y) -> {
            Game.mouseX = x;
            Game.mouseY = y;
            Game.mouseXAdj = -1+2*Game.mouseX/Game.windowX;
            Game.mouseYAdj = -(-1+2*Game.mouseY/Game.windowY);
        });
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); //v-sync
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
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
            glfwSetWindowPos(window, videomode.width()/2-Game.windowX/2,videomode.height()/2-Game.windowY/2);
            glfwPollEvents();
        }
    }

    public static void stop() {
        glfwSetWindowShouldClose(window, true);
    }

    private static void drawNewBuffer() {

        glColor3f(1.0f, 0.0f, 1.0f);
        glPushMatrix();
        glTranslated(Game.mouseXAdj,Game.mouseYAdj,0);
        glBegin(GL_POLYGON);
        glVertex3f(0f, 0f, 0f);
        glVertex3f(0.1f, 0f, 0f);
        glVertex3f(0.05f, -0.1f, 0f);
        glEnd();
        glPopMatrix();


        EntityManager.enemies.forEach(RenderManager::drawEnemy);
    }

    private static void drawEnemy(EntityManager.Enemy enemy) {
        double x = enemy.x;
        double y = enemy.y;
        double size = Math.sqrt(enemy.size) * 0.1;
        glColor3f(1.0f, 0.0f, 0.0f);
        glPushMatrix();
        glTranslated(x,y,0);
        glRotatef(-90f + (float) Math.toDegrees(Math.atan2(Game.mouseYAdj-y,Game.mouseXAdj-x)),0,0,1);
        glBegin(GL_POLYGON);
        glVertex3d(-size, 0, 1f);
        glVertex3d(size, 0 , 1f);
        glVertex3d(0,  size, 1f);
        glEnd();
        glPopMatrix();
    }
}