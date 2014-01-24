package no.uit.zhangwei;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Capability {
	
	private static final String KEY = "123456";
	private String nonce;		
	private ArrayList<Caveat> caveats; 
	private String signature;
	
	public Capability(String resourceId, String secretKey){
		
		Date today = new Date();
		Format formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		this.nonce = formatter.format(today);
		
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
	
	public Capability(Object input, String codeRef, String predicate, Boolean allowDelegation) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException{
		
		Date today = new Date();
		Format formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		this.nonce = formatter.format(today);
		String sigRoot = null;
		
		this.caveats = new ArrayList<Caveat>();
		for(int i = 0; i < 2; i++){
			this.caveats.add(new Caveat());
			if(i == 0){
				sigRoot = HMACSha1Signature.calculateRFC2104HMAC(caveats.get(i).toString(), KEY);
			}else{
				this.signature = HMACSha1Signature.calculateRFC2104HMAC(caveats.get(i).toString(), sigRoot);
			}
			
		}
		Caveat e = new Caveat(input, codeRef, predicate);
		this.caveats.add(e);
		for(int j = 2; j < this.caveats.size(); j++){
			this.signature = HMACSha1Signature.calculateRFC2104HMAC(caveats.get(j).toString(), this.signature);
		}
		
		
		
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