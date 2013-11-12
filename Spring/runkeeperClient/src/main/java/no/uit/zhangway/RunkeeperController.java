package no.uit.zhangway;

import org.springframework.stereotype.Controller;



@Controller
public class RunkeeperController {	

	private RunkeeperService runkeeperService;
	
	/*
	@RequestMapping("/connectrunkeeper")
	public String photos(Model model) throws Exception {
		model.addAttribute("photoIds", sparklrService.getSparklrPhotoIds());
		model.addAttribute("path", "photos");
		return "sparklr";
	}
	*/
	public void setRunkeeperService(RunkeeperService runkeeperService) {
		this.runkeeperService = runkeeperService;
	}
	

}
