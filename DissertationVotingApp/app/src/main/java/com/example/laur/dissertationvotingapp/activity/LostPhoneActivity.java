package com.example.laur.dissertationvotingapp.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.laur.dissertationvotingapp.R;
import com.example.laur.dissertationvotingapp.camera.CameraService;
import com.example.laur.dissertationvotingapp.camera.Picture;
import com.example.laur.dissertationvotingapp.clientside.LostParam;
import com.example.laur.dissertationvotingapp.clientside.LostPhoneRunner;

import java.util.concurrent.ExecutionException;

public class LostPhoneActivity extends Activity{

    Button ok;
    EditText cnp, ans1, ans2;
    TextView msg;
    private AsyncTask<LostParam, String, String> asyncTask;
    private String response;
    private byte[] pictureBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost);
        ok = (Button) findViewById(R.id.lost_btn);
        cnp = (EditText) findViewById(R.id.lost_cnp);
        ans1 = (EditText) findViewById(R.id.lost_ans1);
        ans2 = (EditText) findViewById(R.id.lost_ans2);
        msg = (TextView) findViewById(R.id.lost_msg);
        msg.setVisibility(View.INVISIBLE);

        takePhoto();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cnpStr = cnp.getText().toString();
                String ans1Str = ans1.getText().toString();
                String ans2Str = ans2.getText().toString();
                pictureBytes = Picture.pictureBytes;
                LostPhoneRunner lostPhoneRunner = new LostPhoneRunner();
                LostParam lostParams = new LostParam(cnpStr, ans1Str, ans2Str, pictureBytes);

                asyncTask = lostPhoneRunner.execute(lostParams);

                try {
                    String asyncResultText = asyncTask.get();
                    response = asyncResultText.trim();
                } catch (InterruptedException e1) {
                    response = e1.getMessage();
                } catch (ExecutionException e1) {
                    response = e1.getMessage();
                } catch (Exception e1) {
                    response = e1.getMessage();
                }

                if("1".equals(response)) {
                    msg.setVisibility(View.VISIBLE);
                    msg.setText("You have succesfully updated your mobile phone");
                    finish();
                } else {
                    msg.setVisibility(View.INVISIBLE);
                    msg.setText("wrong");
                    finish();
                }
            }
        });
    }


    private void takePhoto() {
        CameraService cameraService = new CameraService(getApplicationContext());
        if(cameraService.hasCamera()) {
            cameraService.getCameraInstance();
            //cameraService.takePicture();
            cameraService.releaseCamera();
        }
    }
}
