package no.uit.zhangwei;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Capability implements Serializable{

	
	private String nonce;
	private ArrayList<Caveat> caveats;
	private String signature;



	public Capability(Object input, String codeRef, String predicate,
			Boolean allowDelegation) throws InvalidKeyException,
			SignatureException, NoSuchAlgorithmException {

		Date today = new Date();
		Format formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		this.nonce = formatter.format(today);
		String sigRoot = null;

		this.caveats = new ArrayList<Caveat>();
		String strCaveat = null;

		for (int i = 0; i < 2; i++) {
			Caveat caveat = new Caveat();
			//root
			if (i == 0) {
				caveat.setOperation("read, write, delete");
				
				this.caveats.add(caveat);
				//strCaveat = this.caveats.get(i).toString();
				sigRoot = HMACSha1Signature
						.calculateRFC2104HMAC(this.caveats
								.get(i).toString(), "123456");
			} else {
				caveat.setOperation("read, write");
				this.caveats.add(caveat);

				this.signature = HMACSha1Signature.calculateRFC2104HMAC(this.caveats
						.get(i).toString(), sigRoot);
			}

		}
		Caveat e = new Caveat(input, codeRef, predicate);
		this.caveats.add(e);
		for (int j = 2; j < this.caveats.size(); j++) {
			this.signature = HMACSha1Signature.calculateRFC2104HMAC(caveats
					.get(j).toString(), this.signature);
		}
		

	}

	public String getNonce() {
		return this.nonce;
	}

	public ArrayList<Caveat> getCaveats() {
		return this.caveats;
	}

	public String getSignature() {
		return this.signature;
	}

}