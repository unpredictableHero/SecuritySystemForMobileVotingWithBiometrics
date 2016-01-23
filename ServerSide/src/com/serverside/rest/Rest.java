package com.serverside.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.annotation.MultipartConfig;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.serverside.biometrics.FaceDetection;
import com.serverside.biometrics.FaceRecognition;
import com.serverside.jdbc.JdbcVerifier;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.multipart.MultiPart;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

@Path("/voting")
public class Rest {
	
	/*@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hello Jersey";
	}*/
	
	/*@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes("application/x-www-form-urlencoded")
	public String verifyCNP(@QueryParam("cnp") String cnp, @QueryParam("imei") String id) {
		
		JdbcVerifier jdbcVer = new JdbcVerifier();
		if(jdbcVer.verifyCnp(id, cnp) == 1) {
			return "1";
		}
		
		return "0";	
		
	}*/
	
	/*@POST
	@Path("/verify")
	//@Path("/verify")
	@Consumes("multipart/form-data") 
	public String verifyCNP(MultiPart multiPart) {
		
		
		BodyPartEntity bodyPartId = (BodyPartEntity) multiPart.getBodyParts().get(0).getEntity();
		String cnp = "";
		try {
			cnp = getString(bodyPartId.getInputStream());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		BodyPartEntity bodyPartImg = (BodyPartEntity) multiPart.getBodyParts().get(0).getEntity();
		
		InputStream img = bodyPartImg.getInputStream();
		
		BodyPartEntity bodyPartImei = (BodyPartEntity) multiPart.getBodyParts().get(2).getEntity();
		String imei = "";
		try {
			imei = getString(bodyPartImei.getInputStream());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//byte[] pictureBytes = img.getBytes();
		
		byte[] buffer = new byte[32 * 1024];


		try {
			FileOutputStream fos = new FileOutputStream(new File("D:\\ase\\disertatie\\openCvPicturesTesting\\VoteImg.jpg"));
			int bytesRead;
		    while ((bytesRead = img.read(buffer)) != -1) {
		    	fos.write(buffer, 0, bytesRead);
		    }
			fos.close();
		} catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		JdbcVerifier jdbcVer = new JdbcVerifier();
		if(jdbcVer.verifyCnp(imei, cnp) == 1) {
			//verify identity using biometrics
			
			//faceDetection
			FaceDetection.detectFace("D:\\ase\\disertatie\\openCvPicturesTesting\\picture.jpg", 
					"D:\\ase\\disertatie\\openCvPicturesTesting\\pictureOutput.jpg");
			
			//faceRecognition
			String result = FaceRecognition.recognizeFace("D:\\ase\\disertatie\\openCvPicturesTesting\\pictureOutput.jpg",
					"D:\\ase\\disertatie\\openCvPicturesTesting\\faces");
			if("1".equals(result)) {
				return "1";
			} else {
				return "0";
			}
			
			
		}
		
		return "0";	
		
	}*/
	
	@POST
	@Path("/verify")
	@Produces(MediaType.TEXT_PLAIN)
	//@Consumes("application/x-www-form-urlencoded")
	public String verifyCNP(@FormParam("cnp") String cnp, @FormParam("imei") String id,
			@FormParam("img") String img) {
		byte[] data = null;
		
		try {
			data = Base64.decode(img);
		} catch (Base64DecodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File("D:\\ase\\disertatie\\openCvPicturesTesting\\VoteImg.jpg"));
			fos.write(data);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JdbcVerifier jdbcVer = new JdbcVerifier();
		if(jdbcVer.verifyCnp(id, cnp) == 1) {
			//verify identity using biometrics
			
			//faceDetection
			FaceDetection.detectFace("D:\\ase\\disertatie\\openCvPicturesTesting\\VoteImg.jpg", 
					"D:\\ase\\disertatie\\openCvPicturesTesting\\pictureOutput.jpg");
			
			//faceRecognition
			String result = FaceRecognition.recognizeFace("D:\\ase\\disertatie\\openCvPicturesTesting\\pictureOutput.jpg",
					"D:\\ase\\disertatie\\openCvPicturesTesting\\faces");
			if("1".equals(result)) {
				return "1";
			} else {
				return "0";
			}
			
			
		}
		return "0";
	}
	
	@POST 
	@Path("/cast")
	@Produces(MediaType.TEXT_PLAIN)
	public String insertVote(@FormParam("choice") String choice, @FormParam("key") String key) {
		JdbcVerifier jdbcVer = new JdbcVerifier();
		int result = jdbcVer.castVote(choice, key);
		if(result == 1) {
			return "1";
		} else {
			return "0";
		}
	}
	
	
	private String getString(InputStream is) throws Exception {
	    StringBuilder builder = new StringBuilder();
	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
	        String line;
	        while((line = reader.readLine()) != null) {
	            builder.append(line).append("\n");
	        }
	    }
	    return builder.toString();
	} 
	
	@GET
	@Path("/results")
	@Produces(MediaType.TEXT_PLAIN)
	public String results() {
		JdbcVerifier jdbcVer = new JdbcVerifier();
		return jdbcVer.results();
	}
	
	
	@POST
	@Path("/lost")
	@Produces(MediaType.TEXT_PLAIN)
	public String lostPhone(@FormParam("cnp") String cnp, @FormParam("imei") String id,
			@FormParam("img") String img, @FormParam("ans1") String ans1, @FormParam("ans2") String ans2) {
		byte[] data = null;
		
		try {
			data = Base64.decode(img);
		} catch (Base64DecodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File("D:\\ase\\disertatie\\openCvPicturesTesting\\LostImg.jpg"));
			fos.write(data);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JdbcVerifier jdbcVer = new JdbcVerifier();
		if(jdbcVer.verifyCnp(id, cnp) == 1 && jdbcVer.verifyAnswers(id, ans1, ans2) == 1) {
			//verify identity using biometrics
			
			//faceDetection
			FaceDetection.detectFace("D:\\ase\\disertatie\\openCvPicturesTesting\\LostImg.jpg", 
					"D:\\ase\\disertatie\\openCvPicturesTesting\\pictureOutput.jpg");
			
			//faceRecognition
			String result = FaceRecognition.recognizeFace("D:\\ase\\disertatie\\openCvPicturesTesting\\pictureOutput.jpg",
					"D:\\ase\\disertatie\\openCvPicturesTesting\\faces");
			if("1".equals(result)) {
				
				jdbcVer.updateImei(id, cnp);
				return "1";
			} else {
				return "0";
			}
			
			
		}
		return "0";
	}
	
}
