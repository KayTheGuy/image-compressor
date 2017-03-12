package MergerCompressor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	
	/*
	 * 1. RGB -> YUV 4:2:0
	 * 2. 2D DCT
	 * 3. Quantization
	 * 4. 1D array of quantized output concatenated with quantization matrix combined
	 */
	public static void makeMRGformat(BufferedImage image) {
		// 1.RGB -> YUV 4:2:0
		List<double[][]> yuvImage = getYuvFromImage(image);
		
		//2. 2D DCT
		DCT dctUtil = new DCT(yuvImage.get(0)[0].length, yuvImage.get(0).length);
		
		List<double[][]> transformedImg = new ArrayList<>();
		
		for(double[][] component : yuvImage) {		
			transformedImg.add(dctUtil.forwardDCT(component));
		}
		
		List<double[][]> buffer = getYuvFromImage(image);
		
		double[] tot = new double[3*image.getHeight()*image.getWidth() + 2];
		int idx = 0;
		
		//ADD HEADER
		tot[idx++] = image.getWidth();
		tot[idx++] = image.getHeight();
		
		for(double[][] component : buffer ){
			for(int i=0;i<component.length;i++){
				for(int j=0;j<component[0].length;j++){
					tot[idx++] = component[i][j];
				}
			}
		}
		try {
			write("compressed.mrg",tot);
		} catch (IOException e) {
			System.out.println("There was a problem saving the MRG format file!");
		}				
	}
	
	//get YUV color space from image
	//with 4:2:0 down sampling
	private static List<double[][]> getYuvFromImage(BufferedImage image){
		List<double[][]> yuv = new ArrayList<double[][]>();
		double[][] y = null;
		double[][] u = null;
		double[][] v = null; 
		double r = 0;
		double g = 0;
		double b = 0;
        int width = 0;
        int height = 0;

        width = image.getWidth();
        height = image.getHeight();

        y = new double[height][width];
        u = new double[height][width];
        v = new double[height][width];

        for(int i = 0; i < height; i++)
        {
	        for(int j = 0; j < width; j++)
	        {
		        r = (image.getRGB(j, i) >> 16) & 0xFF;
		        g = (image.getRGB(j, i) >> 8) & 0xFF;
		        b = (image.getRGB(j, i) >> 0) & 0xFF;
		
		        y[i][j] = (double) ((0.299 * r) + (0.587 * g) + (0.114 * b));
			    u[i][j] = (double) ((-0.147 * r) - (0.289 * g) + (0.436 * b));
			    v[i][j] = (double) ((0.615 * r) - (0.515 * g) - (0.100 * b));
		     }
        }

        yuv.add(y);
        yuv.add(u);
        yuv.add(v);

        return yuv;
	}
    
    private static void write (String filename, double[] stream) throws IOException{
    	  BufferedWriter outputWriter = null;
    	  outputWriter = new BufferedWriter(new FileWriter(filename));
    	  for (int i = 0; i < stream.length; i++) {
    	    outputWriter.write(stream[i]+" ");
    	    outputWriter.newLine();
    	  }
    	  outputWriter.flush();  
    	  outputWriter.close();  
    }
}
