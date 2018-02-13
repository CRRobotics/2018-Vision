import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

public class QuickMath {

    private static double portionOfPerim = Constants.KNOWNLENGTH/(Constants.KNOWNLENGTH + Constants.KNOWNWIDTH);

    public static double angle(int x){ return  (x - Constants.MAXPIX/2) * Constants.FOV/Constants.MAXPIX; }

    public static double distance(int measuredPix) { return Constants.KNOWNLENGTH * Constants.FL / measuredPix; }

    public static double triArea(Point a, Point b, Point c){ return Math.abs(a.x*(b.y - c.y)+b.x*(c.y - a.y)+c.x*(a.y - b.y))/2; }

    public static double polyArea(List<Point> pts) {
        if (pts.size() < 3) return 0;
        ArrayList<Point> npts =  new ArrayList<>(pts);
        npts.remove(npts.size() - 1);
        return triArea(pts.get(pts.size() - 2), pts.get(pts.size() - 1), pts.get(0)) + polyArea(npts);
    }

    public static double getAngleOfStrips(){ return angle((int)((CamCapture.rect1.center.x + CamCapture.rect2.center.x)/2 + .5)); }

    public static double getAngleOfCube(){ return angle((int)CamCapture.cubeCenter.x); }

    public static double getDistanceOfStrips(){ return distance((int)(((CamCapture.rect1.size.width + CamCapture.rect1.size.height ) * portionOfPerim) + .5)); }
}
