package no.uit.zhangwei;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SignatureException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class CapabilityController {
	
	@Autowired
	FileValidator fileValidator;
	
	@RequestMapping(value = "/create_capability", method = RequestMethod.GET)
	public String createCapability(ModelMap model, Principal principal){
		
		String name = principal.getName(); //get logged in username
		
		try {
			Capability cap = new Capability(null, "a.R", "8:00-12:00", false);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String message = name + "'s Capabilities:";
		model.addAttribute("name", name);
		
		return "createCap";
		
	}
	
	@RequestMapping(value = "/execute", method = RequestMethod.GET)
	public String execute(ModelMap model, Principal principal){
		
		String name = principal.getName(); //get logged in username
		
		
		model.addAttribute("name", name);
		
		return "execute";
		
	}
	
	@RequestMapping("/capUpload")
	public ModelAndView fileUploaded(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			BindingResult result, Principal principal) {
		InputStream inputStream = null;
		OutputStream outputStream = null;

		String name = principal.getName(); //get logged in username
		MultipartFile file = uploadedFile.getFile();
		fileValidator.validate(uploadedFile, result);

		String fileName = file.getOriginalFilename();
		String fullPath = null;

		if (result.hasErrors()) {
			return new ModelAndView("uploadForm");
		}

		try {
			inputStream = file.getInputStream();
			fullPath = "C:/Users/zhangwei/files/" + fileName;
			File newFile = new File(fullPath);
			if (!newFile.exists()) {
				newFile.createNewFile();
			}
			outputStream = new FileOutputStream(newFile);
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//generate a cap for target service
		
		//Capability capTS = new Capability(fullPath, SECRET_KEY);
		
		//generate a cap for the user who uploaded the file. 
		
		//Capability capAdmin = new Capability(null, "read", capTS);
		
		
		Image img;
		return new ModelAndView("showResult", "name", name);
		//return new ModelAndView("showFile", "message", fileName);
	}
	

}
