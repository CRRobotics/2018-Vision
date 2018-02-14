import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

public class QuickMath {

    private static double lengOfCubePerim = Constants.STRIPLENGTH /(Constants.STRIPLENGTH + Constants.STRIPWIDTH);

    public static double hAngle(int x){ return  (x - Constants.HMAXPIX /2) * Constants.HFOV /Constants.HMAXPIX; }

    public static double vAngle(int y){ return  (y - Constants.VMAXPIX /2) * Constants.VFOV /Constants.VMAXPIX; }

    public static double distance(int measuredPix, double knownLength) { return knownLength * Constants.FL / measuredPix; }

    public static double triArea(Point a, Point b, Point c){ return Math.abs(a.x*(b.y - c.y)+b.x*(c.y - a.y)+c.x*(a.y - b.y))/2; }

    public static double polyArea(List<Point> pts) {
        if (pts.size() < 3) return 0;
        ArrayList<Point> npts =  new ArrayList<>(pts);
        npts.remove(npts.size() - 1);
        return triArea(pts.get(pts.size() - 2), pts.get(pts.size() - 1), pts.get(0)) + polyArea(npts);
    }

    public static double getAngleOfStrips(){ return hAngle((int)((CamCapture.rect1.center.x + CamCapture.rect2.center.x)/2 + .5)); }

    public static double gethAngleOfCube(){ return hAngle((int)CamCapture.cubeCenter.x); }

    public static double getvAngleOfCube(){ return vAngle((int)CamCapture.cubeCenter.y); }

    public static double getDistanceOfStrips(){
        return distance((int)(((CamCapture.rect1.size.width + CamCapture.rect1.size.height ) * lengOfCubePerim) + .5), Constants.STRIPLENGTH);
    }

}
