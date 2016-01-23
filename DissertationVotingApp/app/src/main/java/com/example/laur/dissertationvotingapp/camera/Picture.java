package com.example.laur.dissertationvotingapp.camera;

import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Picture implements PictureCallback {

    public static byte[] pictureBytes;

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        pictureBytes = data;
        File pictureFile = getOutputMediaFile();

        if (pictureFile == null) {
            System.out.println("no picture");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public File getOutputMediaFile() {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraVoting");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String pictureName = "VoteImg";
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + pictureName + ".jpg");
        return mediaFile;
    }
}
