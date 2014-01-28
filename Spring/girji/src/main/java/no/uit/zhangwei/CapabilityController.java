package no.uit.zhangwei;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SignatureException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletContext;

import org.apache.commons.codec.language.Caverphone2;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPRaw;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Controller
public class CapabilityController {

	@Autowired
	FileValidator fileValidator;
	ServletContext servletContext;
	ArrayList<Capability> capabilities;

	@RequestMapping(value = "/create_capability", method = RequestMethod.GET)
	public String createCapability(ModelMap model, Principal principal) {
		this.capabilities = new ArrayList<Capability>();
		return "createCap";

	}
	
	@RequestMapping(value = "/mycapabilities", method = RequestMethod.GET)
	public String myCapabilities(ModelMap model, Principal principal) {
 
		String name = principal.getName(); //get logged in username
		String message = name + "'s Capabilities:";
		model.addAttribute("capabilities", this.capabilities);
		return "capabilities";
 
	}

	/*
	 * @RequestMapping(value = "/create_capability", method = RequestMethod.GET)
	 * public String createCapability(ModelMap model, Principal principal) {
	 * 
	 * String name = principal.getName(); // get logged in username Capability
	 * cap = null; try { cap = new Capability(null, "./upload/girji.R",
	 * "8:00-12:00", false); } catch (InvalidKeyException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch
	 * (SignatureException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (NoSuchAlgorithmException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } try {
	 * 
	 * FileOutputStream fout = new FileOutputStream(
	 * "c:\\users\\zw\\spring\\girji\\capability.ser"); ObjectOutputStream oos =
	 * new ObjectOutputStream(fout); oos.writeObject(cap); oos.close();
	 * System.out.println("Done");
	 * 
	 * } catch (Exception ex) { ex.printStackTrace(); } // String message = name
	 * + "'s Capabilities:"; model.addAttribute("name", name);
	 * 
	 * return "createCap";
	 * 
	 * }
	 */
	@RequestMapping(value = "/execute", method = RequestMethod.GET)
	public String execute(ModelMap model, Principal principal) {
		
		

		String name = principal.getName(); // get logged in username

		model.addAttribute("name", name);
		
		return "selectCap";

	}
	@RequestMapping("/capUpload")
	public ResponseEntity<byte[]> capUploaded(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,			
			BindingResult result, Principal principal,ModelMap model) {
	Capability a = null;
	String jpgName = null;
	String RCodePath = null;
	String workingDir = System.getProperty("user.dir");
	REXPRaw b = null;
	//String capabilityFullPath = workingDir + "\\" + capName + ".ser";
	
	try {
		// use buffering
		//InputStream file = new FileInputStream(uploadedFile.getFile());
		InputStream buffer = new BufferedInputStream(uploadedFile.getFile().getInputStream());
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
	for (int i = 0; i < a.getCaveats().size(); i++) {
		if (i == 0) {
			sig = "123456";
		}
		try {
			sig = HMACSha1Signature.calculateRFC2104HMAC(a.getCaveats()
					.get(i).toString(), sig);
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

	if (sig.equals(a.getSignature())) {
		System.out.println("validated");
	} else {
		System.out.println("signature tampered");
	}
	ArrayList<Caveat> caveats = a.getCaveats();
	Caveat ca = null;

	for (int j = 2; j < caveats.size(); j++) {
		ca = caveats.get(j);

		if (ca.getCodeRef() != null) {
			RConnection c = null;
			
			try {
				c = new RConnection("129.242.19.118");
			} catch (RserveException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			RCodePath = "/home/wei" + ca.getCodeRef().substring(1);
			System.out.println("RCodePath: " + RCodePath);
			RList r = null;
			try {
				
				r = c.parseAndEval("source(\"" + RCodePath + "\")")
						.asList();

			} catch (REngineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (REXPMismatchException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			b = (REXPRaw) r.get("value");
			
			/*		
			try {
				byte[] bytes = b.asBytes();
				jpgName = "c:\\users\\zhangwei\\spring\\girji\\src\\main\\webapp\\resources\\images\\" + a.getName() + ".jpg";
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File(jpgName)));
				stream.write(bytes);
				stream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			*/
		}
	}
	
	final HttpHeaders headers = new HttpHeaders();
	return new ResponseEntity<byte[]>(b.asBytes(), headers, HttpStatus.CREATED);
	

}
	//@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	@RequestMapping("/fileUpload")
	public String fileUploaded(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			@RequestParam("description") String description,
			@RequestParam("name") String capName,
			@RequestParam("accessPeriod") String accessPeriod,
			BindingResult result, Principal principal) {
		InputStream inputStream = null;
		OutputStream outputStream = null;

		String name = principal.getName(); // get logged in username
		MultipartFile file = uploadedFile.getFile();
		fileValidator.validate(uploadedFile, result);

		String fileName = file.getOriginalFilename();
		String fullPath = null;

		if (result.hasErrors()) {
			// return new ModelAndView("uploadForm");
			return "create_capability";
		}

		// upload the R file to R Server

		JSch jsch = new JSch();
		Session session = null;
		String time = null;
		String fullName = null;

		try {
			session = jsch.getSession("wei", "129.242.19.118", 22);

			session.setConfig("StrictHostKeyChecking", "no");

			session.setPassword("591102Zw");

			session.connect();

			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel;

			Date today = new Date();
			Format formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			time = formatter.format(today);
			fullName = "./upload/" + file.getOriginalFilename();
			// File file = new File("c:\\Users\\zw\\spring\\girji\\test.R");
			sftpChannel.put(file.getInputStream(), fullName);

			sftpChannel.exit();
			session.disconnect();
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Capability cap = null;
		try {
			cap = new Capability(null, fullName, accessPeriod, false);
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
		/*
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current relative path is: " + s);
		*/
		String workingDir = System.getProperty("user.dir");
		String capabilityFullPath = workingDir + "\\" + capName + ".ser";
		/*
		File testFile = new File("");
	    String currentPath = testFile.getAbsolutePath();
	    System.out.println("current path is: " + currentPath);
		*/
		cap.setName(capName);
		cap.setDescription(description);
		try {

			FileOutputStream fout = new FileOutputStream(capabilityFullPath);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(cap);
			oos.close();
			System.out.println(capName+".ser created");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//add the capability to the hashmap
		this.capabilities.add(cap);
		/*
		 * 
		 * try { inputStream = file.getInputStream(); fullPath =
		 * "C:/Users/zhangwei/files/" + fileName; File newFile = new
		 * File(fullPath); if (!newFile.exists()) { newFile.createNewFile(); }
		 * outputStream = new FileOutputStream(newFile); int read = 0; byte[]
		 * bytes = new byte[1024];
		 * 
		 * while ((read = inputStream.read(bytes)) != -1) {
		 * outputStream.write(bytes, 0, read); } outputStream.close(); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		// generate a cap for target service

		// Capability capTS = new Capability(fullPath, SECRET_KEY);

		// generate a cap for the user who uploaded the file.

		// Capability capAdmin = new Capability(null, "read", capTS);
		return "redirect:/mycapabilities";

		// return new ModelAndView("showFile", "message", fileName);
	}

}
