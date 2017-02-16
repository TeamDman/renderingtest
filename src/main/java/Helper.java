/**
 * Created by User on 16/02/2017.
 */
public class Helper {
    public static boolean isNear(double check, double near, double dist) {
        return Math.abs(check-near) <= dist;
    }
}
