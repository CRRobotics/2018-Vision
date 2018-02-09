import javafx.geometry.BoundingBox;
import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
//import org.opencv.highgui.ImageWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
                    List<MatOfPoint> convexHulls = cog.convexHullsOutput();
                    for(MatOfPoint hull : convexHulls) {
                        MatOfPoint2f hp = new MatOfPoint2f();
                        hull.convertTo(hp, CvType.CV_32F);
                        MatOfPoint2f new_curve = new MatOfPoint2f();
                        Imgproc.approxPolyDP(hp, new_curve, Imgproc.arcLength(hp, true) * .005, true);
                        MatOfPoint int_new_curve = new MatOfPoint();
                        new_curve.convertTo(int_new_curve, CvType.CV_32S);
                        Imgproc.drawContours(f2, Arrays.asList(int_new_curve), 0, new Scalar(0, 255, 0));
                    }
                    /**
                     * roary's attempt to print only the pixel length of the longest line
                     */
                    ArrayList<MatOfPoint> contours = cog.findContoursOutput();

                    MatOfPoint2f mop2fApprox;
                    MatOfPoint2f mop2f;
                    MatOfPoint mop;

                    Point[] ap;
                    double longest = 0;
                    MatOfPoint2f         approxCurve = new MatOfPoint2f();
                    rects = new RotatedRect[contours.size()];
                    //For each contour found
                    for (int i=0; i<contours.size(); i++)
                    {
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
                    if(rects != null && rects.length > 2) {
                        RotatedRect rect1 = rects[0];
                        RotatedRect rect2 = rects[1];
                        for (RotatedRect r : rects) {
                            if (r.size.height + r.size.width > rect1.size.height + rect1.size.width)
                                rect1 = r;
                        }
                        for (RotatedRect r2 : rects) {
                            if (r2.size.height + r2.size.width > rect2.size.height + rect2.size.width && !r2.equals(rect1))
                                rect1 = r2;
                        }
//                        System.out.println(QuickMath.getAngle((int)((rect1.center.x + rect2.center.x)/2 + .5)));
                        if(rect1.size.height > rect1.size.width)
                            System.out.println(QuickMath.getDistance((int)(rect1.size.height + .5)));
                        else
                            System.out.println(QuickMath.getDistance((int)(rect1.size.width + .5)));
                    }

//                    for(MatOfPoint cont : contours) {
//                        mop2f = new MatOfPoint2f();
//                        mop2fApprox = new MatOfPoint2f();
//                        mop = new MatOfPoint();
//                        cont.convertTo(mop2f, CvType.CV_32FC2);
//                        Imgproc.approxPolyDP(mop2f, mop2fApprox, Imgproc.arcLength(mop2f, true) * 0.01, true);
//                        mop2f.convertTo(mop, CvType.CV_32S);

//                        ap = mop2fApprox.toArray();
////                        double current = rect.height;
////                        if (current > longest)
////                            longest = current;
////                        System.out.println(ap.length);
//                    }
//                    if(rect != null) {
//                        Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
////                        System.out.println(QuickMath.getAngle(rect.x + rect.width/2));
////                        System.out.println(QuickMath.getDistance(rect.width));
////                        System.out.println(longest);
//                    }

                    window.image(frame);
                    window2.image(f2);

                }
            }
        }
        camera.release();
        System.out.println("Closed");
    }
}
