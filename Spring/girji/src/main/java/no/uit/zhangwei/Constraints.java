package no.uit.zhangwei;

import javax.xml.bind.annotation.XmlElement;

public class Constraints {
	
	private String accessPeriod;
	private boolean allowDelegation;
	private int version;
	
	public Constraints(){
		
	}
	
	public String getAccessPeriod() {
		return this.accessPeriod;
	}
	@XmlElement
	public void setAccessPeriod(String accessPeriod) {
		this.accessPeriod = accessPeriod;
	}

	public boolean isAllowDelegation() {
		return this.allowDelegation;
	}
	@XmlElement
	public void setAllowDelegation(boolean allowDelegation) {
		this.allowDelegation = allowDelegation;
	}

	public String toString() {

		String str = "";

		if (this.accessPeriod != null) {
			str = str + this.accessPeriod;
		}

		str = str + String.valueOf(this.allowDelegation) + Integer.toString(this.version);

		return str;

	}

}
