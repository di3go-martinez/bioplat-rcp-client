package edu.unlp.medicine.bioplat.rcp.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ConvertByteImageUtils {

	private static final String EXPRESSION = "#52, Vanvijver-C3: 2064,2886,6678,3169,5214,9232,3872,9493,4175,10403,2353,2237,2099,9319,6241,3161,7802,983,7031,991,10615,25800,990,1153,701,7494,4602,11130,890,7033,6422,1846,716,9133,1058,2568,1033,4171,4605,4085,9787,2625,7083,2305,999,7043,771,9768,11065,2146,1164,5347" //
			+ "\n" + " library(survival) time13<-c(177.807,171.1376,79.73717,92.97741,32.91992,104.8706,90.80903,103.7864,83.94251,183.9507,41.69199,153.1992,66.66119,181.6181,65.83984,43.86037,220.0903,168.1478,14.12731,181.2567,11.17043,211.9097,94.48871,193.7741,97.54415,183.885,160.1971,150.8665,158.0945,135.8193,142.3244,88.01643,150.0452,135.1622,89.3963,152.8378,141.9959,113.577,134.3409,112.0657,129.2156,134.4066,121.6591,115.8439,77.76591,89.72485,117.9795,123.9261,124.5175,120.2464,42.97331,169.462,78.19302,97.11704,119.7864,67.64682,136.3778,107.8604,66.20123,99.64682,103.1294,26.67762,87.0308,84.27105,87.0308,71.09651,1.0,61.37166,63.73717,63.50719,88.08214,68.92813,69.25667,72.80493,59.66324,100.4025,66.66119,115.154,113.4784,34.23409,111.9671,110.3244,80.52567,114.5955,122.4148,100.731,109.2402,72.67351,98.89117,28.02464,18.00411,105.6263,106.2505,66.92402,92.32033,88.90349,184.2136,79.31006,83.94251,85.4538,56.64066,74.05339,77.56879,69.71663,58.1191,73.92197,72.54209,74.57906,69.8809,74.87474,72.21355,66.59548,64.16427,209.8398,205.8316,114.8255,119.9836,93.2731,152.8706,93.2731,132.9938,98.56263,86.70226,81.64271,42.1191,68.50103,134.538,57.19918,18.33265,81.05133,90.84189,11.95893,63.6386,62.78439,121.1663,133.8809,121.6591,123.5318,98.79261,94.39014,83.64682,62.22587,74.94045,136.6735,121.2977,88.24641,132.2053,13.07598,58.87474,56.34497,58.71047,32.16427,36.33676,75.82752,87.55647,70.40657,41.26489,16.82136,19.31828,206.883,11.53183,33.74127,53.35524,183.7536,189.8316,74.31622,19.35113,178.6283,14.65298,164.9938,31.37577,129.807,144.8871,32.3614,130.8912,132.4353,128.0986,134.4394,23.68789,20.59959,28.09035,39.26078,19.41684,29.79877,13.83162,49.1499,138.5462,50.13552,63.80287,112.8871,109.4702,55.06366,20.59959,61.40452,84.13963,83.154,83.97536,63.90144,46.98152,59.3347,4.23819,139.8275,75.76181,73.72485,3.90965,21.38809,111.8686,50.62834,38.63655,25.65914,76.45175,118.7351,80.45996,55.45791,25.56057,25.88912,101.9466,25.03491,199.0965,37.4538,20.79671,78.32444,63.11294,59.66324,216.9692,6.86653,39.09651,23.62218,32.16427,209.0513,167.0308,166.3737,23.35934,41.03491,74.05339,66.89117,122.7762,16.49281,38.34086,26.02054,15.24435,155.9589,105.6263,69.65092,98.00411,13.47023,55.55647,9.69199,25.52772,13.37166,111.9671,169.5277,67.97536,91.13758,107.1047,153.1663,14.52156,140.8789,24.6078,132.5667,40.54134,145.7413,124.2218,27.03901,105.4949,14.68583,3.25257,12.09035,24.04928,59.20329,81.47844,85.05955,11.23593,35.54825,7.78645,23.58932,109.1745,106.3162,99.58111,62.39014,95.90144,89.72485,76.1232,82.49692,39.42505,102.3409,101.0924,88.54209,59.43326,94.29158,12.32033,27.56468,23.78645,25.79055,26.51335) " //
			+ "\n" + " status13<-c(0,0,0,0,1,0,0,0,1,0,1,0,0,0,0,1,0,1,1,0,1,0,0,0,1,0,0,0,0,0,0,1,0,0,1,0,0,1,0,1,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,1,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,1,0,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,1,0,0,1,0,0,0,0,1,0,1,1,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,0,1,1,1,1,0,1,1,1,0,0,1,1,0,1,0,1,1,0,1,0,0,0,0,1,1,1,1,1,0,1,1,0,1,0,0,1,1,1,1,0,0,0,0,1,0,1,0,0,0,1,0,0,1,1,1,1,0,0,1,1,1,0,1,0,1,1,0,0,1,0,1,1,1,1,0,0,0,1,1,0,0,0,1,1,1,1,0,0,0,0,1,1,1,1,1,0,0,0,1,1,0,1,1,1,0,1,1,0,1,0,1,1,1,1,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,1,0,0,1,0,1,1,1,1,1) " //
			+ "\n" + " groups13<-c(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2) " //
			+ "\n" + " survFitResult13<-survfit(formula = Surv(time13, status13) ~ groups13) " //
			+ "\n" + " plot(survFitResult13,col=c('red', 'blue', 'green', 'orange', 'yellow', 'brown'))";



	// TODO unificar con el método de abajo
	public static File toImage(byte[] bytes, String filename) throws IOException {
		// Before is how to change ByteArray back to Image
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		Iterator<?> readers = ImageIO.getImageReadersByFormatName("jpg");
		// ImageIO is a class containing static convenience methods for locating
		// ImageReaders
		// and ImageWriters, and performing simple encoding and decoding.

		ImageReader reader = (ImageReader) readers.next();
		Object source = bis; // File or InputStream, it seems file is OK

		ImageInputStream iis = ImageIO.createImageInputStream(source);
		// Returns an ImageInputStream that will take its input from the given
		// Object

		reader.setInput(iis, true);
		ImageReadParam param = reader.getDefaultReadParam();

		Image image = reader.read(0, param);
		// got an image file

		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
		// bufferedImage is the RenderedImage to be written
		Graphics2D g2 = bufferedImage.createGraphics();
		
		g2.drawImage(image, null, null);
		File imageFile = new File(filename);
		ImageIO.write(bufferedImage, "jpg", imageFile);
		// "jpg" is the format of the image imageFile is the file to be written
		// to.
		return imageFile;
	}

	// TODO unificar con el método de arriba
	public static File toImage(byte[] bytes) throws IOException {
		// Before is how to change ByteArray back to Image
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		Iterator<?> readers = ImageIO.getImageReadersByFormatName("jpg");
		// ImageIO is a class containing static convenience methods for locating
		// ImageReaders
		// and ImageWriters, and performing simple encoding and decoding.

		ImageReader reader = (ImageReader) readers.next();
		Object source = bis; // File or InputStream, it seems file is OK

		ImageInputStream iis = ImageIO.createImageInputStream(source);
		// Returns an ImageInputStream that will take its input from the given
		// Object

		reader.setInput(iis, true);
		ImageReadParam param = reader.getDefaultReadParam();

		Image image = reader.read(0, param);
		// got an image file

		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
		// bufferedImage is the RenderedImage to be written
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawImage(image, null, null);
		File imageFile = File.createTempFile("bioplat.image", ".jpg");
		imageFile.deleteOnExit();
		ImageIO.write(bufferedImage, "jpg", imageFile);
		// "jpg" is the format of the image imageFile is the file to be written
		// to.
		return imageFile;
	}

	public static byte[] toBytes(File file) throws FileNotFoundException {
		System.out.println(file.exists() + "!!");

		FileInputStream fis = new FileInputStream(file);
		// create FileInputStream which obtains input bytes from a file in a
		// file system
		// FileInputStream is meant for reading streams of raw bytes such as
		// image data. For reading streams of characters, consider using
		// FileReader.

		// InputStream in = resource.openStream();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		try {
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
				// no doubt here is 0
				/*
				 * Writes len bytes from the specified byte array starting at
				 * offset off to this byte array output stream.
				 */
				System.out.println("read " + readNum + " bytes,");
			}
		} catch (IOException ex) {
			Logger.getLogger(ConvertByteImageUtils.class.getName()).log(Level.SEVERE, null, ex);
		}
		return bos.toByteArray();
	}
}