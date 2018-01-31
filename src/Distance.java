
public class Distance {

    //Variable
    private static double FOV = 60;
    //distance from object

    public double getFOV(){return FOV;}
    public void setFOV(double newFOV){FOV = newFOV;}

    public static double getDistance(double knownLength, int measuredPix, int maxPix) {
        return (Math.sin(90-((measuredPix/2)*(FOV/maxPix)))*knownLength/2)/Math.sin((measuredPix/2)*(FOV/maxPix));
    }

}