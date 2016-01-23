package com.example.laur.dissertationvotingapp.crypto;

import android.util.Base64;

import com.example.laur.dissertationvotingapp.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private static final String ALGO = "AES/ECB/PKCS7Padding";
    private static final String keyStorePass = "123456";
    private Key aesKey;

    public String encrypt(String data) {
        Key key = null;
        try {
            key = generateKey();
            aesKey = key;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Cipher c = null;
        try {
            try {
                c = Cipher.getInstance(ALGO,"BC");
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            c.init(Cipher.ENCRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] encVal = new byte[0];
        try {
            encVal = c.doFinal(data.getBytes());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        String encryptedValue = Base64.encodeToString(encVal, 0);
        return encryptedValue;
    }


    private Key generateKey() throws Exception {

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();
        return secretKey;
    }

    public Key getKeyStoreKey() {
        KeyStore ks = null;
        Key key = null;
        try {
            ks = KeyStore.getInstance("JKS");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        InputStream readStream = null;
        try {
            readStream = new FileInputStream("D:\\ase\\disertatie\\dissertationProject\\DissertationVotingApp\\app\\src\\main\\res\\raw\\keystore.bks");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ks.load(readStream, keyStorePass.toCharArray());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        try {
            key = ks.getKey("keyAlias", keyStorePass.toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        try {
            readStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return key;
    }

    public Key getAesKey() {
        return aesKey;
    }
}
