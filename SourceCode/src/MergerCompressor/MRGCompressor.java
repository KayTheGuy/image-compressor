package MergerCompressor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kayhan Dehghani Mohammadi 
 * CMPT 365 Spring 2017
 * Assignment 2 Image Compressor
 **/

public class MRGCompressor {

	// Merges Two images
	// img1 is the larger image (both in height and width)
	public static BufferedImage merge(BufferedImage img1, BufferedImage img2) {
		int width1 = img1.getWidth();
		int height1 = img1.getHeight();

		int width2 = img2.getWidth();
		int height2 = img2.getHeight();

		BufferedImage resultImage = new BufferedImage(width1, height1, img1.getType());

		// paint the larger image
		for (int y = 0; y < height1; y++) {
			for (int x = 0; x < width1; x++) {
				Color c = new Color(img1.getRGB(x, y));
				resultImage.setRGB(x, y, c.getRGB());
			}
		}

		// paint the smaller image 
		for (int y = 0; y < height2; y++) {
			for (int x = 0; x < width2; x++) {
				Color c = new Color(img2.getRGB(x, y),true);
				// ignore black pixels
				if (c.getRed() != 0 && c.getBlue() != 0 && c.getGreen() != 0) {
					resultImage.setRGB(x, y, c.getRGB());
				}
			}
		}

		return resultImage;
	}
	
	//get YUV color space from image
	public static List<int[][]> getYuvFromImage(BufferedImage image){
		List<int[][]> yuv = new ArrayList<int[][]>();
        int[][] y = null;
        int[][] u = null;
        int[][] v = null; 
        int r = 0;
        int g = 0;
        int b = 0;
        int width = 0;
        int height = 0;

        width = image.getWidth();
        height = image.getHeight();

        y = new int[height][width];
        u = new int[height][width];
        v = new int[height][width];

        for(int i = 0; i < height; i++)
        {
	        for(int j = 0; j < width; j++)
	        {
		        r = (image.getRGB(j, i) >> 16) & 0xFF;
		        g = (image.getRGB(j, i) >> 8) & 0xFF;
		        b = (image.getRGB(j, i) >> 0) & 0xFF;
		
		        y[i][j] = (int) ((0.299 * r) + (0.587 * g) + (0.114 * b));
		        u[i][j] = (int) ((-0.147 * r) - (0.289 * g) + (0.436 * b));
		        v[i][j] = (int) ((0.615 * r) - (0.515 * g) - (0.100 * b));
	        }
        }

        yuv.add(y);
        yuv.add(u);
        yuv.add(v);

        return yuv;
	}
}
