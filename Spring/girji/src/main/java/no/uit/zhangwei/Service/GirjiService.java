package no.uit.zhangwei.Service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.uit.zhangwei.Capability;
import no.uit.zhangwei.CodeConsent;
import no.uit.zhangwei.ConsentRequest;
import no.uit.zhangwei.Operation;
import no.uit.zhangwei.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class GirjiService {

	static final String OPENCPU_SERVER = "http://129.242.19.118";
	private ArrayList<Capability> capabilities;
	private ArrayList<CodeConsent> ccoList;
	private ArrayList<ConsentRequest> croList;
	private HashMap<String, User> userMap;
	public long uploadTime;
	public long downloadTime;
	PoolingHttpClientConnectionManager cManager;
	CloseableHttpClient httpClient;

	public GirjiService() {
		this.capabilities = new ArrayList<Capability>();
		this.croList = new ArrayList<ConsentRequest>();
		this.userMap = new HashMap<String, User>();
		this.ccoList = new ArrayList<CodeConsent>();
		this.cManager = new PoolingHttpClientConnectionManager();

		this.cManager.setMaxTotal(5000);
		this.httpClient = HttpClients.custom()
				.setConnectionManager(this.cManager).build();
	}

	public User findUser(String name) {
		User user = null;
		if (this.userMap.containsKey(name)) {
			user = this.userMap.get(name);
		}
		return user;

	}

	public boolean addUser(String name) {
		User user = new User(name);
		if (this.userMap.put(name, user) != null) {
			return true;
		}
		return false;
	}

	public void addCRO(ConsentRequest cro) {
		this.croList.add(cro);
	}

	public ArrayList<ConsentRequest> getCROList() {
		return this.croList;
	}

	public ArrayList<Capability> getUserCapLst(String userName) {
		ArrayList<Integer> idLst = this.userMap.get(userName).getCapIdLst();
		ArrayList<Capability> lst = new ArrayList<Capability>();
		Capability cap = null;
		for (int i = 0; i < idLst.size(); i++) {
			cap = findCap(idLst.get(i));
			lst.add(cap);
		}
		return lst;
	}

	public void addCap(Capability cap, String userName) {
		this.capabilities.add(cap);
		// update this user's capList
		int id = cap.getId();
		this.userMap.get(userName).addCapId(id);
	}

	private Capability findCap(int id) {
		Capability cap = null;
		for (int i = 0; i < this.capabilities.size(); i++) {
			Capability ca = this.capabilities.get(i);
			if (id == ca.getId()) {
				cap = ca;
			}
		}
		return cap;
	}

	public String getFile(String file, ArrayList<String> result, int number,
			int j) throws ClientProtocolException, IOException {
		HttpClient httpClient = HttpClientBuilder.create().build();
		ArrayList<String> files = new ArrayList<String>();
		for (String s : result) {
			if (s.contains("files")) {
				files.add(s);
			}
		}
		String ss = null;
		String f = null;
		int size = files.size();

		String filePath = null;
		for (int i = 0; i < files.size(); i++) {
			ss = files.get(i);
			f = ss.substring(ss.lastIndexOf('/') + 1);

			if (!f.equalsIgnoreCase(file)) {
				filePath = ss;
			}
		}

		String url = OPENCPU_SERVER + filePath;

		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", "girji");

		long t1 = System.currentTimeMillis();

		HttpResponse response = httpClient.execute(request);

		long t = System.currentTimeMillis() - t1;
		this.downloadTime = t;

		System.out.println("get file latency: " + t);

		System.out.println("Response Code : "
				+ response.getStatusLine().getStatusCode());

		String responseString = EntityUtils.toString(response.getEntity(),
				"UTF-8");
		// System.out.println(responseString);

		// BufferedInputStream bis = new
		// BufferedInputStream(response.getEntity().getContent());
		// System.out.println(bis.available());
		String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
		if (j == 3) {
			fileName = fileName + number;
		}
		FileOutputStream fos = new FileOutputStream(new File(fileName));
		byte[] contentInBytes = responseString.getBytes();
		fos.write(contentInBytes);
		fos.flush();
		fos.close();
		/*
		 * int inByte = bis.read(); while(inByte != -1) { bos.write(inByte); }
		 * bis.close(); bos.close();
		 */
		return fileName;
	}

	public String getFile(ArrayList<String> result) {

		HttpClient httpClient = HttpClientBuilder.create().build();

		for (int i = 0; i < result.size(); i++) {
			String s = result.get(i);
			if (s.contains("files")) {

			}
		}

		String url = OPENCPU_SERVER + result.get(0);

		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", "girji");

		long t1 = System.currentTimeMillis();

		HttpResponse response = null;
		try {
			response = httpClient.execute(request);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		long t = System.currentTimeMillis() - t1;
		this.downloadTime = t;
		System.out.println("get file latency: " + t);

		System.out.println("Response Code : "
				+ response.getStatusLine().getStatusCode());

		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(response.getEntity().getContent());
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(
					new File(".val")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int inByte;
		try {
			while ((inByte = bis.read()) != -1)
				bos.write(inByte);
			bis.close();
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ".val";
	}

	public String getFile(String file, ArrayList<String> result)
			throws ClientProtocolException, IOException {
		// HttpClient httpClient = HttpClientBuilder.create().build();
		ArrayList<String> files = new ArrayList<String>();
		for (String s : result) {
			if (s.contains("files")) {
				files.add(s);
			}
		}
		String ss = null;
		String f = null;
		int size = files.size();

		String filePath = null;
		for (int i = 0; i < files.size(); i++) {
			ss = files.get(i);
			f = ss.substring(ss.lastIndexOf('/') + 1);

			if (!f.equalsIgnoreCase(file)) {
				filePath = ss;
			}
		}

		String url = OPENCPU_SERVER + filePath;

		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", "girji");

		long t1 = System.currentTimeMillis();

		CloseableHttpResponse response = this.httpClient.execute(request);

		long t = System.currentTimeMillis() - t1;
		this.downloadTime = t;
		// System.out.println("get file latency: " + t);

		// System.out.println("Response Code : " +
		// response.getStatusLine().getStatusCode());
		int code = response.getStatusLine().getStatusCode();
		if (code != 200) {
			System.out.println("Get Not OK." + code);
			System.exit(0);
		}

		/*
		 * HttpEntity entity = null; entity = response.getEntity();
		 * //EntityUtils.consume(entity); BufferedReader rd = new
		 * BufferedReader( new InputStreamReader(entity.getContent()));
		 * 
		 * 
		 * StringBuffer resultBuf = new StringBuffer(); String line = ""; while
		 * ((line = rd.readLine()) != null) { resultBuf.append(line); } try {
		 * rd.close(); response.close(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * return resultBuf.toString();
		 */

		BufferedInputStream bis = new BufferedInputStream(response.getEntity()
				.getContent());
		String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);

		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(new File(fileName)));
		int inByte;
		while ((inByte = bis.read()) != -1)
			bos.write(inByte);
		bis.close();
		bos.close();
		response.close();
		return fileName;
	}

	public String getResult(ArrayList<String> resultList)
			throws ClientProtocolException, IOException {

		// HttpClient httpClient = HttpClientBuilder.create().build();
		String url = OPENCPU_SERVER + resultList.get(0);

		HttpGet get = new HttpGet(url);

		// add request header
		get.addHeader("User-Agent", "girji");

		long t1 = System.currentTimeMillis();

		CloseableHttpResponse response = this.httpClient.execute(get);

		long t = System.currentTimeMillis();
		this.downloadTime = t - t1;
		// System.out.println("get result latency: " + t);
		int code = response.getStatusLine().getStatusCode();
		if (code != HttpStatus.SC_OK) {
			System.out.println("Get Not OK." + code);
			System.exit(0);
		}
		// System.out.println("Response Code : " +
		// response.getStatusLine().getStatusCode());
		HttpEntity entity = null;
		entity = response.getEntity();
		// EntityUtils.consume(entity);
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				entity.getContent()));

		StringBuffer resultBuf = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			resultBuf.append(line);
		}
		try {
			rd.close();
			response.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultBuf.toString();
	}

	public ArrayList<String> execute(String filePath, String name,
			String codeRef) {
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
		System.out.println("Post parameters : " + url);
		HttpResponse response = null;
		long startTime = 0;
		long endTime;

		try {
			startTime = System.currentTimeMillis();
			response = client.execute(post);
			endTime = System.currentTimeMillis();
			System.out.println(" :: data file upload latency :: "
					+ (endTime - startTime));
			this.uploadTime = endTime - startTime;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int responseCode = response.getStatusLine().getStatusCode();
		long t = System.currentTimeMillis() - startTime;
		System.out.println(" :: cap execution latency :: " + t);
		System.out.println("Response Code : " + responseCode);

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

	public ArrayList<String> execute(Operation o, String filePath) {
		// HttpClient client = HttpClientBuilder.create().build();

		String url = OPENCPU_SERVER + o.getCodeRef();
		HttpPost post = new HttpPost(url);

		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		final File file = new File(filePath);
		FileBody fb = new FileBody(file);

		entityBuilder.addPart("file", fb);

		if (o.getParam1() != null) {
			if (!o.getParam1().equals("")) {
				String param1 = "'" + o.getParam1() + "'";
				entityBuilder.addTextBody("param1", param1);
				// System.out.println("Parameter1 : " + o.getParam1());
			}
		}

		if (o.getParam2() != null) {
			if (!o.getParam2().equals("")) {
				String param2 = "'" + o.getParam2() + "'";
				entityBuilder.addTextBody("param2", param2);
				// System.out.println("Parameter2 : " + o.getParam2());
			}
		}

		final HttpEntity yourEntity = entityBuilder.build();
		post.setEntity(yourEntity);
		// System.out.println("URL : " + url);

		CloseableHttpResponse response = null;
		long startTime = 0;
		long endTime;
		try {
			startTime = System.currentTimeMillis();
			response = this.httpClient.execute(post);

			endTime = System.currentTimeMillis();
			// System.out.println(" :: data file upload latency :: " + (endTime
			// - startTime));
			this.uploadTime = endTime - startTime;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int responseCode = response.getStatusLine().getStatusCode();
		if (responseCode != 201) {
			System.out.println("POST Not OK." + responseCode);
			System.exit(0);
		}
		// System.out.println("Response Code : " + responseCode);

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
		} finally {
			try {
				rd.close();
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resultList;
	}

	public ArrayList<String> execute(Operation o) throws IOException,
			ClientProtocolException {
		// HttpClient client = HttpClientBuilder.create().build();

		String url = OPENCPU_SERVER + o.getCodeRef();
		HttpPost post = new HttpPost(url);

		/*
		 * List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		 * 
		 * try { post.setEntity(new UrlEncodedFormEntity(urlParameters)); }
		 * catch (UnsupportedEncodingException e1) { // TODO Auto-generated
		 * catch block e1.printStackTrace(); }
		 */

		// System.out.println("URL : " + url);

		CloseableHttpResponse response = null;
		long startTime = 0;
		long endTime;
		try {
			startTime = System.currentTimeMillis();
			response = this.httpClient.execute(post);
			endTime = System.currentTimeMillis();
			// System.out.println(" :: upload latency :: " + (endTime -
			// startTime));
			this.uploadTime = endTime - startTime;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int responseCode = response.getStatusLine().getStatusCode();
		if (responseCode != HttpStatus.SC_CREATED) {
			System.out.println("POST Not OK." + responseCode);
			System.exit(0);
		}
		// System.out.println("Response Code : " + responseCode);
		HttpEntity entity = response.getEntity();

		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(entity.getContent()));

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
		} finally {
			try {
				rd.close();
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultList;
	}

}
