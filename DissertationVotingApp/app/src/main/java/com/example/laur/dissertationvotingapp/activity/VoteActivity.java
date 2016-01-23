package com.example.laur.dissertationvotingapp.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.laur.dissertationvotingapp.R;
import com.example.laur.dissertationvotingapp.application.VotingState;
import com.example.laur.dissertationvotingapp.clientside.VoteRunner;
import com.example.laur.dissertationvotingapp.crypto.AES;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.ExecutionException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class VoteActivity extends Activity {

    Button submit;
    RadioButton rBtn1, rBtn2;
    TextView submitText;
    private AsyncTask<String,String,String> asyncTask;
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote);
        final VotingState state = ((VotingState) getApplicationContext());
        submit = (Button) findViewById(R.id.submit_btn);
        rBtn1 = (RadioButton) findViewById(R.id.radioButton1);
        rBtn2 = (RadioButton) findViewById(R.id.radioButton2);
        submitText = (TextView) findViewById(R.id.submitTextView);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String voteStr = "";
                if(rBtn1.isChecked()) {
                    voteStr = "1";
                } else {
                    if( rBtn2.isChecked()) {
                        voteStr = "2";
                    }
                }
                VoteRunner voteRunner = new VoteRunner();

                //crypto
                AES aes = new AES();
                String encodedVote = aes.encrypt(voteStr);
                //asyncTask = voteRunner.execute(voteStr, getWrappedKey());


                String keyStr = Base64.encodeToString(aes.getAesKey().getEncoded(), Base64.DEFAULT);
                asyncTask = voteRunner.execute(encodedVote, keyStr);
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

                if ("1".equals(response)) {
                    state.setVotingState("0");
                    submitText.setText("You have successfully submitted your vote");
                    finish();
                } else {
                    state.setVotingState("0");
                    submitText.setText("wrong");
                    finish();
                }
                //finalResult.setText(response);
            }
        });
    }

    private String getWrappedKey(AES aes) {
        Key keyStoreKey = aes.getKeyStoreKey();
        Key aesKey = aes.getAesKey();
        Cipher cipher = null;
        String wrappedKeyStr = "";
        try {
            cipher = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        try {
            cipher.init(Cipher.WRAP_MODE, keyStoreKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            byte[] wrappedKey = cipher.wrap(aesKey);
            wrappedKeyStr = Base64.encodeToString(wrappedKey, Base64.DEFAULT);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return wrappedKeyStr;
    }
}
