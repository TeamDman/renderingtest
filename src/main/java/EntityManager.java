import java.util.ArrayList;

/**
 * Created by User on 13/02/2017.
 */
public class EntityManager {
    public static double distToJoin = 0.01;
    public static double distToKill = 0.05;
    public static double speed = 0.01;

    public static ArrayList<Enemy> enemies = new ArrayList<>();
    public static ArrayList<Entity> dirty = new ArrayList<>();
    public static ArrayList<Laserbeam> lasers = new ArrayList<>();
    public static Player player;

    public static void spawnNewEnemy() {
        enemies.add(new Enemy());
    }

    public static void spawnNewPlayer() {
        player = new Player();
    }

    public static class Entity {
        double x, y;

        public Entity(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public void moveTowards(Entity e) {
            moveTowards(e.x, e.y);
        }

        public void moveTowards(double x, double y) {
            System.out.println("Entity not overriding moveTowards! FIX!");
        }

        @Override
        public String toString() {
            return this.getClass().getCanonicalName() + " [x: " + x + ", y: " + y + "]";
        }
    }

    public static class Enemy extends Entity {
        int size;

        public Enemy() {
            super((Math.random() - 0.5) * 2, (Math.random() - 0.5) * 2);
            this.size = 1;
        }

        @Override
        public void moveTowards(double x, double y) {
            double deltax = x - this.x;
            double deltay = y - this.y;
            double deltahyp = Math.sqrt(deltax * deltax + deltay * deltay);
            this.x += deltax / deltahyp * speed;
            this.y += deltay / deltahyp * speed;
        }

        public void checkKill() {
            if (Helper.isNear(this.x, player.x, distToKill) && Helper.isNear(this.y, player.y, distToKill)) {
                System.out.println("dead");
                System.exit(1);
            }
        }
    }

    public static class Player extends Entity {
        double nextX, nextY = 0;

        public Player() {
            super(0, 0);
        }

        public void pew(double x, double y) {
            lasers.add(new Laserbeam(this.x, this.y, x, y));
        }

        @Override
        public void moveTowards(double x, double y) {
//            double deltax = x - this.x;
//            double deltay = y - this.y;
//            double deltahyp = Math.sqrt(x * x + y * y);
//            this.x += deltax / deltahyp * speed;
//            this.y += deltay / deltahyp * speed;
//            if (Helper.isNear(this.x, x, distToKill) && Helper.isNear(this.y, y, distToKill)) {
//                System.out.println("dead");
//                System.exit(1);
//            }
        }
    }

    public static class Laserbeam extends Entity {
        double ax;
        double ay;
        int lifecycle;

        public Laserbeam(double x, double y, double ax, double ay) {
            super(x, y);
            this.ax = ax;
            this.ay = ay;
            this.lifecycle = 20;
        }
    }
}
