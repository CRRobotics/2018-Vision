/**
 * ALL OF THIS IS TEMPORARY AND NOT FINAL IN THE SLIGHTEST
 * Also I'm pretty sure that this doesn't actually work
 * @author Aidan Foley
 */

public Class Trigonometry
{
  
  /**
   * Returns the angle, in degrees, between the center1 and center2
   * Currently disregards FOV
   * @param center1 The center of the first strip of tape
   * @param center2 The center of the second strip of tape
   * @return The angle, in degrees, between center1 and center2
   */ 
  public static double getAngle(int center1, center2)
  {
    return Math.atan(center1 / center2);
  }
  
  /**
   * Returns the length to the point between two strips of tape
   * @param separation The separation bewteen the centers of the two strips
   * of tape
   * @param angle The angle between the centers of the two pieces of tape
   * @return The distance (in who knows what unit) to the center
   */
  public static double getLength(int separation, angle)
  {
    return separation / Math.tan(angle);
  }
  
  /**
   * Returns the length to the center of center1 and center2
   * @param center1 The center of the first strip of tape
   * @param center2 The center of the second strip of tape
   * @return The distance (in who knows what unit) to the center
   */
  public static double getLengthToCenter(int center1, int center2)
  {
    double angle = getAngle(center1, center2);
    return getLength(Math.abs(center2 - center1), angle);
  }
  
}
