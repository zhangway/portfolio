package no.uit.zhangwei;

import java.util.ArrayList;

public class User {
	
	private String name;
	ArrayList<Integer> capidList;
	
	public User(String name){
		this.name = name;
		this.capidList = new ArrayList<Integer>();
	}
	
	public void addCapId(int id){
		this.capidList.add(id);
	}
	
	public ArrayList<Integer> getCapIdLst(){
		return this.capidList;
	}

}
