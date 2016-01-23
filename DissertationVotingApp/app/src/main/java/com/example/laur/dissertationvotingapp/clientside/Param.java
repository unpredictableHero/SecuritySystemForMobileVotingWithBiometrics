package com.example.laur.dissertationvotingapp.clientside;

public class Param {

    private String cnp;
    private byte[] img;

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public Param(byte[] img, String cnp) {
        this.img = img;
        this.cnp = cnp;
    }
}
