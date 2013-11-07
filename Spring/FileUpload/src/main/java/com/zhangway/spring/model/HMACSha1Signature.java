package com.zhangway.spring.model;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMACSha1Signature {
	
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	public static String calculateRFC2104HMAC(String data, String key)
			throws SignatureException, NoSuchAlgorithmException,
			InvalidKeyException {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		return toHexString(mac.doFinal(data.getBytes()));
	}
	
	private static String toHexString(byte[] bytes) {
		Formatter formatter = new Formatter();
		
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}
 
		return formatter.toString();
	}
	
	public static void main(String[] args) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {

		String hmac = calculateRFC2104HMAC("http://public.opencpu.org/ocpu/library/", "591102Zw");

		System.out.println(hmac);
		assert hmac.equals("cd68ba160d04e6d4ecb99b44070c874b8fbcf46a");

	}

}
