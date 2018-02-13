import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.videoio.VideoCapture;

import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.util.ArrayList;
import java.util.Arrays;

public class CamCapture {

    public static RotatedRect rect1;
    public static RotatedRect rect2;
    public static Point cubeCenter;

    /**
     * this is the main part of vision that MUST be running for vision to actually work
     * @param args
     */

    public static void main(String[] args) {

        //load opencv native library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //detect camera
        VideoCapture camera = new VideoCapture(0);
        camera.set(15, -1);
        cubeCenter = new Point();
//        String face_cascade_name = "haarcascade_frontalface_alt.xml";
//        String eyes_cascade_name = "haarcascade_eye_tree_eyeglasses.xml";
//        CascadeClassifier face_cascade = new CascadeClassifier();
//        CascadeClassifier eyes_cascade = new CascadeClassifier();
//        String window_name = "Capture - Face detection.jpg";
//
//        System.out.println("capture through camera " + Core.VERSION);
//
//
//        //load the face xml cascade
//        if (!face_cascade.load(face_cascade_name)) {
//            System.out.println("Error loading face cascade");
//        } else {
//            System.out.println("Success loading face cascade");
//        }
//
//        //load the eyes xml cascade
//        if (!eyes_cascade.load(eyes_cascade_name)) {
//            System.out.println("Error loading eyes cascade");
//        } else {
//            System.out.println("Success loading eyes cascade");
//        }
//
//        if (!camera.isOpened()) {
//            System.out.println("Did not connected to camera.");
//        } else {
//            System.out.println("Conected to camera: " + camera.toString());
//        }
//
//        //create new Mat image
//        Mat frame = new Mat();
//        camera.retrieve(frame);
//
//        Mat frame_gray = new Mat();
//        Imgproc.cvtColor(frame, frame_gray, Imgproc.COLOR_BGRA2GRAY);
//        Imgproc.equalizeHist(frame_gray, frame_gray);
//
//
//        MatOfRect faces = new MatOfRect();
//
//        face_cascade.detectMultiScale(frame_gray, faces, 1.1, 2, 0, new Size(30, 30), new Size());
//
//
//        Rect[] facesArray = faces.toArray();
//
//        for (int i = 0; i < facesArray.length; i++) {
//            Point center = new Point(facesArray[i].x + facesArray[i].width * 0.5, facesArray[i].y + facesArray[i].height * 0.5);
//            //Core.ellipse(frame, center, new Size(facesArray[i].width * 0.5, facesArray[i].height * 0.5), 0, 0, 360, new Scalar(255, 0, 255), 4, 8, 0);
//
//            Mat faceROI = frame_gray.submat(facesArray[i]);
//            MatOfRect eyes = new MatOfRect();
//
//            //-- In each face, detect eyes
//            eyes_cascade.detectMultiScale(faceROI, eyes, 1.1, 2, 0, new Size(30, 30), new Size());
//
//            Rect[] eyesArray = eyes.toArray();
//
//            for (int j = 0; j < eyesArray.length; j++) {
//                Point center1 = new Point(facesArray[i].x + eyesArray[i].x + eyesArray[i].width * 0.5, facesArray[i].y + eyesArray[i].y + eyesArray[i].height * 0.5);
//                int radius = (int) Math.round((eyesArray[i].width + eyesArray[i].height) * 0.25);
//                //Core.circle(frame, center1, radius, new Scalar(255, 0, 0), 4, 8, 0);
//            }
//        }
        GripPipeline cog = new GripPipeline();
        GripPipeline cog2 = new GripPipeline();
//        System.out.print(cog.convexHullsOutput());

        Mat frame = new Mat();
//        Mat f2 = null;
        camera.read(frame);

        ImageWindow window = new ImageWindow("wow", 150, 100);
//        ImageWindow window2 = new ImageWindow("wow2", 700, 0);

        while (window.isOpen()) {
            //System.out.println(window.isOpen());
            if (!camera.isOpened()) {
                throw new RuntimeException("Could not open camera!");
            } else {
                if (camera.read(frame)) {
//                    f2 = Mat.zeros(frame.rows(), frame.cols(), CvType.CV_8UC3);
                    cog.process(frame);
                    RotatedRect[] rects;
                    ArrayList<MatOfPoint> hulls = cog.convexHullsOutput();
                    MatOfPoint2f approxCurve = new MatOfPoint2f();
                    rects = new RotatedRect[hulls.size()];
                    //For each contour found
                    for (int i=0; i<hulls.size(); i++)
                    {
//                        Imgproc.drawContours(f2, hulls, i, new Scalar(0, 255, 0));
                        //Convert hulls(i) from MatOfPoint to MatOfPoint2f
                        MatOfPoint2f contour2f = new MatOfPoint2f( hulls.get(i).toArray() );
                        //Processing on mMOP2f1 which is in type MatOfPoint2f
                        double approxDistance = Imgproc.arcLength(contour2f, true)*0.005;
                        Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);


                        //make rects of contour
                        rects[i] = Imgproc.minAreaRect(approxCurve);
                        Point[] pts = new Point[4];
                        rects[i].points(pts);

                        // draw enclosing rectangle (all same color, but you could use variable i to make them unique)
                        for(int j=0; j<pts.length; ++j){
                            Imgproc.line(frame, pts[j], pts[(j+1)%pts.length], new Scalar(0,255,0));
                        }
                    }
                    if(rects != null && rects.length > 1) {
                        rect1 = rects[0];
                        rect2 = rects[1];
                        for (RotatedRect r : rects) {
                            if (r.size.height + r.size.width > rect1.size.height + rect1.size.width)
                                rect1 = r;
                        }
                        for (RotatedRect r2 : rects) {
                            if (r2.size.height + r2.size.width > rect2.size.height + rect2.size.width && !r2.equals(rect1))
                                rect2 = r2;
                        }
//                        System.out.println(QuickMath.getAngleOfStrips());
//                        System.out.println(QuickMath.getDistanceOfStrips());
                    }

                    /*
                     this section of code is implementation of vision for the cube
                     it is VERY IMPORTANT TO NOTE THAT I didnt use grip to generate the hues
                     I just kinda eyeballed the values that are in Process2 however if Grip is used
                     to edit the existing pipeline there will be errors because I changes that file
                     to allow the processing for different hues
                     */
                    cog2.process2(frame);
                    ArrayList<MatOfPoint> hulls2 = cog2.convexHullsOutput();
                    
                    int bigI = 0;
                    if(hulls2.size() > 0) {
                        for(int i = 0; i < hulls2.size(); i++)
                            if(QuickMath.polyArea(hulls2.get(bigI).toList()) < QuickMath.polyArea(hulls2.get(i).toList()))
                                bigI = i;
                        Point[] cubePoints = hulls2.get(bigI).toArray();
                        double sumX = 0;
                        double sumY = 0;
                        for (Point p : cubePoints) {
                            sumX += p.x;
                            sumY += p.y;
                        }
                        sumX /= cubePoints.length;
                        sumY /= cubePoints.length;
                        cubeCenter.x = sumX;
                        cubeCenter.y = sumY;

                        Imgproc.drawContours(frame, hulls2, 0, new Scalar(0, 0, 255));
                        System.out.println(QuickMath.getAngleOfCube());
                    }

                    window.image(frame);
//                    window2.image(f2);

                }
            }
        }

        //Highgui.imwrite(window_name, frame);
        camera.release();
        System.out.println("Closed");

    }

}