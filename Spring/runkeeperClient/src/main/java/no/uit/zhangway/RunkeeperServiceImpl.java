package no.uit.zhangway;

import org.springframework.web.client.RestOperations;

public class RunkeeperServiceImpl implements RunkeeperService {
	
	private String sparklrPhotoListURL;
	private String sparklrTrustedMessageURL;
	private String sparklrPhotoURLPattern;
	private RestOperations sparklrRestTemplate;
	private RestOperations trustedClientRestTemplate;

}
