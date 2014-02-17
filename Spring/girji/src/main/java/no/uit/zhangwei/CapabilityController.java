package no.uit.zhangwei;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SignatureException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.rosuda.REngine.REXP;
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

import au.com.bytecode.opencsv.CSVReader;

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
	
	
	static final String OPENCPU_SERVER = "http://129.242.19.118";

	
	@RequestMapping(value = "/create_capability", method = RequestMethod.GET)
	public String createCapability(ModelMap model, Principal principal) {
		this.capabilities = new ArrayList<Capability>();
		return "newCap";

	}

	@RequestMapping(value = "/mycapabilities", method = RequestMethod.GET)
	public String myCapabilities(ModelMap model, Principal principal) {

		String name = principal.getName(); // get logged in username

		String message = name + "'s Capabilities:";
		model.addAttribute("capabilities", this.capabilities);
		return "capabilityList";

	}

	@RequestMapping(value = "/opencpu", method = RequestMethod.GET)
	public String opencpu(ModelMap model, Principal principal) throws IOException {

		String name = principal.getName(); // get logged in username
		/*
		String test = "/ocpu/tmp/x0674fb83/files/owner.csv";

		char[] tmp = new char[128];
		int m = 0;
		ArrayList<String> value = new ArrayList<String>();
		int k = 0;
		char c;
		do {
			c = test.charAt(k);
			if (c != '/') {
				tmp[m] = test.charAt(k);
			} else {
				tmp[m] = '\0';
				if (tmp[0] != '\0') {
					value.add(new String(tmp, 0, m));
				}

				m = -1;
			}
			if (k == test.length() - 1) {
				value.add(new String(tmp, 0, m + 1));
			}
			m++;
			k++;
		} while (k < test.length());

		for (String str : value) {
			System.out.println(str);
		}
		 */
		

		

		/*
		 * String url = "http://129.242.19.118/ocpu/library/root/R/root";
		 * HttpPost post = new HttpPost(url); MultipartEntity entity = new
		 * MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );
		 */

		/*
		 * HttpClient client = new DefaultHttpClient(); String url =
		 * "http://129.242.19.118/ocpu/library/"; HttpGet request = new
		 * HttpGet(url); request.addHeader("User-Agent", "girji"); HttpResponse
		 * response = null; try { response = client.execute(request); } catch
		 * (ClientProtocolException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } int statusCode =
		 * response.getStatusLine().getStatusCode(); if (statusCode ==
		 * org.apache.http.HttpStatus.SC_OK) {
		 * 
		 * BufferedReader rd = null; try { rd = new BufferedReader( new
		 * InputStreamReader(response.getEntity().getContent())); } catch
		 * (IllegalStateException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 * 
		 * StringBuffer result = new StringBuffer(); String line = ""; try {
		 * while ((line = rd.readLine()) != null) { result.append(line);
		 * result.append(System.getProperty("line.separator")); } } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * System.out.println(result.toString()); } else { //throw new
		 * ClientException("Unexpected statusCode " + statusCode);
		 * System.out.println("statusCode: " + statusCode); }
		 */
		String message = name + "'s Capabilities:";
		model.addAttribute("capabilities", this.capabilities);
		return "capabilities";

	}
	
	private String getFile(String file, ArrayList<String> result) throws ClientProtocolException, IOException{
		HttpClient httpClient = HttpClientBuilder.create().build();
		String s = result.get(4);
		String f = s.substring(s.lastIndexOf('/')+1);
		String filePath;
		if(file.equalsIgnoreCase(f)){
			filePath = result.get(5);
		}else{
			filePath = result.get(4);
		}
		
		String url = OPENCPU_SERVER + filePath;

		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", "girji");
		HttpResponse response = httpClient.execute(request);

		System.out.println("Response Code : "
				+ response.getStatusLine().getStatusCode());

		BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
		String fileName = filePath.substring(filePath.lastIndexOf('/')+1);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
		int inByte;
		while((inByte = bis.read()) != -1) bos.write(inByte);
		bis.close();
		bos.close();
		return fileName;
	}
	
	private ArrayList<String> execute(String filePath, String name, String codeRef){
		String user = "'" + name + "'";
		HttpClient client = HttpClientBuilder.create().build();
		String url = OPENCPU_SERVER + codeRef;
		HttpPost post = new HttpPost(url);

		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		final File file = new File(filePath);
		FileBody fb = new FileBody(file);

		entityBuilder.addPart("file", fb);
		entityBuilder.addTextBody("user", user);
		final HttpEntity yourEntity = entityBuilder.build();
		post.setEntity(yourEntity);
		System.out.println("Post parameters : " + post.getEntity());
		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int responseCode = response.getStatusLine().getStatusCode();
		
		System.out.println("Response Code : "
				+ responseCode);
		
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String body = "";
		String content = "";
		ArrayList<String> resultList = new ArrayList<String>();
		try {
			while ((body = rd.readLine()) != null) {
				resultList.add(body);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;
	}
	
	private ArrayList<String> execute(String filePath, String codeRef){
		HttpClient client = HttpClientBuilder.create().build();
		
		String url = OPENCPU_SERVER + codeRef;
		HttpPost post = new HttpPost(url);

		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		final File file = new File(filePath);
		FileBody fb = new FileBody(file);

		entityBuilder.addPart("file", fb);
		
		final HttpEntity yourEntity = entityBuilder.build();
		post.setEntity(yourEntity);
		System.out.println("Post parameters : " + post.getEntity());
		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int responseCode = response.getStatusLine().getStatusCode();
		
		System.out.println("Response Code : "
				+ responseCode);
		
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String body = "";
		String content = "";
		ArrayList<String> resultList = new ArrayList<String>();
		try {
			while ((body = rd.readLine()) != null) {
				resultList.add(body);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ResponseEntity<byte[]> test(ModelMap model, Principal principal)
			throws REXPMismatchException {

		RConnection c = null;
		REXPRaw b = null;
		String RCodePath = null;

		try {
			c = new RConnection("129.242.19.118");
		} catch (RserveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		RCodePath = "/home/wei/upload/f.R";
		System.out.println("RCodePath: " + RCodePath);
		RList r = null;
		String device = "jpeg";
		REXP x = null;
		REXP xp = null;
		String myCode = null;

		myCode = "df <- read.csv('/home/wei/upload/zw.csv', sep=';', header=TRUE);jpeg('/home/wei/upload/hello.jpg');"
				+ "bp <- barplot(df$Distance, main='Distance Plot', xlab='Month', ylab='Distance',ylim=c(0,70),names.arg=df$Date);"
				+ "text(bp, df$Distance, labels =df$Distance,  pos=3);"
				+ "dev.off(); r=readBin('/home/wei/upload/hello.jpg','raw',1024*1024); unlink('/home/wei/upload/hello.jpg'); r";
		myCode = "R.version.string";
		myCode = "library(lubridate);"
				+ "df <- read.csv('/home/wei/upload/zhangwei.csv', sep=';', header=TRUE);"
				+ "timeSeriesDatesZ <- as.POSIXct(df$Date, format = '%Y:%j:%H:%M:%S');"
				+ "date <- dmy_hms(df$Date); "
				+ "year <- year(date);"
				+ "month <- month(date, label=FALSE);"
				+ "strMonth <- sprintf('%02d', month);"
				+ "strYear <- sprintf('%d', year);"
				+ "yearmon <- paste(strYear,strMonth, sep='');"
				+ "df$Date <- as.integer(yearmon); "
				+ "data <- aggregate(Distance ~ Date, df, function(x) sum=sum(x)); "
				+ "data <- data[order(data$Date),]; "
				+ "jpeg('/home/wei/upload/hello.jpg'); "
				+ "bp <- barplot(data$Distance, main='Distance Plot', xlab='Month', ylab='Distance',ylim=c(0,70),names.arg=data$Date);"
				+ "text(bp, data$Distance, labels =data$Distance,  pos=3); "
				+ "dev.off();"
				+ "r=readBin('/home/wei/upload/hello.jpg','raw',1024*1024);"
				+ "r";

		try {
			c.assign(".tmp.", myCode);
		} catch (RserveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		REXP re = null;
		try {
			re = c.parseAndEval("try(eval(parse('/home/wei/upload/e.R')),silent=TRUE)");
		} catch (REngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (re.inherits("try-error"))
			System.err.println("Error: " + re.toString());
		else {
			// success .. }

		}
		final HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<byte[]>(re.asBytes(), headers,
				HttpStatus.CREATED);

		/*
		 * REXP xp = c.parseAndEval("try("+device+"('test.jpg',quality=90))");
		 * 
		 * if (xp.inherits("try-error")) { // if the result is of the class
		 * try-error then there was a problem System.err.println(xp.asString());
		 * // this is analogous to 'warnings', but for us it's sufficient to get
		 * just the 1st warning REXP w = c.eval(
		 * "if (exists('last.warning') && length(last.warning)>0) names(last.warning)[1] else 0"
		 * ); if (w.isString()) System.err.println(w.asString());
		 * 
		 * }
		 */

		/*
		 * x = c.eval("R.version.string"); System.out.println(x.asString());
		 */
		/*
		 * r = c.parseAndEval("source(\"" + RCodePath + "\")") .asList();
		 */

		/*
		 * //xp = c.parseAndEval(
		 * "df <- read.csv('/home/wei/upload/zw.csv', sep=';', header=TRUE)");
		 * xp = c.parseAndEval(
		 * "df <- read.csv('/home/wei/upload/zw.csv', sep=';', header=TRUE);jpeg('/home/wei/upload/hello.jpg');"
		 * +
		 * "bp <- barplot(df$Distance, main='Distance Plot', xlab='Month', ylab='Distance',ylim=c(0,70),names.arg=df$Date);"
		 * + "text(bp, df$Distance, labels =df$Distance,  pos=3);" +
		 * "dev.off(); r=readBin('/home/wei/upload/hello.jpg','raw',1024*1024); unlink('/home/wei/upload/hello.jpg'); r"
		 * );
		 * 
		 * } catch (RserveException rse) { // RserveException (transport layer -
		 * e.g. Rserve is not running) System.out.println(rse); } catch
		 * (REXPMismatchException mme) { System.out.println(mme);
		 * mme.printStackTrace(); }catch(Exception e) { // something else
		 * System.out.println("Something went wrong, but it's not the Rserve: "
		 * +e.getMessage()); e.printStackTrace(); } b = (REXPRaw)
		 * r.get("value");
		 */

	}

	@RequestMapping(value = "/execute", method = RequestMethod.GET)
	public String execute(ModelMap model, Principal principal) {

		String name = principal.getName(); // get logged in username

		model.addAttribute("name", name);

		return "selectCap";

	}

	
	@RequestMapping("/capUpload")
	public ResponseEntity<byte[]> capUploaded(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			BindingResult result, Principal principal, ModelMap model) {
		Capability a = null;
		String jpgName = null;
		String RCodePath = null;
		String workingDir = System.getProperty("user.dir");
		REXPRaw b = null;
		REXP re = null;
		// String capabilityFullPath = workingDir + "\\" + capName + ".ser";
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
		/*
		 * try { // use buffering // InputStream file = new
		 * FileInputStream(uploadedFile.getFile()); InputStream buffer = new
		 * BufferedInputStream(uploadedFile.getFile() .getInputStream());
		 * ObjectInput input = new ObjectInputStream(buffer); try { //
		 * deserialize the List // List<String> recoveredQuarks = //
		 * (List<String>)input.readObject(); a = (Capability)
		 * input.readObject(); // display its data
		 * 
		 * 
		 * } finally { input.close(); } } catch (ClassNotFoundException ex) {
		 * ex.printStackTrace(); } catch (IOException ex) {
		 * ex.printStackTrace(); }
		 */
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

		
		for (int j = 0; j < caveats.size(); j++) {
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

				try {
					re = c.parseAndEval("try(eval(parse(\"" + RCodePath
							+ "\")),silent=TRUE)");

				} catch (REngineException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (REXPMismatchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (re.inherits("try-error"))
					System.err.println("Error: " + re.toString());
				else {
					// success ..
					System.out.println("R executed OK");

				}

			}
		}

		final HttpHeaders headers = new HttpHeaders();
		byte[] byt = null;
		try {
			byt = re.asBytes();

		} catch (REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<byte[]>(byt, headers, HttpStatus.CREATED);

	}
	
	@RequestMapping("/capUploaded")
	public String capUpload(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			BindingResult result, Principal principal, ModelMap model) throws ClientProtocolException, IOException {
		Capability a = null;
		String jpgName = null;
		String RCodePath = null;
		String workingDir = System.getProperty("user.dir");
		REXPRaw b = null;
		REXP re = null;
		// String capabilityFullPath = workingDir + "\\" + capName + ".ser";
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
		String filePath = null;
		String codeRef = null;
		ArrayList<String> resultList = null;
		String name = principal.getName();
		//execute the capability chain from root item
		for (int j = 0; j < caveats.size(); j++) {
			ca = caveats.get(j);
			if (ca.getCodeRef() != null) {
				codeRef = ca.getCodeRef();
				if (j == 0) {
					filePath = "test.csv";
					resultList = execute(filePath, name, codeRef);
				} else {
					resultList = execute(filePath, codeRef);
				}
				filePath = getFile(filePath, resultList);

			}
		}
			
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
		
		this.capabilities.add(cap);
		
		return "redirect:/mycapabilities";
		
	}

	// @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
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

		
		this.capabilities.add(cap);
		
		return "redirect:/mycapabilities";
		
	}

}
