package com.example.laur.dissertationvotingapp.clientside;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class VoteRunner extends AsyncTask<String, String, String> {

    private String responseStr = "";
    @Override
    protected String doInBackground(String... params) {
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();
        int count = params.length;
        if(count == 2) {
            ArrayList<NameValuePair> postParams = new ArrayList<>();
            postParams.add(new BasicNameValuePair("choice", params[0]));
            postParams.add(new BasicNameValuePair("key", params[1]));
            String response = null;
            try {
                Client httpClient = new Client();
                response = httpClient.executeHttpPost("http://192.168.1.9:8080/ServerSide/rest/voting/cast/", postParams);
                //response = httpClient.executeRegisterHttpPost("http://192.168.1.5:8080/ServerSide/rest/voting/", postParams);

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
