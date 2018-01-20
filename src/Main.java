import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
//import org.opencv.highgui.ImageWindow;
import org.opencv.highgui.Highgui;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String args[]) throws InterruptedException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        boolean isRunning = true;

        VideoCapture camera = new VideoCapture(0);

        Mat frame = new Mat();
        camera.read(frame);
        //if(camera.read() = true) System.out.println();

        WindowWithImage window = new WindowWithImage("wow", 0, 0);

        double time = System.currentTimeMillis();
        ContourOfGod cog = new ContourOfGod();

        long startTime = System.currentTimeMillis();

        int frames = 0;
        int timer = 0;
        int runs = 1;
        //Timer timer = new Timer();
        // Timer.schedule(new FPS(frames), 0, 5000);
//        while (!window.isOpen()) continue;
        while (System.currentTimeMillis() - time < 250000 && window.isOpen()) {
            //System.out.println(window.isOpen());
            if (!camera.isOpened()) {
                System.out.println("Error");
            } else {
                while (window.isOpen()) {

                    if (camera.read(frame)) {
                        frames++;

                        cog.process(frame);
                        //t.window(image, "Original Image", 0, 0);

                        window.image(frame);

                        //t.window(t.loadImage("ImageName"), "Image loaded", 0, 0);

                        break;
                    }
                }
                if(!window.isOpen())
                    camera.release();

            }

        }
            camera.release();
            System.out.println("Closed");
    }
}
