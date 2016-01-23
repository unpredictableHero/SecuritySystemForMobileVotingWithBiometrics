package com.example.laur.dissertationvotingapp.clientside;

public class LostParam {

    private String cnp;
    private String ans1;
    private String ans2;
    private byte[] img;

    public LostParam(String cnp, String ans1, String ans2, byte[] img) {
        this.cnp = cnp;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.img = img;
    }

    public String getCnp() {
        return cnp;
    }

    public String getAns1() {
        return ans1;
    }

    public String getAns2() {
        return ans2;
    }

    public byte[] getImg() {
        return img;
    }
}
