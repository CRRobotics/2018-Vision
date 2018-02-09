public class QuickMath {
    private static double portionOfPerim = Constants.KNOWNLENGTH/(Constants.KNOWNLENGTH + Constants.KNOWNWIDTH);
    public static double getAngle(int x){
        return  (x - Constants.MAXPIX/2) * Constants.FOV/Constants.MAXPIX;
    }

    public static double getDistance(int measuredPix) {
        return Constants.KNOWNLENGTH * Constants.FL / (measuredPix * portionOfPerim);
    }
}
