package Decompressor;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author Kayhan Dehghani Mohammadi 
 * CMPT 365 Spring 2017
 * Assignment 2 Image Compressor
 **/

public class MRGDecompressor {
	
	// Make a bufferedImage from YUV list
	public static BufferedImage getImageFromYuv(List<int[][]> yuv, int imgType)
    {
		BufferedImage image = null;
		int width = 0;
		int height = 0;
		int r = 0;
		int g = 0;
		int b = 0;
		int[][] y = null;
		int[][] u = null;
		int[][] v = null;
		
		y = yuv.get(0);
		u = yuv.get(1);
		v = yuv.get(2);
		
		height = y.length;
		width = y[0].length;
		image = new BufferedImage(width, height, (imgType == 0 ? BufferedImage.TYPE_INT_RGB : imgType));
		
		for(int i = 0; i < height; i++)
		{
		    for(int j = 0; j < width; j++)
		    {
		        r = pixelRange(y[i][j] + 1.140 * v[i][j]);
		        g = pixelRange(y[i][j] - 0.395 * u[i][j] - 0.581 * v[i][j]);
		        b = pixelRange(y[i][j] + 2.032 * u[i][j]);
		
		        image.setRGB(j, i, (r << 16) + (g << 8) + b);
		    }
		}
		
		return image;
    }
	
	public static int pixelRange(int p)
    {
        return ((p > 255) ? 255 : (p < 0) ? 0 : p);
    }

    public static int pixelRange(double p)
    {
        return ((p > 255) ? 255 : (p < 0) ? 0 : (int) p);
    }

}
