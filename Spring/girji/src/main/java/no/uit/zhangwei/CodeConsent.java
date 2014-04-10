package no.uit.zhangwei;

import java.util.ArrayList;

public class CodeConsent {
	
	private String dataOwnerId;
	private String principalId;
	private boolean revoked;

	private String name;
	private String description;
	
	private ArrayList<Operation> operations;
	private Constraints constraints;
	
	public CodeConsent(){
		this.operations = new ArrayList<Operation>();
		this.constraints = new Constraints();
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	
	public String getDataOwnerId() {
		return dataOwnerId;
	}
	public void setDataOwnerId(String dataOwnerId) {
		this.dataOwnerId = dataOwnerId;
	}
	public String getPrincipalId() {
		return principalId;
	}
	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}
	public boolean isRevoked() {
		return revoked;
	}
	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}

	public ArrayList<Operation> getOperations() {
		return operations;
	}
	public void setOperations(ArrayList<Operation> operations) {
		this.operations = operations;
	}
	public Constraints getConstraints() {
		return constraints;
	}
	public void setConstraints(Constraints constraints) {
		this.constraints = constraints;
	}
	
	public void setConstraints(String accessPeriod, String allowDelegation){
		System.out.println(accessPeriod);
		this.constraints.setAccessPeriod(accessPeriod);
		boolean boolean2 = Boolean.parseBoolean(allowDelegation);
		this.constraints.setAllowDelegation(boolean2);
	}


}
