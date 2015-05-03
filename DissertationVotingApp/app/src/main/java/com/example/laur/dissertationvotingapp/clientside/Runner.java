package com.example.laur.dissertationvotingapp.clientside;


import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

public class Runner extends AsyncTask<String,String,String> {

    private String responseStr = "";
    @Override
    protected String doInBackground(String... params) {
        int count = params.length;
        if(count==2){
            ArrayList<NameValuePair> postParams = new ArrayList<>();
            postParams.add(new BasicNameValuePair("username", params[0]));
            postParams.add(new BasicNameValuePair("password", params[1]));
            String response = null;
            try {
                Client httpClient = new Client();
                response = httpClient.executeHttpPost("http://192.168.1.9:8080/ServerSide/Servlet", postParams);
                responseStr = response.replaceAll("###", "");

            } catch (Exception e) {
                e.printStackTrace();
                responseStr = e.getMessage();
        }
        }else{
            responseStr="Invalid number of arguments-"+count;
        }
        return responseStr;

    }
}
