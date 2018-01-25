import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

public class WindowWithImage {
    private JFrame frame;

    private boolean open = true;
    private ImgPanel img_p;
    private int last_w = -1, last_h = -1;

    class ImgPanel extends JPanel {
        public BufferedImage img;
        @Override
        public void paint(Graphics g) {
            if(img != null) {
                g.drawImage(img, 0, 0, this);
            }
        }
    }

    public WindowWithImage(String title, int x, int y) {
        frame = new JFrame(title);
        frame.setLocation(x, y);
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        //frame.setResizable(false);
        img_p = new ImgPanel();
        frame.add(img_p);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                open = false;
                System.out.println("windowClosing method called");
            }
        });
    }

    public void image(Mat m) {
        if (!isOpen()) return;
        BufferedImage bfm = MatToBufferedImage(m);
        img_p.img = bfm;
        //frame.getContentPane().removeAll();
        //frame.getContentPane().add(new JPanel() {
        //    @Override
        //    public void paint(Graphics g) {
        //        g.drawImage(bfm, 0, 0, this);
        //    }
        //});
        int nw = bfm.getWidth(), nh = bfm.getHeight();
        if(nw != last_w || nh != last_h) {
            frame.setSize(nw, nh+30);
            last_w = nw; last_h = nh;
        }
        //frame.setVisible(true);
        frame.repaint();
    }

    public boolean isOpen() {
        return open;
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
