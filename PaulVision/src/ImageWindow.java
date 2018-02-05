import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

public class ImageWindow {
    private JFrame frame;

    public ImageWindow(String title, int x, int y) {
        frame = new JFrame(title);
        frame.setLocation(x, y);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void image(Mat m) {
        BufferedImage bfm = MatToBufferedImage(m);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(new JPanel() {
            @Override
            public void paint(Graphics g) {
                g.drawImage(bfm, 0, 0, this);
            }
        });
        frame.setSize(bfm.getWidth(), bfm.getHeight() + 30);
        frame.setVisible(true);
        frame.repaint();
    }

    public static BufferedImage MatToBufferedImage(Mat frame) {
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
