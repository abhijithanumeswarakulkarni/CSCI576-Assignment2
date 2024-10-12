package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.RandomAccessFile;
import javax.imageio.ImageIO;

import utils.ImageUtils;

public class Assignment {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Invalid input");
            return;
        }

        String imagePath = args[0];
        int h1 = Integer.parseInt(args[1]);
        int h2 = Integer.parseInt(args[2]);

        boolean isFullRange = (h1 == 0 && h2 == 359);

        try {
            int width = 512;
            int height = 512;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            ImageUtils.readImageRGB(width, height, imagePath, image);
            if (!isFullRange) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int rgb = image.getRGB(x, y);
                        int r = (rgb >> 16) & 0xFF;
                        int g = (rgb >> 8) & 0xFF;
                        int b = rgb & 0xFF;

                        float[] hsv = new float[3];
                        ImageUtils.rgbToHsv(r, g, b, hsv);

                        if (hsv[0] < h1 || hsv[0] > h2) {
                            int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                            int grayRgb = (gray << 16) | (gray << 8) | gray;
                            image.setRGB(x, y, grayRgb);
                        }
                    }
                }
            }
            ImageUtils.showImage(image);
        } catch (Exception e) {
            System.out.println("Something Went Wrong!");
            e.printStackTrace();
        }
    }
}
