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
@XmlType(propOrder = { "name", "description", "dataOwner", "requester", "policies", "signature"})
public class Capability {

	private int id;
	private String name;
	private String dataOwner;
	private String requester;
	private ArrayList<Policy> policies;
	private String description;
	private String signature;
	
	public Capability(){
		
	}


	public Capability(CodeConsent cc){
		this.policies = new ArrayList<Policy>();
		this.id++;
		this.name = cc.getName();
		this.dataOwner = cc.getDataOwnerId();
		this.requester = cc.getPrincipalId();
		this.description = cc.getDescription();
		String sig = "123456";
		for(int i = 0; i < cc.getOperations().size(); i++){
			Policy p = new Policy();
			p.setOperation(cc.getOperations().get(i));
			p.setConstraints(cc.getConstraints());
			this.policies.add(p);
			sig = signature(sig, p);
		
		}
		this.signature = sig;
	}
	
	private String signature(String key, Policy p){
		String s = null;
		try {
			s = HMACSignature.calculateRFC2104HMAC(p.toString(), key);
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
		return s;
	}


	/*

	public Capability(Object input, String codeRef, String predicate,
			Boolean allowDelegation) throws InvalidKeyException,
			SignatureException, NoSuchAlgorithmException {

		this();
		String sigRoot = null;
		
		
		String strCaveat = null;

		for (int i = 0; i < 2; i++) {
			Policy policy = new Policy();
			//root
			if (i == 0) {
				caveat.setOperation("read, write, delete");
				caveat.setCodeRef(codeRef);
				this.caveats.add(caveat);
				//strCaveat = this.caveats.get(i).toString();
				sigRoot = HMACSignature
						.calculateRFC2104HMAC(this.caveats
								.get(i).toString(), "123456");
			} else {
				caveat.setOperation("read, write");
				caveat.setCodeRef(codeRef);
				this.caveats.add(caveat);

				this.signature = HMACSignature.calculateRFC2104HMAC(this.caveats
						.get(i).toString(), sigRoot);
			}

		}
		Policy e = new Policy(input, codeRef, predicate);
		this.caveats.add(e);
		for (int j = 2; j < this.caveats.size(); j++) {
			this.signature = HMACSignature.calculateRFC2104HMAC(caveats
					.get(j).toString(), this.signature);
		}
		

	}
	*/
	/*
	//root capability
	public Capability(Object input, String user, String codeRef,
			String predicate, Boolean allowDelegation)
			throws InvalidKeyException, SignatureException,
			NoSuchAlgorithmException {

		this();
		// first item: root, second item: owner
		String sigRoot = null;

		String strCaveat = null;

		Policy caveat = new Policy();
		

		//caveat.setOperation("read, write, delete");
		caveat.setCodeRef(codeRef);
		caveat.setPredicate(predicate);
		this.caveats.add(caveat);
		long t1=System.currentTimeMillis();  
		this.signature = HMACSignature.calculateRFC2104HMAC(caveat.toString(), "123456");
		long t=System.currentTimeMillis()-t1; 
		System.out.println("HMAC root caveat latency: " + t);

	}
	*/
	/*
	public void addCaveat(String codeRef, String predicate) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException{
		
		Policy e = new Policy(null, codeRef, predicate);
		this.caveats.add(e);
		for(int i = 0; i < caveats.size(); i++){
			long t1=System.currentTimeMillis(); 
			if(i == 0){
				this.signature = HMACSignature.calculateRFC2104HMAC(this.caveats.get(i).toString(), "123456");
			}else{
				
				this.signature = HMACSignature.calculateRFC2104HMAC(this.caveats.get(i).toString(), this.signature);
				
			}
			long t=System.currentTimeMillis()-t1; 
			System.out.println("HMAC " + i + " caveat latency: " + t);
		}
		
	}
	*/
	
	public int getId(){
		return this.id;
	}
	
	public String getDataOwner() {
		return dataOwner;
	}
	@XmlElement
	public void setDataOwner(String dataOwner) {
		this.dataOwner = dataOwner;
	}

	public String getRequester() {
		return requester;
	}
	@XmlElement
	public void setRequester(String requester) {
		this.requester = requester;
	}

	public ArrayList<Policy> getPolicies() {
		return policies;
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
	@XmlElementWrapper(name = "policies")
	@XmlElement(name="policy")
	public void setPolicies(ArrayList<Policy> policies) {
		this.policies = policies;
	}

	public ArrayList<Policy> getPolicies() {
		return this.policies;
	}
	@XmlElement
	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignature() {
		return this.signature;
	}

}