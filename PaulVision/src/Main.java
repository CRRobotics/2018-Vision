import org.opencv.core.*;
import org.opencv.core.Point;
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
                    List<MatOfPoint> convexHulls = cog.convexHullsOutput();
                    for(MatOfPoint hull : convexHulls) {
                        MatOfPoint2f hp = new MatOfPoint2f();
                        hull.convertTo(hp, CvType.CV_32F);
                        double len = Imgproc.arcLength(hp, true);
                        MatOfPoint2f new_curve = new MatOfPoint2f();
//                        System.out.println(len*.005);
                        Imgproc.approxPolyDP(hp, new_curve, len * .005, true);
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
                    Rect rect = null;
                    Point[] ap;
                    for(MatOfPoint cont : contours){
                        mop2f = new MatOfPoint2f();
                        mop2fApprox = new MatOfPoint2f();
                        mop = new MatOfPoint();
                        cont.convertTo(mop2f, CvType.CV_32FC2);
                        Imgproc.approxPolyDP(mop2f, mop2fApprox, Imgproc.arcLength(mop2f, true)*0.5, true);
                        mop2f.convertTo(mop, CvType.CV_32S);
                        rect = Imgproc.boundingRect(mop);
                        ap = mop2fApprox.toArray();
                        System.out.println(ap.length);
                    }
                    Imgproc.drawContours(f2, convexHulls, -1, new Scalar(0, 0, 255));
                    if(rect != null) {
                        Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
                        System.out.println(QuickMath.getAngle(rect.x + rect.width));
                        System.out.println(QuickMath.getDistance(rect.width));
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
