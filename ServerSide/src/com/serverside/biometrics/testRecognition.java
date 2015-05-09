package com.serverside.biometrics;

import java.io.File;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import static org.bytedeco.javacpp.opencv_contrib.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;



public class testRecognition {

	public static void main(String[] args) {
        
        Mat testImage = imread("D:\\ase\\disertatie\\openCvPicturesTesting\\picture.jpg");

        File root = new File("D:\\ase\\disertatie\\openCvPicturesTesting\\faces");

        FilenameFilter imgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".jpg");
            }
        };

        File[] imageFiles = root.listFiles(imgFilter);

        MatVector images = new MatVector(imageFiles.length);

        Mat labels = new Mat(imageFiles.length, 1, CV_32SC1);
        IntBuffer labelsBuf = labels.getIntBuffer();

        int counter = 0;

        for (File image : imageFiles) {
            Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);

            int label = Integer.parseInt(image.getName().split("\\-")[0]);

            images.put(counter, img);

            labelsBuf.put(counter, label);

            counter++;
        }

        org.bytedeco.javacpp.opencv_contrib.FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
        // FaceRecognizer faceRecognizer = createEigenFaceRecognizer();
        // FaceRecognizer faceRecognizer = createLBPHFaceRecognizer()

        faceRecognizer.train(images, labels);

        int predictedLabel = faceRecognizer.predict(testImage);

        System.out.println("prediction: " + predictedLabel);
    }
}
