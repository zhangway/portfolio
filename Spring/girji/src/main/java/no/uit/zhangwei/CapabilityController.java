package no.uit.zhangwei;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SignatureException;
import java.util.ArrayList;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPRaw;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
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
	public String createCapability(ModelMap model, Principal principal) {

		String name = principal.getName(); // get logged in username
		Capability cap = null;
		try {
			cap = new Capability(null, "./upload/girji.R", "8:00-12:00", false);
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
		try {

			FileOutputStream fout = new FileOutputStream(
					"c:\\users\\zw\\spring\\girji\\capability.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(cap);
			oos.close();
			System.out.println("Done");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// String message = name + "'s Capabilities:";
		model.addAttribute("name", name);

		return "createCap";

	}

	@RequestMapping(value = "/execute", method = RequestMethod.GET)
	public String execute(ModelMap model, Principal principal) {

		String name = principal.getName(); // get logged in username

		model.addAttribute("name", name);
		Capability a = null;
		try {
			// use buffering
			InputStream file = new FileInputStream(
					"c:\\users\\zw\\spring\\girji\\capability.ser");
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			try {
				// deserialize the List
				// List<String> recoveredQuarks =
				// (List<String>)input.readObject();
				a = (Capability) input.readObject();
				// display its data
				/*
				 * for(String quark: recoveredQuarks){
				 * System.out.println("Recovered Quark: " + quark); }
				 */
			} finally {
				input.close();
			}
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		String sig = null;
		String key = null;
		for(int i = 0; i < a.getCaveats().size(); i++){
			if(i == 0){
				sig = "123456";
			}
			try {
				sig = HMACSha1Signature.calculateRFC2104HMAC(a.getCaveats().get(i).toString(), sig);
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
		}
		
		if(sig.equals(a.getSignature())){
			System.out.println("validated");
		}else{
			System.out.println("signature tampered");
		}
		ArrayList<Caveat> caveats = a.getCaveats();
		Caveat ca = null;
		
		for(int j = 0; j < caveats.size(); j++){
			ca = caveats.get(j);
			
			if(ca.getCodeRef() != null){
				RConnection c = null;;
				try {
					c = new RConnection("129.242.19.118");
				} catch (RserveException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				RList r = null;
				try {
					
						r = c.parseAndEval("source(\""+ ca.getCodeRef() + "\")").asList();
					
				}catch (REngineException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (REXPMismatchException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				REXPRaw b = (REXPRaw) r.get("value");
				try {
		            byte[] bytes = b.asBytes();
		            
		            
		            
		            BufferedOutputStream stream =
		                    new BufferedOutputStream(new FileOutputStream(new File("C:\\Users\\zhangwei\\spring\\girji\\src\\main\\webapp\\resources\\images\\girji.jpg")));
		            stream.write(bytes);
		            stream.close();
		            
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			}
		}
		
		return "display";

	}

	@RequestMapping("/capUpload")
	public ModelAndView fileUploaded(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			BindingResult result, Principal principal) {
		InputStream inputStream = null;
		OutputStream outputStream = null;

		String name = principal.getName(); // get logged in username
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

		// generate a cap for target service

		// Capability capTS = new Capability(fullPath, SECRET_KEY);

		// generate a cap for the user who uploaded the file.

		// Capability capAdmin = new Capability(null, "read", capTS);

		Image img;
		return new ModelAndView("showResult", "name", name);
		// return new ModelAndView("showFile", "message", fileName);
	}

}
