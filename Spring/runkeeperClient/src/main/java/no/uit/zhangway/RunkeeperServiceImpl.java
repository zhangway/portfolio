package no.uit.zhangway;

import org.springframework.web.client.RestOperations;

public class RunkeeperServiceImpl implements RunkeeperService {
	
	private String sparklrTrustedMessageURL;
	private String sparklrPhotoURLPattern;
	private RestOperations sparklrRestTemplate;
	private RestOperations trustedClientRestTemplate;
	
	private String sparklrPhotoListURL;
	
	public void setSparklrPhotoListURL(String sparklrPhotoListURL) {
		this.sparklrPhotoListURL = sparklrPhotoListURL;
	}
	public void setSparklrTrustedMessageURL(String sparklrTrustedMessageURL) {
		this.sparklrTrustedMessageURL = sparklrTrustedMessageURL;
	}
	public void setSparklrPhotoURLPattern(String sparklrPhotoURLPattern) {
		this.sparklrPhotoURLPattern = sparklrPhotoURLPattern;
	}
	public void setSparklrRestTemplate(RestOperations sparklrRestTemplate) {
		this.sparklrRestTemplate = sparklrRestTemplate;
	}
	public void setTrustedClientRestTemplate(
			RestOperations trustedClientRestTemplate) {
		this.trustedClientRestTemplate = trustedClientRestTemplate;
	}


}
