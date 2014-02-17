package no.uit.zhangwei;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlRootElement
@XmlType(propOrder = { "name", "description", "caveats", "signature"})
public class Capability {

	
	private String name;
	private ArrayList<Caveat> caveats;
	private String description;
	private String signature;


	public Capability(){
		this.caveats = new ArrayList<Caveat>();
	}



	public Capability(Object input, String codeRef, String predicate,
			Boolean allowDelegation) throws InvalidKeyException,
			SignatureException, NoSuchAlgorithmException {

		
		String sigRoot = null;

		this.caveats = new ArrayList<Caveat>();
		String strCaveat = null;

		for (int i = 0; i < 2; i++) {
			Caveat caveat = new Caveat();
			//root
			if (i == 0) {
				caveat.setOperation("read, write, delete");
				caveat.setCodeRef(codeRef);
				this.caveats.add(caveat);
				//strCaveat = this.caveats.get(i).toString();
				sigRoot = HMACSha1Signature
						.calculateRFC2104HMAC(this.caveats
								.get(i).toString(), "123456");
			} else {
				caveat.setOperation("read, write");
				caveat.setCodeRef(codeRef);
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
	
	public Capability(Object input, String user, String codeRef, String predicate,
			Boolean allowDelegation) throws InvalidKeyException,
			SignatureException, NoSuchAlgorithmException {

		//first item: root, second item: owner
		String sigRoot = null;
		this.caveats = new ArrayList<Caveat>();
		
		String strCaveat = null;
		for (int i = 0; i < 2; i++) {
			Caveat caveat = new Caveat();
			//root
			if (i == 0) {
				caveat.setOperation("read, write, delete");
				caveat.setCodeRef("/ocpu/library/root/R/root");
				this.caveats.add(caveat);
				//strCaveat = this.caveats.get(i).toString();
				sigRoot = HMACSha1Signature
						.calculateRFC2104HMAC(this.caveats
								.get(i).toString(), "123456");
			} else {
				caveat.setOperation("read, write");
				caveat.setCodeRef(codeRef);
				caveat.setPredicate(predicate);
				this.caveats.add(caveat);

				this.signature = HMACSha1Signature.calculateRFC2104HMAC(this.caveats
						.get(i).toString(), sigRoot);
			}

		}

	}
	
	public String toString(){
		
		return this.name+this.signature;
		
	}
	
	public String getName() {
		return name;
	}
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	@XmlElement
	public void setDescription(String description) {
		this.description = description;
	}
	@XmlElementWrapper(name = "caveats")
	@XmlElement(name="caveat")
	public void setCaveats(ArrayList<Caveat> caveats) {
		this.caveats = caveats;
	}

	public ArrayList<Caveat> getCaveats() {
		return this.caveats;
	}
	@XmlElement
	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignature() {
		return this.signature;
	}

}