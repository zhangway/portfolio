package no.uit.zhangwei;

import javax.xml.bind.annotation.XmlElement;

public class Operation {
	
	private String codeRef;
	private String param;
	
	public Operation(){
		
	}
	

	
	public String getParam() {
		return param;
	}
	@XmlElement
	public void setParam(String param) {
		this.param = param;
	}

	public String getCodeRef() {
		return codeRef;
	}

	public Operation(String codeRef, String param){
		this.codeRef = codeRef;
		this.param = param;
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

		if (this.param != null) {
			str = str + this.param;
		}
	

		return str;

	}

}
