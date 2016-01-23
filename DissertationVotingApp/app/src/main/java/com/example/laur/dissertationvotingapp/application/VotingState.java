package com.example.laur.dissertationvotingapp.application;

import android.app.Application;

public class VotingState extends Application {

    private String votingState = "0";
    public String getVotingState(){
        return votingState;
    }
    public void setVotingState(String s) {
        votingState = s;
    }
}
