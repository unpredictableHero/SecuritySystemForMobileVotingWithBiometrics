package com.serverside.rest;

import javax.ws.rs.Consumes;
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
	
	@GET
	@Path("/verify")
	@Produces(MediaType.TEXT_PLAIN)
	public String verifyCNP(@QueryParam("cnp") String cnp, @QueryParam("imei") String id) {
		
		JdbcVerifier jdbcVer = new JdbcVerifier();
		if(jdbcVer.verifyCnp(id, cnp) == 1) {
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
		
	}
	
	@GET 
	@Path("/cast")
	@Produces(MediaType.TEXT_PLAIN)
	public String insertVote(@QueryParam("choice") String choice) {
		JdbcVerifier jdbcVer = new JdbcVerifier();
		int result = jdbcVer.castVote(choice);
		if(result == 1) {
			return "1";
		} else {
			return "0";
		}
	}
}
