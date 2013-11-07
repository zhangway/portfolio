package com.zhangway.spring.model;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Capability {
	
	private String nonce;
	private String resourceId;
	
	private ArrayList<Caveat> caveats; 
	private String signature;
	
	public Capability(String resourceId, String secretKey){
		this.resourceId = resourceId;
		Date today = new Date();
		Format formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		this.nonce = formatter.format(today) + this.resourceId;
		
		try {
			this.signature = HMACSha1Signature.calculateRFC2104HMAC(this.nonce, secretKey);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public Capability(String predicates, String operation, Capability cap){
		this.nonce = cap.nonce;
		this.resourceId = cap.resourceId;
		Caveat caveat = new Caveat(predicates, operation);
		if(cap.caveats == null){ //the cap is ts cap			
			this.caveats = new ArrayList<Caveat>();			
		}else{
			this.caveats = cap.caveats;			
		}
		this.caveats.add(caveat);
		
		try {
			this.signature = HMACSha1Signature.calculateRFC2104HMAC(caveat.toString(), cap.signature);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getResourceId(){
		return this.resourceId;
	}
	
	public String getNonce(){
		return this.nonce;
	}
	
	public ArrayList<Caveat> getCaveats(){
		return this.caveats;
	}
	
	public String getSignature(){
		return this.signature;
	}

}
