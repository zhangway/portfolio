package no.uit.zhangwei;

public class Caveat {
	
	
	private Object input;
	private String codeRef;
	private Object output;
	private String predicate;
	
	public Caveat(Object input, String codeRef, String predicate){
		this.input = input;
		this.codeRef = codeRef;
		this.predicate = predicate;
	}
	 
	
	public Caveat() {
		// TODO Auto-generated constructor stub
	}

	public String toString(){
		return (input.toString()+codeRef.toString()+output.toString()+predicate.toString());
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
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

}

