package no.uit.zhangwei;

import javax.xml.bind.annotation.XmlElement;

public class Operation {
	
	private String codeRef;
	private String param1;
	private String param2;
	
	public Operation(){
		
	}
	

	
	public String getParam1() {
		return param1;
	}
	public String getParam2() {
		return param2;
	}
	@XmlElement
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	
	@XmlElement
	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getCodeRef() {
		return codeRef;
	}

	public Operation(String codeRef, String param1, String param2){
		this.codeRef = codeRef;
		this.param1 = param1;
		this.param2 = param2;
	}
	
	@XmlElement
	public void setCodeRef(String codeRef) {
		this.codeRef = codeRef;
	}
	
	public String toString() {

		String str = "";

		if (this.codeRef != null) {
			str = str + this.codeRef;
		}

		if (this.param1 != null) {
			str = str + this.param1;
		}
		
		if (this.param2 != null) {
			str = str + this.param2;
		}
	

		return str;

	}

}
