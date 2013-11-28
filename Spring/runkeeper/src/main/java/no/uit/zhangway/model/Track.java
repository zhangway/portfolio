package no.uit.zhangway.model;



import java.util.List;

public class Track {
	
	private String type;
	private String equipment;
	private String start_time;
	private String comments;
	private List<Path> path;
	
	public Track(String a, String b, String c, String d){
		this.type = a;
		this.equipment = b;
		this.start_time = c;
		this.comments = d;
	}
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public List<Path> getPath() {
		return path;
	}
	public void setPath(List<Path> path) {
		this.path = path;
	}
}
