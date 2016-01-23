package com.serverside;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.mysql.jdbc.util.Base64Decoder;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import org.bouncycastle.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
public class Crypto {
	
	private Key publicKey;
	private Key privateKey;
	
	public Key getPublicKey(){
		return publicKey;
	}
	
	public Key getPrivateKey(){
		return privateKey;
		
	}
	
	public Key getCertKey() {
		Certificate cert = null;
		try {
			cert = CertificateFactory.getInstance("X509").generateCertificate(new FileInputStream("cert1.pem"));
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cert.getPublicKey();
	}
	
	public String decrypt(String encData, String key ) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		String data = null;
		Cipher c = null;
		try {
			c = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			c.init(Cipher.UNWRAP_MODE, getCertKey());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Key aesKey = null;
		try {
			aesKey = c.unwrap(new BASE64Decoder().decodeBuffer(key), "AES/CBC/PKCS5Padding", Cipher.SECRET_KEY);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        try {
			c.init(Cipher.DECRYPT_MODE, aesKey);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        byte[] decordedValue = null;
        byte[] decValue = null;
		try {
			decordedValue = new BASE64Decoder().decodeBuffer(encData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			decValue = c.doFinal(decordedValue);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        data = new BASE64Encoder().encode(decValue);
		return data;
	}
	
	public String fakeDecrypt(String encData, String key) {
		Security.addProvider(new BouncyCastleProvider());
		Cipher c = null;
		try {
			c = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] aesKey = null;
		try {
			aesKey = new BASE64Decoder().decodeBuffer(key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Key simmetricKey = new SecretKeySpec(aesKey, 0, aesKey.length, "AES/ECB/PKCS7Padding");
	    try {
			c.init(Cipher.DECRYPT_MODE, simmetricKey);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    byte[] decordedValue = null;
		try {
			decordedValue = new BASE64Decoder().decodeBuffer(encData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    byte[] decValue = null;
		try {
			decValue = c.doFinal(decordedValue);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    String decryptedValue = new String(decValue);
	    	
	    System.out.println("Vote was succesfully decrypted");
	    return decryptedValue;
	    
	}

}
