package com.serverside.biometrics;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class FaceDetection {
	
	public static void detectFace (String pathIn, String pathOut) {
		
		System.loadLibrary("opencv_java249");
	    //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	        

	    //CascadeClassifier faceDetector = new CascadeClassifier(FaceDetector.class.getResource("haarcascade_frontalface_alt.xm    l").getPath());
	    CascadeClassifier faceDetector = new CascadeClassifier();
	    faceDetector.load("C:/opencv/sources/data/haarcascades/haarcascade_frontalface_alt.xml");
	    
	  
	    Mat image = Highgui.imread(pathIn);
	    
	    MatOfRect morDetections = new MatOfRect();
	    faceDetector.detectMultiScale(image, morDetections);
	    System.out.println(String.format("Detected face on picture received from mobile device",  morDetections.toArray().length));
	    Rect rect_Crop=null;
	    for (Rect rect : morDetections.toArray()) {
	    	Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
	    			new Scalar(0, 255, 0));
	    	rect_Crop = new Rect(rect.x, rect.y, rect.width, rect.height);
	    }
	    
	    
	    Mat faceMat = new Mat(image, rect_Crop);	
		Imgproc.cvtColor(faceMat, faceMat, Imgproc.COLOR_BGR2GRAY); 
		Imgproc.resize(faceMat, faceMat, new Size(50, 50));
	    Highgui.imwrite(pathOut, faceMat);
	}
}
