import com.sun.javafx.geom.Vec2d;

/**
 * Created by s401321 on 28/02/2017.
 */
public class Vector2 extends Vec2d {
    public Vector2(double x, double y) {
        super(x, y);
    }

    public Vector2 rotate(double theta) {
        double x = this.x;
        double y = this.y;
        this.x = x * Math.cos(theta) - y * Math.sin(theta);
        this.y = x * Math.sin(theta) + y * Math.cos(theta);
        return this;
    }

    public Vector2 translate(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

}
