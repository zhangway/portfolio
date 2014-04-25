package no.uit.zhangwei;

import java.util.ArrayList;

public class ConsentRequest {
	
	private String name;
	private String description;
	private String provider;
	
	private ArrayList<Operation> operations;
	
	public ConsentRequest(String provider, String name, String description){
		this.provider = provider;
		this.name = name;
		this.description = description;
		this.operations = new ArrayList<Operation>();
	}
	
	public void setOperations(String[] codeRef, String[] param1, String[] param2){
		
		
		
		for(int i = 0; i < codeRef.length; i++){
			if(param1 != null && param1.length>0){
				Operation o = new Operation(codeRef[i], param1[i], param2[i]);
				this.operations.add(o);
				
			}else{
				Operation o = new Operation(codeRef[i], null, null);
				this.operations.add(o);
			}
			
			
		}
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

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public ArrayList<Operation> getOperations() {
		return operations;
	}
	
	

}
