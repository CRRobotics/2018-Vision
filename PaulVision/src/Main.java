import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import java.util.ArrayList;
import org.opencv.imgproc.*;



public class Main {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    static public class Line {
        Point a;
        Point b;
        double len_2;

        public Line(Point a, Point b, double len_2) {
            this.a = a;
            this.b = b;
            this.len_2 = len_2;
        }
    }

    public static void main(String args[]) throws InterruptedException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        boolean isRunning = true;
        double s = 1.52565555;


        PaulPipeline cog = new PaulPipeline();
        System.out.print(cog.convexHullsOutput());
        VideoCapture camera = new VideoCapture(1);
        camera.set(15,-1);


        Mat frame = new Mat();
        Mat f2 = null;
        camera.read(frame);
        //if(camera.read() = true) System.out.println();

        WindowWithImage window2 = new WindowWithImage("wow2", 700, 0);
        WindowWithImage window = new WindowWithImage("wow", 0, 0);
        //Timer timer = new Timer();
        // Timer.schedule(new FPS(frames), 0, 5000);
//        while (!window.isOpen()) continue;
        while (window.isOpen()) {
            //System.out.println(window.isOpen());
            if (!camera.isOpened()) {
                throw new RuntimeException("Could not open camera!");
            } else {
                if (camera.read(frame)) {
                    f2 = Mat.zeros(frame.rows(), frame.cols(), CvType.CV_8UC3);
                    cog.process(frame);
                    RotatedRect[] rects;
                    ArrayList<MatOfPoint> contours = cog.findContoursOutput();
                    MatOfPoint2f approxCurve = new MatOfPoint2f();
                    rects = new RotatedRect[contours.size()];
                    //For each contour found
                    for (int i=0; i<contours.size(); i++)
                    {
                        Imgproc.drawContours(f2, contours, i, new Scalar(0, 255, 0));
                        //Convert contours(i) from MatOfPoint to MatOfPoint2f
                        MatOfPoint2f contour2f = new MatOfPoint2f( contours.get(i).toArray() );
                        //Processing on mMOP2f1 which is in type MatOfPoint2f
                        double approxDistance = Imgproc.arcLength(contour2f, true)*0.005;
                        Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

                        //Convert back to MatOfPoint
                        MatOfPoint points = new MatOfPoint( approxCurve.toArray() );

                        // Get bounding rect of contour
                        rects[i] = Imgproc.minAreaRect(approxCurve);
                        Point[] pts = new Point[4];
                        rects[i].points(pts);

                        // draw enclosing rectangle (all same color, but you could use variable i to make them unique)
                        for(int j=0; j<pts.length; ++j){
                            Imgproc.line(frame, pts[j], pts[(j+1)%pts.length], new Scalar(0,255,0));
                        }
                    }
                    if(rects != null && rects.length > 1) {
                        RotatedRect rect1 = rects[0];
                        RotatedRect rect2 = rects[1];
                        for (RotatedRect r : rects) {
                            if (r.size.height + r.size.width > rect1.size.height + rect1.size.width)
                                rect1 = r;
                        }
                        for (RotatedRect r2 : rects) {
                            if (r2.size.height + r2.size.width > rect2.size.height + rect2.size.width && !r2.equals(rect1))
                                rect2 = r2;
                        }
                        System.out.println(QuickMath.getAngle((int)((rect1.center.x + rect2.center.x)/2 + .5)));
//                        System.out.println(QuickMath.getDistance((int)(rect1.size.width + rect1.size.height + .5)));
                    }


                    window.image(frame);
                    window2.image(f2);

                }
            }
        }
        camera.release();
        System.out.println("Closed");
    }
}
