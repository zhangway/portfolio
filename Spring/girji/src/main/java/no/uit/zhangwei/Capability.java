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

	private int id;
	private String name;
	private ArrayList<Caveat> caveats;
	private String description;
	private String signature;


	public Capability(){
		this.caveats = new ArrayList<Caveat>();
		this.id++;
	}



	public Capability(Object input, String codeRef, String predicate,
			Boolean allowDelegation) throws InvalidKeyException,
			SignatureException, NoSuchAlgorithmException {

		this();
		String sigRoot = null;
		
		
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
	
	//root capability
	public Capability(Object input, String user, String codeRef,
			String predicate, Boolean allowDelegation)
			throws InvalidKeyException, SignatureException,
			NoSuchAlgorithmException {

		this();
		// first item: root, second item: owner
		String sigRoot = null;

		String strCaveat = null;

		Caveat caveat = new Caveat();
		

		//caveat.setOperation("read, write, delete");
		caveat.setCodeRef(codeRef);
		caveat.setPredicate(predicate);
		this.caveats.add(caveat);
		long t1=System.currentTimeMillis();  
		this.signature = HMACSha1Signature.calculateRFC2104HMAC(caveat.toString(), "123456");
		long t=System.currentTimeMillis()-t1; 
		System.out.println("HMAC root caveat latency: " + t);

	}
	
	public void addCaveat(String codeRef, String predicate) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException{
		
		Caveat e = new Caveat(null, codeRef, predicate);
		this.caveats.add(e);
		for(int i = 0; i < caveats.size(); i++){
			long t1=System.currentTimeMillis(); 
			if(i == 0){
				this.signature = HMACSha1Signature.calculateRFC2104HMAC(this.caveats.get(i).toString(), "123456");
			}else{
				
				this.signature = HMACSha1Signature.calculateRFC2104HMAC(this.caveats.get(i).toString(), this.signature);
				
			}
			long t=System.currentTimeMillis()-t1; 
			System.out.println("HMAC " + i + " caveat latency: " + t);
		}
		
	}
	
	public int getId(){
		return this.id;
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