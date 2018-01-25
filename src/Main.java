import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.videoio.VideoCapture;
//import org.opencv.highgui.ImageWindow;
import org.opencv.highgui.Highgui;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
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


        ContourOfGod cog = new ContourOfGod();
        System.out.print(cog.convexHullsOutput());
        VideoCapture camera = new VideoCapture(1);
        boolean TargetInrange;


        Mat frame = new Mat();
        camera.read(frame);
        //if(camera.read() = true) System.out.println();

        WindowWithImage window2 = new WindowWithImage("wow2", 700, 0);
        WindowWithImage window = new WindowWithImage("wow", 0, 0);

        double time = System.currentTimeMillis();

        long startTime = System.currentTimeMillis();

        int frames = 0;
        int timer = 0;
        int runs = 1;
        //Timer timer = new Timer();
        // Timer.schedule(new FPS(frames), 0, 5000);
//        while (!window.isOpen()) continue;
        while (window.isOpen()) {
            //System.out.println(window.isOpen());
            if (!camera.isOpened()) {
                System.out.println("Error");
            } else {
                while (window.isOpen()) {

                        if (camera.read(frame)) {
                            frames++;

                            cog.process(frame);
                            //t.window(image, "Original Image", 0, 0);
                            //List<MatOfPoint> convexHulls = cog.convexHullsOutput();
                            ArrayList<MatOfPoint> convexHulls = cog.findContoursOutput();
                            byte[] u = {(byte) 0XFF,(byte) 0000, (byte) 0000};
                            //Imgproc.drawContours(frame, convexHulls, -1, new Scalar(0, 0, 0));
                            org.opencv.core.Rect rect = null;
                            org.opencv.core.Rect rect2 = null;
                            List<MatOfPoint> mops = new ArrayList<>();
                            for(MatOfPoint m : convexHulls) {
                                double a = Imgproc.contourArea(m);
                                MatOfPoint2f bb = new MatOfPoint2f();
                                m.convertTo(bb, CvType.CV_32FC2);
                                double al = Imgproc.arcLength(bb, true);
                                MatOfPoint2f asdasd = new MatOfPoint2f();
                                Imgproc.approxPolyDP(bb, asdasd, al*0.005, true);
                                MatOfPoint zz = new MatOfPoint();
                                asdasd.convertTo(zz, CvType.CV_32S);
                                Point[] zz2 = zz.toArray();
                                LinkedList<Line> biggest = new LinkedList<Line>();
                                for(int i = 0; i < 4; i++) {
                                    Line l = new Line(new Point(0., 0.), new Point(0., 0.), 0.0);
                                    biggest.add(l);
                                }
                                int zzz = 0;
                                for(int i = 0; i < zz2.length; i++) {
                                    Point p = zz2[(i+1) % zz2.length];
                                    double dx = (p.x - zz2[i].x);
                                    double dy = (p.y - zz2[i].y);
                                    double len_2 = dx * dx + dy * dy;
                                    if(len_2 > biggest.getFirst().len_2) {
                                        zzz++;
                                        int j = 0;
                                        for(Line l : biggest) {
                                            if(len_2 <= l.len_2) {
                                                break;
                                            }
                                            j++;
                                        }
                                        biggest.add(j, new Line(zz2[i], p, len_2));
                                        biggest.remove(0);
                                        //System.out.println("Replaced idx " + j);
                                        for(Line l : biggest) {
                                            
                                        }
                                    }
                                }
                                //System.out.println("" + zzz + " " + zz2.length);
                                int i = 0;
                                for(Line b : biggest) {
                                    //System.out.println("a " + b.a + " " + b.b);
                                    Imgproc.line(frame, b.a, b.b, new Scalar(255, 0, 0), 2);
                                    i++;
                                }
                                rect = Imgproc.boundingRect(zz);
                                mops.add(zz);

                            }
                            MatOfPoint c = new MatOfPoint();

                            Imgproc.drawContours(frame, mops, -1, new Scalar(0, 0, 255));
                            if(rect != null) {
                                Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
                            }


                            //Imgproc.
                            //Imgproc.approxPolyDP(convexHulls, );
                            /*for(MatOfPoint m : convexHulls) {
                                Point[] z = m.toArray();
                                for(int o = 0; o < z.length; o++)
                                    frame.put((int)(z[o].y), (int)(z[o].x), u);
                            }*/

                            Mat hsv = cog.hsvThresholdOutput();
                            Mat Dilate = cog.cvDilateOutput();
                            Mat Erode = cog.cvErodeOutput();
                            //List<MatOfPoint> test2;
                            //ArrayList<MatOfPoint>



                            //Point p = m.toList()
                            //Mat test = new Mat(Dilate);
                            //frame.put(cog.convexHullsOutput());
                            //Dilate.put();
                            //cog.convexHulls(frame);

                            window.image(frame);
                            window2.image(Dilate);
                            //window.image(Dilate);

                            //t.window(t.loadImage("ImageName"), "Image loaded", 0, 0);

                            break;

                        }

                }
                //if(!window.isOpen())
                    //camera.release();

            }

        }
            camera.release();
            System.out.println("Closed");
    }
}
