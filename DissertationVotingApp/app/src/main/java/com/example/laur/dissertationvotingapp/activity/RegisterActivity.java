package com.example.laur.dissertationvotingapp.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.laur.dissertationvotingapp.R;
import com.example.laur.dissertationvotingapp.application.VotingState;
import com.example.laur.dissertationvotingapp.camera.CameraService;
import com.example.laur.dissertationvotingapp.camera.Picture;
import com.example.laur.dissertationvotingapp.clientside.Param;
import com.example.laur.dissertationvotingapp.clientside.RegisterRunner;

import org.apache.http.message.BasicHeaderValueFormatter;

import java.util.concurrent.ExecutionException;

public class RegisterActivity extends Activity {

    Button ok,reg;
    EditText cnp;
    TextView msg;
    private AsyncTask<Param,String,String> asyncTask;
    private String response;
    private byte[] pictureBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        final VotingState state = ((VotingState) getApplicationContext());
        ok = (Button) findViewById(R.id.reg_ok_btn);
        cnp = (EditText) findViewById(R.id.cnp_editText);
        msg = (TextView) findViewById(R.id.reg_msg_tv);
        reg = (Button) findViewById(R.id.reg_btn);


        takePhoto();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*cnp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });*/
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cnpStr = cnp.getText().toString();
                pictureBytes = Picture.pictureBytes;
                //takePhoto();
                RegisterRunner regRunner = new RegisterRunner();
                //if(Picture.pictureBytes!=null) {
                    //asyncTask = regRunner.execute(cnpStr, Base64.encodeToString(pictureBytes, Base64.DEFAULT));
                Param param = new Param(pictureBytes, cnpStr);
                asyncTask = regRunner.execute(param);
                //}
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
                    state.setVotingState("1");
                    msg.setText("You have succesfully logged in! You can now vote!");
                    finish();
                } else {
                    msg.setText("wrong");
                    state.setVotingState("0");
                    finish();
                }
                //finalResult.setText(response);
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
