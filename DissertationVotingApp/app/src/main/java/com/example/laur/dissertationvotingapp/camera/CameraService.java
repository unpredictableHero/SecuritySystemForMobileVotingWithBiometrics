package com.example.laur.dissertationvotingapp.camera;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.os.IBinder;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;


import java.io.IOException;


public class CameraService {

    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private boolean hasCamera;
    private int cameraId;
    private Picture picture;
    private Context context;
    public static final int DONE = 1;
    public static final int NEXT = 2;
    private Timer timer;
    private boolean safeToTakePicture = false;

    public CameraService(Context c){
        picture = new Picture();
        context = c.getApplicationContext();

        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            cameraId = getFrontCameraId();

            if(cameraId != -1){
                hasCamera = true;
            }else{
                hasCamera = false;
            }
        }else{
            hasCamera = false;
        }
    }

    public boolean hasCamera(){
        return hasCamera;
    }

    public void getCameraInstance(){
        camera = null;
        byte[] pictureBytes = null;

        if(hasCamera){
            try{
                //camera = Camera.open(cameraId);
              prepareCamera();
            }
            catch(Exception e){
                hasCamera = false;
            }
        }
    }

    public void takePicture(){
        if(hasCamera && safeToTakePicture){
            camera.takePicture(null, null, picture);
            safeToTakePicture = false;
        }
    }

    private void prepareCamera(){
        SurfaceView view = new SurfaceView(context);

        SurfaceHolder holder = view.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //showMessage("Surface created");

                try {
                    camera = Camera.open(cameraId);
                    //showMessage("Opened camera");
                    try {
                        camera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Camera.Parameters params = camera.getParameters();
                    camera.startPreview();
                    params.setRotation(270);
                    params.setJpegQuality(100);
                    params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_TWILIGHT);
                    camera.setParameters(params);
                    //showMessage("Started preview");
                    camera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            camera.takePicture(null, null, picture);
                        }
                    });

                    // Thread.sleep(10);
                } catch (Exception e) {
                    if (camera != null)
                        camera.release();
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });



        WindowManager wm = (WindowManager)context
                .getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                1, 1, //Must be at least 1x1
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                0,
                //Don't know if this is a safe default
                PixelFormat.UNKNOWN);

        //Don't set the preview visibility to GONE or INVISIBLE
        wm.addView(view, params);

       /* try{
            camera.setPreviewDisplay(view.getHolder());
        } catch(IOException e){
            e.printStackTrace();
        }*/

        /*try {
            //camera.setPreviewTexture(new SurfaceTexture((0)));
            camera.startPreview();
            safeToTakePicture = true;
        } catch (Exception e){
            e.printStackTrace();
        }
        Camera.Parameters params = camera.getParameters();
        params.setJpegQuality(100);

        camera.setParameters(params);*/
        //timer=new Timer(context, threadHandler);
        //timer.execute();

    }

    private int getFrontCameraId(){
        int camId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo ci = new Camera.CameraInfo();

        for(int i = 0;i < numberOfCameras;i++){
            Camera.getCameraInfo(i,ci);
            if(ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                camId = i;
            }
        }

        return camId;
    }

    public void releaseCamera(){
        if(camera != null){
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    private boolean safeCameraOpen() {
        boolean qOpened = false;
        try {
            releaseCamera();
            camera = Camera.open(cameraId);
            qOpened = (camera != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qOpened;
    }

/*    private Handler threadHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what){
                case DONE:
                    // Trigger camera callback to take pic
                    if(safeToTakePicture) {
                        camera.takePicture(null, null, picture);
                    }
                    safeToTakePicture = false;
                    break;
                case NEXT:
                    timer=new Timer(context, threadHandler);
                    timer.execute();
                    break;
            }
        }
    };*/

}
