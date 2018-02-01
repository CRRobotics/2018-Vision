
public class Distance {
    public static double getDistance(double knownLength, int measuredPix, int maxPix, double FOV) {
        return (Math.sin(90-((measuredPix/2)*(FOV/maxPix)))*knownLength/2)/Math.sin((measuredPix/2)*(FOV/maxPix));
    }
}