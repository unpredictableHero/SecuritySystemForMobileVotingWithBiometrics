package com.example.laur.dissertationvotingapp.clientside;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

public class RegisterRunner extends AsyncTask<String,String,String> {

    private String responseStr = "";
    @Override
    protected String doInBackground(String... params) {
        int count = params.length;
        if(count == 1) {
            ArrayList<NameValuePair> postParams = new ArrayList<>();

        }

        return responseStr;
    }
}
