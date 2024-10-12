package utils;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.RandomAccessFile;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class ImageUtils {
    public static void readImageRGB(int width, int height, String imgPath, BufferedImage img) {
        try {
            int frameLength = width * height * 3;
            File file = new File(imgPath);
            try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                raf.seek(0);

                byte[] bytes = new byte[frameLength];
                raf.read(bytes);

                int ind = 0;
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        byte r = bytes[ind];
                        byte g = bytes[ind + height * width];
                        byte b = bytes[ind + height * width * 2];

                        int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                        img.setRGB(x, y, pix);
                        ind++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showImage(BufferedImage resultImage) {
        JFrame resultImageFrame = new JFrame("Result Image");
        GridBagLayout gLayout = new GridBagLayout();
        resultImageFrame.getContentPane().setLayout(gLayout);

        JLabel resultImageLabel = new JLabel(new ImageIcon(resultImage));

        GridBagConstraints c2 = new GridBagConstraints();
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.anchor = GridBagConstraints.CENTER;
        c2.weightx = 0.5;
        c2.gridx = 0;
        c2.gridy = 0;

        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.gridx = 0;
        c2.gridy = 1;
        resultImageFrame.getContentPane().add(resultImageLabel, c2);

        resultImageFrame.pack();
        resultImageFrame.setVisible(true);
        resultImageFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void rgbToHsv(int r, int g, int b, float[] hsv) {
        float red = r / 255f;
        float green = g / 255f;
        float blue = b / 255f;

        float max = Math.max(red, Math.max(green, blue));
        float min = Math.min(red, Math.min(green, blue));
        float delta = max - min;

        if (delta == 0) {
            hsv[0] = 0;
        } else if (max == red) {
            hsv[0] = 60 * (((green - blue) / delta) % 6);
        } else if (max == green) {
            hsv[0] = 60 * (((blue - red) / delta) + 2);
        } else {
            hsv[0] = 60 * (((red - green) / delta) + 4);
        }

        if (hsv[0] < 0) hsv[0] += 360;
        hsv[1] = max == 0 ? 0 : (delta / max);
        hsv[2] = max;
    }
}
