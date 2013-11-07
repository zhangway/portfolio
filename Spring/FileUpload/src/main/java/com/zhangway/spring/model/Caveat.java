package com.zhangway.spring.model;

public class Caveat {
	
	private String predicate;
	private String operation;
	
	public Caveat(String predicate, String operation){
		this.predicate = predicate;
		this.operation = operation;
	}
	
	public String toString(){
		return(this.predicate+this.operation);
	}
	
	public String getPredicate(){
		return this.predicate;
	}
	
	public String getOperation(){
		return this.operation;
	}

}
