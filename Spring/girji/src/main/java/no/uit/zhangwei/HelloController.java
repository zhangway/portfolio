package no.uit.zhangwei;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPRaw;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller

public class HelloController {
	
	byte[] b;
 
	
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String printWelcome(ModelMap model, Principal principal) {
 
		String name = principal.getName(); //get logged in username
		String message = "Hello " + name + ", Welcome to Girji!";
		model.addAttribute("message", message);
		return "hello";
 
	}
	
	@RequestMapping(value = "/mycapabilities", method = RequestMethod.GET)
	public String myCapabilities(ModelMap model, Principal principal) {
 
		String name = principal.getName(); //get logged in username
		String message = name + "'s Capabilities:";
		model.addAttribute("message", message);
		return "capabilities";
 
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test(ModelMap model, Principal principal)  {
 
		/*
		
		RConnection c = null;;
		try {
			c = new RConnection("129.242.19.118");
		} catch (RserveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		RList r = null;
		try {
			r = c.eval("source(\"/home/kevin/b.R\")").asList();
		} catch (RserveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (REXPMismatchException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		REXPRaw b = (REXPRaw) r.get("value");
		try {
            byte[] bytes = b.asBytes();
            
            
            
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File("C:\\Users\\zhangwei\\spring\\girji\\src\\main\\webapp\\resources\\images\\test1.jpg")));
            stream.write(bytes);
            stream.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
		/*
		FileOutputStream fos = new FileOutputStream("test.jpg");
		try{
			fos.write(b.asBytes());
		}finally{
			fos.close();
		}
		*/
        //REXP x = c.eval("R.version.string");
        //System.out.println(x.asString());
		
		//model.addAttribute("message", x.asString());
		
		return "display";
 
	}
	
//	@RequestMapping(value = "/displayResult", method = RequestMethod.GET)
//	public byte[] displayResult(ModelMap model, Principal principal)  {
// 
//		/*
//		RConnection c = new RConnection("129.242.19.118");
//		RList r = c.eval("source(\"/home/kevin/c.R\")").asList();
//		REXPRaw b = (REXPRaw) r.get("value");
//		FileOutputStream fos = new FileOutputStream("c:/users/zhangwei/test.jpg");
//		fos.write(b.asBytes());
//        //REXP x = c.eval("R.version.string");
//        //System.out.println(x.asString());
//		
//		model.addAttribute("message", x.asString());
//		*/
//		File fi = new File("c:/users/zhangwei/a.jpg");
//		try {
//			return (Files.readAllBytes(fi.toPath()));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	}
	
	
 
}