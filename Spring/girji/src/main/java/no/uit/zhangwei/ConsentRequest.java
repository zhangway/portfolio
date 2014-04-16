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
	
	public void setOperations(String[] codeRef, String[] param){
		
		
		
		for(int i = 0; i < codeRef.length; i++){
			if(param != null && param.length>0){
				Operation o = new Operation(codeRef[i], param[i]);
				this.operations.add(o);
				
			}else{
				Operation o = new Operation(codeRef[i], null);
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
