package com.serverside.biometrics;



import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
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


public class testDectection {
    private static Mat cropImage;

public static void main(String[] args)throws Exception {
	
    System.loadLibrary("opencv_java249");
    //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("\nRunning FaceDetector");

//CascadeClassifier faceDetector = new       CascadeClassifier(FaceDetector.class.getResource("haarcascade_frontalface_alt.xm    l").getPath());
    CascadeClassifier faceDetector = new CascadeClassifier();
    faceDetector.load("C:/opencv/sources/data/haarcascades/haarcascade_frontalface_alt.xml");
    
    Security.addProvider(new BouncyCastleProvider());
	Mat image = Highgui.imread("D:\\ase\\disertatie\\openCvPicturesTesting\\download.jpg");
	MatOfRect face_Detections = new MatOfRect();
	faceDetector.detectMultiScale(image, face_Detections);
	System.out.println(String.format("Detected %s faces",  face_Detections.toArray().length));
	Rect rect_Crop=null;
	for (Rect rect : face_Detections.toArray()) {
	    Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
	            new Scalar(0, 255, 0));
	    rect_Crop = new Rect(rect.x, rect.y, rect.width, rect.height);
	}
	
	
	
	Mat faceMat = new Mat(image, rect_Crop);	
	Imgproc.cvtColor(faceMat, faceMat, Imgproc.COLOR_BGR2GRAY); 
	Imgproc.resize(faceMat, faceMat, new Size(50, 50));
	Imgproc.equalizeHist(faceMat, faceMat);
	Highgui.imwrite("D:\\ase\\disertatie\\openCvPicturesTesting\\downloadOutput.jpg", faceMat);



}}


