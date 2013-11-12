package no.uit.zhangway;

import org.springframework.stereotype.Controller;

@Controller
public class RunkeeperRedirectController {
	
	public void setRunkeeperService(RunkeeperService runkeeperService) {
		this.runkeeperService = runkeeperService;
	}

	private RunkeeperService runkeeperService;

}
