import java.util.ArrayList;

/**
 * Created by User on 13/02/2017.
 */
public class EntityManager {
    public static ArrayList<Enemy> enemies = new ArrayList<>();
    public static ArrayList<Enemy> dirty = new ArrayList<>();

    public static void spawnNewEnemy() {
        enemies.add(new Enemy());
    }

    public static class Enemy {
        public static double distToJoin = 0.01;
        public static double distToKill = 0.05;
        public static double speed = 0.01;
        double x;
        double y;
        int size;
        int id;

        public Enemy() {
            this.x = (Math.random() - 0.5) * 2;//Game.windowX/2);
            this.y = (Math.random() - 0.5) * 2;//Game.windowY/2);
            this.size = 1;
            this.id=EntityManager.enemies.size();
        }

        public void moveTowards(double x, double y, int step) {
//            this.x += Math.log10((x-this.x)*2/this.size+1)*0.1;
//            this.y += Math.log10((y-this.y)*2/this.size+1)*0.1;
            double deltax = x - this.x;
            double deltay = y - this.y;
            double deltahyp = Math.sqrt(x * x + y * y);
            this.x += deltax / deltahyp * speed;
            this.y += deltay / deltahyp * speed;
            if (Helper.isNear(this.x, x, distToKill) && Helper.isNear(this.y, y, distToKill)) {
                System.out.println("dead");
                System.exit(1);
            }
        }

        public boolean isNear(Enemy e) {
            if (this != e && Helper.isNear(e.x, this.x, distToJoin) && Helper.isNear(e.y, this.y, distToJoin)) {
                return true;
            }
            return false;
        }

        public Enemy getClosest() {
            for (Enemy enemy:enemies ) {
                if (isNear(enemy))
                    return enemy;
            }
            return this;
        }

        public void consume(Enemy e) {
            System.out.println("Consumed, " + this);
            this.size+=e.size;
            dirty.add(e);
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof Enemy && ((Enemy) obj).id==this.id);
        }

        @Override
        public String toString() {
            return "enemy [size: " + size + ", x: " + x + ", y: " + y + "]";
        }
    }
}
