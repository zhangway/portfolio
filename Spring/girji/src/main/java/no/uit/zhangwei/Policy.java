package no.uit.zhangwei;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
@XmlType(propOrder = { "operation", "constraints"})
public class Policy {
	
	private Operation operation;
	private Constraints constraints;
	
	public Policy(){
		
	}
	

	public Operation getOperation() {
		return operation;
	}


	public void setOperation(Operation operation) {
		this.operation = operation;
	}


	public Constraints getConstraints() {
		return constraints;
	}


	public void setConstraints(Constraints constraints) {
		this.constraints = constraints;
	}


	public String toString(){
		String str = "";
		
		if(this.operation != null){
			str = str + this.operation.toString();
		}
		
		if(this.constraints != null){
			str = str + this.constraints.toString();
		}
		
		return str;
	}





}

