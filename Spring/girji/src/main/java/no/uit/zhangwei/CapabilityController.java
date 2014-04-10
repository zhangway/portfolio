package no.uit.zhangwei;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SignatureException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import no.uit.zhangwei.Model.runkeeper.FitnessActivityFeed;
import no.uit.zhangwei.Model.runkeeper.FitnessActivityFeedItem;
import no.uit.zhangwei.Service.GirjiService;
import no.uit.zhangwei.runkeeper.ClientException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPRaw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.rits.cloning.Cloner;

@Lazy
@Controller
public class CapabilityController {

	@Autowired
	FileValidator fileValidator;
	@Autowired
	GirjiService girjiService;
	private Client client;
	
	
	
	
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String printWelcome(ModelMap model, Principal principal) {
 
		String name = principal.getName(); //get logged in username
		User user = this.girjiService.findUser(name);
		if(user == null){
			this.girjiService.addUser(name);
		}
		String message = "Hello " + name + ", Welcome to Girji!";
		model.addAttribute("message", message);
		return "hello";
 
	}
	
	
	@RequestMapping(value = "/createcap", method = RequestMethod.GET)
	public String createCap(ModelMap model, Principal principal) {
 
		return "newCap";
 
	}
	

	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registerService(ModelMap model, Principal principal) {
 
		return "register";
 
	}
	
	@RequestMapping("/registerForm")
	public String registerForm(@RequestParam("name") String name, @RequestParam("description") String description, 
			@RequestParam("codeRef") String[] codeRef, @RequestParam("param") String[] param, ModelMap model,Principal principal) {
	    
		// Processing
		String provider = principal.getName(); // get logged in username
		ConsentRequest cro = new ConsentRequest(provider, name, description);
		cro.setOperations(codeRef, param);
		this.girjiService.addCRO(cro);
		
		model.addAttribute("reqView", this.girjiService.getCROList());
		
		return "reqList";
		
		
	}
	
	@RequestMapping("/choose")
	public String choose(@RequestParam("radio") String value, Model model){
		
		//String value = request.getParameter("radio"); 
		
		System.out.println(value);
		
		ConsentRequest view = null;
		
		int size = this.girjiService.getCROList().size();
		
		for(int i = 0; i < size; i++){
			ConsentRequest v = this.girjiService.getCROList().get(i);
			if(v.getName().equals(value)){
				view = v;
			}
		}
		
		model.addAttribute("reqView", view);
		
		
		return "next";
	}
	
	@RequestMapping("/finish")
	public String consent(@RequestParam("accessPeriod") String accessPeriod, 
						  @RequestParam("allow") String allowDelegation, 
						  @RequestParam("name") String name, 
						  Model model,  Principal principal){
		
		System.out.println(accessPeriod);
		System.out.println(allowDelegation);
		System.out.println(name);
		
		ArrayList<Operation> o = null;
		ConsentRequest req = null;
		int size = this.girjiService.getCROList().size();
		for(int i = 0; i < size; i++){
			req = this.girjiService.getCROList().get(i);
			if(req.getName().equals(name)){
				 o = req.getOperations();
			}
		}
		
		//create a code consent object
		CodeConsent cc = new CodeConsent();
		cc.setName(name);
		cc.setDescription(req.getDescription());
		cc.setDataOwnerId(principal.getName());
		cc.setPrincipalId(principal.getName());	
		cc.setRevoked(false);
		cc.setOperations(o);
		cc.setConstraints(accessPeriod, allowDelegation);
		
		//a capability is derived from the code consent object		
		Capability cap = new Capability(cc);
		this.girjiService.addCap(cap, cap.getRequester());
		
		//generate the xml file		
		generateXML(cap);	
		
		String message = "Hello " + principal.getName() + ", Welcome to Girji!";
		model.addAttribute("message", message);
		return "hello";
		
	}
	
	private void generateXML(Capability cap){
		String workingDir = System.getProperty("user.dir");

		String capabilityFullPath = workingDir + "\\" + cap.getName() + ".xml";
		try {

			File file2 = new File(capabilityFullPath);
			JAXBContext jaxbContext = JAXBContext.newInstance(Capability.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(cap, file2);
			jaxbMarshaller.marshal(cap, System.out);

		} catch (JAXBException ex) {
			ex.printStackTrace();
		}
	}
	
	
	@RequestMapping(value = "/mycapabilities", method = RequestMethod.GET)
	public String myCapabilities(ModelMap model, Principal principal) {

		String name = principal.getName(); // get logged in username
		String message = name + "'s Capabilities:";
		User user = this.girjiService.findUser(name);
		if(user == null){
			model.addAttribute("capabilities", null);
		}else{
			ArrayList<Capability> lst = this.girjiService.getUserCapLst(name);
			model.addAttribute("capabilities", lst);
		}
		
		//return "lst";
		return "capabilityList";
	}
	
	@RequestMapping(value = "/getrunkeepercap", method = RequestMethod.GET)
	public String getRunkeeperCap(ModelMap model, Principal principal)  {

		String name = principal.getName(); // get logged in username
		//create a cap with root item
		Capability cap = null;

		cap.setName(name + "'s runkeeper fitnessactivity");
		cap.setDescription("user's runkeeper data");
		String capName = name + "_runkeeper";
		String workingDir = System.getProperty("user.dir");

		String capabilityFullPath = workingDir + "\\" + capName + ".xml";
		try {

			File file2 = new File(capabilityFullPath);
			JAXBContext jaxbContext = JAXBContext.newInstance(Capability.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(cap, file2);
			jaxbMarshaller.marshal(cap, System.out);

		} catch (JAXBException ex) {
			ex.printStackTrace();
		}
		
		//update global capability list
		
		//update user's capability list
		return "capabilityList";
	}

	
	@RequestMapping(value = "/execute", method = RequestMethod.GET)
	public String execute(ModelMap model, Principal principal) {

		String name = principal.getName(); // get logged in username

		model.addAttribute("name", name);

		return "selectCap";

	}
	
	@RequestMapping(value = "/runkeeper", method = RequestMethod.GET)
	public String runkeeper(Model model) {

		model.addAttribute("client_id", "d2831aa1942f4f33ae2ce5dcb86d7e91");
		model.addAttribute("response_type", "code");
		model.addAttribute("redirect_uri",
				"http://localhost:8080/zhangwei/index.htm");
		return "redirect:https://runkeeper.com/apps/authorize";
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(@RequestParam("code") String code, Model model) {

		System.out.println("code: " + code);

		return "index";
	}
	
	

	@RequestMapping(value = "/gettoken", method = RequestMethod.POST)
	public String retrieve(@RequestParam("code") String code, Model model, Principal principal){

		System.out.println("retrieve code: " + code);
		String token = convertToken(code, "d2831aa1942f4f33ae2ce5dcb86d7e91",
				"ec171aefb9b0406187c211c48138f24f",
				"http://localhost:8080/zhangwei/index.htm");
		System.out.println("token: " + token);
		client = new Client(token);
		String dataFilePath = null;
		FitnessActivityFeed fitnessActivities = client.getFitnessActivities();
		FitnessActivityFeedItem[] feedItems = fitnessActivities.getItems();
		String workingDir = System.getProperty("user.dir");
		String user = principal.getName();
		
		ArrayList<FitnessActivityFeedItemView> feedItemsView = new ArrayList<FitnessActivityFeedItemView>();
		BigDecimal thousandths = new BigDecimal(1000);
		long value = 0;
		
		for(FitnessActivityFeedItem i : feedItems){
			FitnessActivityFeedItemView viewItem = new FitnessActivityFeedItemView();
			value = i.getDuration().longValue();
			viewItem.setDuration(String.format("%d:%02d:%02d", value/3600, (value%3600)/60, (value%60)));
			
			viewItem.setStartTime(i.getStartTime());
			viewItem.setTotalCalories(String.valueOf(i.getTotalCalories()));
			
			viewItem.setTotalDistance(Double.toString( (i.getTotalDistance().divide(thousandths, 2, BigDecimal.ROUND_HALF_UP)).doubleValue() ) );
			viewItem.setType(i.getType());
			feedItemsView.add(viewItem);
		}
		
		model.addAttribute("feedItemsView", feedItemsView);
		String name = principal.getName(); // get logged in username
		FileWriter writer = null;
		FitnessActivityFeedItemView f = null;	
		
		try {
			dataFilePath = workingDir + "\\runkeeper.csv";
			writer = new FileWriter(dataFilePath);
			writer.append("user");
		    writer.append(',');
			writer.append("date");
		    writer.append(',');
		    writer.append("type");
		    writer.append(',');
		    writer.append("distance");
		    writer.append(',');
		    writer.append("duration");
		    writer.append(',');
		    writer.append("calories");
		    writer.append('\n');
		    Date date = null;
		    Timestamp timeStamp = null;
		    long t;
		    for(int j = 0; j < feedItemsView.size(); j++){
		    	writer.append(user);
			    writer.append(',');
		    	f = feedItemsView.get(j);
		    	String time = f.getStartTime().split(",")[1].trim();
		    	//System.out.println("time is " + time);
		    	/*
		    	date = new SimpleDateFormat("dd MMM yyyy HH:mm:ss").parse(time);
		    	t = date.getTime();
		    	timeStamp = new Timestamp(t);
		    	writer.append(String.valueOf(timeStamp.getTime()));
			    */
		    	writer.append(time);
			    writer.append(',');
			    writer.append(f.getType());
			    writer.append(',');
			    writer.append(f.getTotalDistance());
			    writer.append(',');
			    writer.append(f.getDuration());
			    writer.append(',');
			    writer.append(f.getTotalCalories());
			    writer.append('\n');
		    }
		    writer.flush();
		    writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
			

		return "rk";
	}

	private String convertToken(String code, String clientId,
			String clientSecret, String redirectUri) {
		HttpClient client = new DefaultHttpClient();

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("grant_type",
				"authorization_code"));
		nameValuePairs.add(new BasicNameValuePair("code", code));
		nameValuePairs.add(new BasicNameValuePair("client_id", clientId));
		nameValuePairs
				.add(new BasicNameValuePair("client_secret", clientSecret));
		nameValuePairs.add(new BasicNameValuePair("redirect_uri", redirectUri));

		try {
			HttpPost post = new HttpPost("https://runkeeper.com/apps/token");
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String entityAsString = EntityUtils.toString(entity);
			Map<String, String> map = new HashMap<String, String>();
			map = (Map<String, String>) new Gson().fromJson(entityAsString,
					map.getClass());
			return map.get("access_token");
		} catch (Exception e) {
			throw new ClientException(e);
		}
	}


	
	@RequestMapping(value = "/download", headers="Accept=*/*", produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void getFile(@RequestParam String file, HttpServletResponse response) {
		System.out.println(file);
		try {
		      // get your file as InputStream
		      InputStream is = new FileInputStream(file);
		      // copy it to response's OutputStream
		      response.setContentType("application/force-download");
		      response.setHeader("Content-Disposition", "attachment; filename="+file.replace(" ", "_"));
		      IOUtils.copy(is, response.getOutputStream());
		      
		      response.flushBuffer();
		    } catch (IOException ex) {
		      System.out.println("Error writing file to output stream. Filename was '" + file + "'");
		      throw new RuntimeException("IOError writing file to output stream");
		    }
	}
	
	@RequestMapping("/capUploaded")
	public String capUpload(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile, 
			BindingResult result, Principal principal, ModelMap model) {
		Capability a = null;
		String jpgName = null;
		String RCodePath = null;
		String workingDir = System.getProperty("user.dir");
		REXPRaw b = null;
		REXP re = null;
		String capFileName = uploadedFile.getFile().getOriginalFilename();

		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(Capability.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			a = (Capability) jaxbUnmarshaller.unmarshal(uploadedFile.getFile()
					.getInputStream());
			// System.out.println(a);

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sig = null;
		String key = null;
		for (int i = 0; i < a.getPolicies().size(); i++) {
			if (i == 0) {
				sig = "123456";
			}
			try {
				sig = HMACSignature.calculateRFC2104HMAC(a.getPolicies()
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
		ArrayList<Policy> policies = a.getPolicies();
		Policy p = null;
		String filePath = null;
		String codeRef = null;
		ArrayList<String> resultList = null;
		Operation o = null;
		String name = principal.getName();
		//execute the capability chain from root item
		String file = "fitnessActivity.csv";
		for (int j = 0; j < policies.size(); j++) {
			p = policies.get(j);
			o = p.getOperation();
			codeRef = o.getCodeRef();
			resultList = this.girjiService.execute(o, file);
			file = this.girjiService.getFile(resultList);
		}
		/*
		for (int j = 0; j < caveats.size(); j++) {
			ca = caveats.get(j);
			if (ca.getCodeRef() != null) {
				codeRef = ca.getCodeRef();
				if (j == 0) {
					if(capFileName.equals("test.xml")){
						filePath = "runkeeper.csv";
						resultList = this.girjiService.execute(filePath, codeRef);
						
					}else{
					filePath = "runkeeper.csv";
					
					resultList = this.girjiService.execute(filePath, name, codeRef);
					}
				} else {
					resultList = this.girjiService.execute(filePath, codeRef);
				}
				try {
					filePath = this.girjiService.getFile(filePath, resultList);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}
		}
		*/
		model.addAttribute("filePath", filePath );
		
		return "result";

		
	}
	
	@RequestMapping("/capNew")
	public String capNew(
			@ModelAttribute("codeRef") String codeRef,
			@RequestParam("description") String description,
			@RequestParam("name") String capName,
			@RequestParam("accessPeriod") String accessPeriod,
			Principal principal) {
		String name = principal.getName();
		Capability cap = null;
		/*
		try {
			cap = new Capability(null, name, codeRef, accessPeriod, false);
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
		*/
		cap.setName(capName);
		cap.setDescription(description);
		
		String workingDir = System.getProperty("user.dir");
		// String capabilityFullPath = workingDir + "\\" + capName + ".ser";
		String capabilityFullPath = workingDir + "\\" + capName + ".xml";
		try {

			File file2 = new File(capabilityFullPath);
			JAXBContext jaxbContext = JAXBContext.newInstance(Capability.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(cap, file2);
			jaxbMarshaller.marshal(cap, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		this.girjiService.addCap(cap, name);
		
		
		return "redirect:/mycapabilities";
		
	}
	
	@RequestMapping("/delegatecap")
	public String delegate(Principal principal, ModelMap model){
		String name = principal.getName(); // get logged in username

		model.addAttribute("name", name);

		return "delegateChooseCap";
	}
	@RequestMapping("/delegatecapUploaded")
	public String delegateUploaded(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			@RequestParam("description") String description,
			@RequestParam("name") String capName,
			@RequestParam("codeRef") String codeRef,
			@RequestParam("accessPeriod") String accessPeriod,ModelMap model,
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
			return "delegationChooseCap";
		}
		//read the uploaded file
		Capability a = null;
		try {

			// File file = new File("C:\\file.xml");

			JAXBContext jaxbContext = JAXBContext.newInstance(Capability.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			a = (Capability) jaxbUnmarshaller.unmarshal(uploadedFile.getFile()
					.getInputStream());
			// System.out.println(a);

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//check signature
		String sig = null;
		String key = null;
		for (int i = 0; i < a.getPolicies().size(); i++) {
			if (i == 0) {
				sig = "123456";
			}
			try {
				sig = HMACSignature.calculateRFC2104HMAC(a.getPolicies()
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
		//create a new cap, add one more caveat
		Cloner cloner=new Cloner();

		Capability delegatedCap =cloner.deepClone(a);
		// clone is a deep-clone of o
		delegatedCap.setName(capName);
		delegatedCap.setDescription(description);
		/*
		try {
			delegatedCap.addCaveat(codeRef, accessPeriod);
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SignatureException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		//generate xml file
		String workingDir = System.getProperty("user.dir");
		// String capabilityFullPath = workingDir + "\\" + capName + ".ser";
		String capabilityFullPath = workingDir + "\\" + capName + ".xml";
		try {

			File file2 = new File(capabilityFullPath);
			JAXBContext jaxbContext = JAXBContext.newInstance(Capability.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(delegatedCap, file2);
			jaxbMarshaller.marshal(delegatedCap, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		//display file download page
		model.addAttribute("filePath", capName+".xml" );
		return "result";
	}
			
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
		/*
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
		*/
		cap.setName(capName);
		cap.setDescription(description);
		/*
		 * Path currentRelativePath = Paths.get(""); String s =
		 * currentRelativePath.toAbsolutePath().toString();
		 * System.out.println("Current relative path is: " + s);
		 */
		String workingDir = System.getProperty("user.dir");
		// String capabilityFullPath = workingDir + "\\" + capName + ".ser";
		String capabilityFullPath = workingDir + "\\" + capName + ".xml";
		try {

			File file2 = new File(capabilityFullPath);
			JAXBContext jaxbContext = JAXBContext.newInstance(Capability.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(cap, file2);
			jaxbMarshaller.marshal(cap, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		
		//this.capabilities.add(cap);
		
		return "redirect:/mycapabilities";
		
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test(ModelMap model, Principal principal) {

		String name = principal.getName(); // get logged in username
		
		//execute the same capability file. The data files are different
		
		Capability a = null;
		String jpgName = null;
		String RCodePath = null;
		String workingDir = System.getProperty("user.dir");
		REXPRaw b = null;
		REXP re = null;
		File capFile = new File(workingDir+"\\test1.xml");
		
		//File file = new File("C:\\file.xml");
		// String capabilityFullPath = workingDir + "\\" + capName + ".ser";
		try {

			// File file = new File("C:\\file.xml");

			JAXBContext jaxbContext = JAXBContext.newInstance(Capability.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			a = (Capability) jaxbUnmarshaller.unmarshal(new FileInputStream(capFile));
			// System.out.println(a);

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sig = null;
		String key = null;
		for (int i = 0; i < a.getPolicies().size(); i++) {
			if (i == 0) {
				sig = "123456";
			}
			try {
				sig = HMACSignature.calculateRFC2104HMAC(a.getPolicies()
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
		ArrayList<Policy> caveats = a.getPolicies();
		Policy ca = null;
		String filePath = null;
		String codeRef = null;
		ArrayList<String> resultList = null;
		String filename;
		DateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm:ss");
		Date date = new Date();
		
		long uploadTime, downloadTime = 0;
		// System.out.println(dateFormat.format(cal.getTime()));
		FileWriter writer = null;

		try {
			writer = new FileWriter(workingDir +"\\measurement2.csv");

			writer.append("Time");
			writer.append(',');
			writer.append("Number of Points");
			writer.append(',');
			writer.append("Number of Policy items");
			writer.append(',');
			writer.append("Execution Time1");
			writer.append(',');
			writer.append("Download Time1");
			writer.append(',');
			writer.append("Execution Time2");
			writer.append(',');
			writer.append("Download Time2");
			writer.append(',');
			writer.append("Execution Time3");
			writer.append(',');
			writer.append("Download Time3");
			writer.append('\n');

			while (true) {
				for (int k = 1000; k <= 10000; k += 1000) {
					
					writer.append(dateFormat.format(Calendar.getInstance()
							.getTime()));
					writer.append(',');
					writer.append(Integer.toString(k));
					writer.append(',');
					
					
					int size = caveats.size();
					// execute the capability chain from root item
					/*
					for (int j = 0; j < size; j++) {
						writer.append(Integer.toString(size));
						writer.append(',');						
						
						ca = caveats.get(j);
						if (ca.getCodeRef() != null) {
							codeRef = ca.getCodeRef();
							if(j == 0){
								filePath = "runkeeper" + "_" + k + ".csv";
								if(size == 1){
									resultList = this.girjiService.execute(filePath, codeRef);
								}else{
									resultList = this.girjiService.execute(filePath, name, codeRef);
								}
							}else{
								resultList = this.girjiService.execute(filePath, codeRef);
							}
							writer.append(Long.toString(this.girjiService.uploadTime));
							writer.append(',');
							try {
								filePath = this.girjiService.getFile(filePath,
										resultList, k, j);
							} catch (ClientProtocolException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							writer.append(Long.toString(this.girjiService.downloadTime));

						}
					}
					*/
					writer.append('\n');
					writer.flush();
					
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		

			
		model.addAttribute("filePath", filePath );
		
		return "result";

		
	}

}
