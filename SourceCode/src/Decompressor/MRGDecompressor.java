package Decompressor;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import MergerCompressor.DCT;

/**
 * @author Kayhan Dehghani Mohammadi 
 * CMPT 365 Spring 2017
 * Assignment 2 Image Compressor
 **/

public class MRGDecompressor {
	
	public static BufferedImage getImageFromYuv(List<double[][]> yuv, int imgType)
    {
		BufferedImage image = null;
		int width = 0;
		int height = 0;
		int r = 0;
		int g = 0;
		int b = 0;
		double[][] y = null;
		double[][] u = null;
		double[][] v = null;
		
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
    
    
    private static double[] read (String filename) throws IOException {
    	List<Double> result = new ArrayList<>();
    	FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        String s = br.readLine();
        while (s != null) {
        	result.add(Double.parseDouble(s));
            s = br.readLine();
        }
        br.close();
        
        double[] target = new double[result.size()];
        for (int i = 0; i < target.length; i++) {
           target[i] = result.get(i).doubleValue(); 
        }
        return target;
    }
    
    
	public static BufferedImage Decompress(List<double[][]> input) {
		DCT dctUtil = new DCT(input.get(0)[0].length, input.get(0).length);
		
		List<double[][]> transformedImg = new ArrayList<>();
		
		for(double[][] component : input) {		
			transformedImg.add(dctUtil.inverseDCT(component));
		}
		
		return new BufferedImage(input.get(0)[0].length, input.get(0).length, 0);
	}
    
    public static List<double[][]> decompressFile() {
    	List<double[][]> yuvFromMrg = null;
    	double[] mrgData=null;
		try {
			mrgData = read("compressed.mrg");
			
			yuvFromMrg = new ArrayList<>();
			
			double[][] y = new double[(int) mrgData[1]][(int) mrgData[0]];
			double[][] u = new double[(int) mrgData[1]][(int) mrgData[0]];
			double[][] v = new double[(int) mrgData[1]][(int) mrgData[0]];
			
			int headIndx = 2;
			
			for (int i = 0; i < (int) mrgData[1]; i++) {
				for (int j = 0; j < (int) mrgData[0]; j++) {
					if((i+j)%4 == 0) {
						y[i][j] = mrgData[headIndx++];						
					}
					else{
						y[i][j] = mrgData[headIndx-1];
						headIndx++;
					}						
				}
			}
			
			for (int i = 0; i < (int) mrgData[1]; i++) {
				for (int j = 0; j < (int) mrgData[0]; j++) {
					if((i+j)%4 == 0) {
						u[i][j] = mrgData[headIndx++];						
					}
					else{
						u[i][j] = mrgData[headIndx-1]/1.2;
						headIndx++;
					}
				}
			}
			
			for (int i = 0; i < (int) mrgData[1]; i++) {
				for (int j = 0; j < (int) mrgData[0]; j++) {
					if((i+j)%4 == 0) {
						v[i][j] = mrgData[headIndx++];					
					}
					else{
						v[i][j] = mrgData[headIndx-1]/2;
						headIndx++;
					}
				}
			}
			
			yuvFromMrg.add(y);
			yuvFromMrg.add(u);
			yuvFromMrg.add(v);
			
			
		} catch (IOException e) {
			System.out.println("There was a problem reading the MRG format file!");
		}
		return yuvFromMrg;
    }

}
