package MergerCompressor;

import java.awt.Color;
import java.awt.image.BufferedImage;

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
}
