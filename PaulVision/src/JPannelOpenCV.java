import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.TimerTask;

import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;

class JPanelOpenCV extends JPanel {

    JFrame frame0 = new JFrame();

    BufferedImage image;

    public static void main(String args[]) throws InterruptedException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        boolean isRunning = true;

        JPanelOpenCV jpocv = new JPanelOpenCV();
        VideoCapture camera = new VideoCapture(0);

        Mat frame = new Mat();
        camera.read(frame);
        //if(camera.read() = true) System.out.println();

        ImageWindow window = new ImageWindow("wow", 0, 0);

        double time = System.currentTimeMillis();
        PaulPipeline cog = new PaulPipeline();

        long startTime = System.currentTimeMillis();

        int frames = 0;
        int timer = 0;
        int runs = 1;
        //Timer timer = new Timer();
       // Timer.schedule(new FPS(frames), 0, 5000);

        while (System.currentTimeMillis() - time < 250000) {
            if (!camera.isOpened()) {
                System.out.println("Error");
            } else {
                while (isRunning) {
//                    if(time > 1000*runs) {
//                        System.out.println(frames);
//                        frames = 0;
//                        runs++;
                    //}

                    if (camera.read(frame)) {
                        frames++;


                        BufferedImage image = jpocv.MatToBufferedImage(frame);
                        cog.process(frame);
                        //t.window(image, "Original Image", 0, 0);

                        window.image(frame);

                        //t.window(t.loadImage("ImageName"), "Image loaded", 0, 0);

                        break;
                    }
                }

            }

        }
        camera.release();


    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public JPanelOpenCV() {
    }

    public JPanelOpenCV(BufferedImage img) {
        image = img;
    }

    //Show image on window
    public void window(BufferedImage img, String text, int x, int y) {

        frame0.getContentPane().add(new JPanelOpenCV(img));
        frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame0.setTitle(text);
        frame0.setSize(img.getWidth(), img.getHeight() + 30);
        frame0.setLocation(x, y);
        frame0.setVisible(true);
    }

    //Load an image
    public BufferedImage loadImage(String file) {
        BufferedImage img;

        try {
            File input = new File(file);
            img = ImageIO.read(input);

            return img;
        } catch (Exception e) {
            System.out.println("erro");
        }

        return null;
    }

    //Save an image
    public void saveImage(BufferedImage img) {
        try {
            File outputfile = new File("Images/new.png");
            ImageIO.write(img, "png", outputfile);
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    //Grayscale filter
    public BufferedImage grayscale(BufferedImage img) {
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color c = new Color(img.getRGB(j, i));

                int red = (int) (c.getRed() * 0.299);
                int green = (int) (c.getGreen() * 0.587);
                int blue = (int) (c.getBlue() * 0.114);

                Color newColor =
                        new Color(
                                red + green + blue,
                                red + green + blue,
                                red + green + blue);

                img.setRGB(j, i, newColor.getRGB());
            }
        }

        return img;
    }

    public BufferedImage MatToBufferedImage(Mat frame) {
        //Mat() to BufferedImage
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);

        return image;
    }
}

class FPS extends TimerTask
{
    private int frames;

    public FPS (int frames)
    {
        this.frames = frames;
    }

    public void run() {
        System.out.println(frames);
    }
}