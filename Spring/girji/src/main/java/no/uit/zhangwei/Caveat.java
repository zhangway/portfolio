package no.uit.zhangwei;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

public class Caveat {
	
	private String id;
	private Object input;
	private String codeRef;
	private Object output;
	private String operation;
	private String predicate;
	
	public Caveat(Object input, String codeRef, String predicate){
		this.input = input;
		this.codeRef = codeRef;
		this.predicate = predicate;
	}
	
	public Caveat(String codeRef){
		
		this.codeRef = codeRef;
		
	}
	
	public Caveat(){
		
	}
	
	public Caveat(Object input, String codeRef, Object output, String operation, String predicate) {
		
		this.input = input;
		this.codeRef = codeRef;
		this.output = output;
		this.operation = operation;
		this.predicate = predicate;
	
	}

	public String toString(){
		String str = "";
		if(this.input != null){
			str = str + this.input.toString();
		}
		if(this.codeRef != null){
			str = str + this.codeRef;
		}
		if(this.output != null){
			str = str + this.output;
		}
		if(this.operation != null){
			str = str + this.operation;
		}
		if(this.predicate != null){
			str = str + this.predicate;
		}
		
		return str;
	}

	public String getOperation() {
		return operation;
	}
	@XmlElement
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public Object getInput() {
		return input;
	}
	public void setInput(Object input) {
		this.input = input;
	}
	public String getCodeRef() {
		return codeRef;
	}
	public void setCodeRef(String codeRef) {
		this.codeRef = codeRef;
	}
	public Object getOutput() {
		return output;
	}
	public void setOutput(Object output) {
		this.output = output;
	}
	public String getPredicate() {
		return predicate;
	}
	@XmlElement
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

}

