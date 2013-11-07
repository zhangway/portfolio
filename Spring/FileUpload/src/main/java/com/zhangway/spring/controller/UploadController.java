package com.zhangway.spring.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.zhangway.spring.model.Capability;
import com.zhangway.spring.model.HMACSha1Signature;
import com.zhangway.spring.model.UploadedFile;
import com.zhangway.spring.validator.FileValidator;

@Controller
public class UploadController {

	@Autowired
	FileValidator fileValidator;
	private static final String SECRET_KEY = "123456csuit";
	Capability capService;

	@RequestMapping("/upload")
	public ModelAndView getUploadForm(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,  //("uploadedFile")这里表示的是接收uploadForm.jsp中的Form的modelAttribute
			BindingResult result) {
		return new ModelAndView("uploadForm");  //这里的uploadForm是一个view reference，String类型，然后ViewResolvers就会将这个view reference解析成一个view的实现，
	}

	@RequestMapping("/fileUpload")
	public ModelAndView fileUploaded(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			BindingResult result) {
		InputStream inputStream = null;
		OutputStream outputStream = null;

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
		
		Capability capTS = new Capability(fullPath, SECRET_KEY);
		
		//generate a cap for the user who uploaded the file. 
		
		Capability capAdmin = new Capability(null, "read", capTS);
		
		
		
		return new ModelAndView("showFile", "capability", capAdmin);
		//return new ModelAndView("showFile", "message", fileName);
	}
}
