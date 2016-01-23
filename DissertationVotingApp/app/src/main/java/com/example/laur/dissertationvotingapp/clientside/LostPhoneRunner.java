package com.example.laur.dissertationvotingapp.clientside;

import android.os.AsyncTask;
import android.util.Base64;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class LostPhoneRunner extends AsyncTask<LostParam,String,String> {

    private String responseStr = "";
    private static final String IMEI = "123";

    @Override
    protected String doInBackground(LostParam... params) {
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();
        int count = params.length;
        if(count == 1) {
            ArrayList<NameValuePair> postParams = new ArrayList<>();
            postParams.add(new BasicNameValuePair("cnp", params[0].getCnp()));
            postParams.add(new BasicNameValuePair("imei", IMEI));
            postParams.add(new BasicNameValuePair("ans1", params[0].getAns1()));
            postParams.add(new BasicNameValuePair("ans2", params[0].getAns2()));
            postParams.add(new BasicNameValuePair("img", Base64.encodeToString(params[0].getImg(),Base64.DEFAULT)));
            String response = null;
            try {
                Client httpClient = new Client();

                response = httpClient.executeRegisterHttpPost("http://192.168.1.9:8080/ServerSide/rest/voting/lost/", postParams);

                responseStr = response;
            } catch (Exception e) {
                e.printStackTrace();
                responseStr = e.getMessage();
            }
        } else {
            responseStr="Invalid number of arguments-"+count;
        }

        return responseStr;
    }
}
