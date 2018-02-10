public class QuickMath {

    private static double portionOfPerim = Constants.KNOWNLENGTH/(Constants.KNOWNLENGTH + Constants.KNOWNWIDTH);

    public static double angle(int x){
        return  (x - Constants.MAXPIX/2) * Constants.FOV/Constants.MAXPIX;
    }

    public static double distance(int measuredPix) {
        return Constants.KNOWNLENGTH * Constants.FL / measuredPix;
    }

    public static double getAngleOfStrips(){ return angle((int)((CamCapture.rect1.center.x + CamCapture.rect2.center.x)/2 + .5)); }

    public static double getAngleOfCube(){ return angle((int)CamCapture.cubeCenter.x); }

    public static double getDistanceOfStrips(){ return distance((int)(((CamCapture.rect1.size.width + CamCapture.rect1.size.height ) * portionOfPerim) + .5)); }
}
