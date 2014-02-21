package no.uit.zhangwei.Service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

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

import no.uit.zhangwei.Capability;
import no.uit.zhangwei.User;

public class GirjiService {
	
	static final String OPENCPU_SERVER = "http://129.242.19.118";
	private ArrayList<Capability> capabilities;
	private HashMap<String, User> userMap;
	
	public GirjiService(){
		this.capabilities = new ArrayList<Capability>();
		this.userMap = new HashMap<String, User>();
	}
	
	public User findUser(String name){
		User user = null;
		if(this.userMap.containsKey(name)){
			user = this.userMap.get(name);
		}
		return user;
		
	}
	
	public boolean addUser(String name){
		User user = new User(name);
		if(this.userMap.put(name, user) != null){
			return true;
		}
		return false;
	}
	
	public ArrayList<Capability> getUserCapLst(String userName){
		ArrayList<Integer> idLst = this.userMap.get(userName).getCapIdLst();
		ArrayList<Capability> lst = new ArrayList<Capability>();
		Capability cap = null;
		for(int i = 0; i < idLst.size(); i++){
			cap = findCap(idLst.get(i));
			lst.add(cap);
		}
		return lst;
	}
	
	public void addCap(Capability cap, String userName){
		this.capabilities.add(cap);
		//update this user's capList
		int id = cap.getId();
		this.userMap.get(userName).addCapId(id);
	}
	
	private Capability findCap(int id){
		Capability cap = null;
		for(int i = 0; i < this.capabilities.size(); i++){
			Capability ca = this.capabilities.get(i);
			if(id == ca.getId()){
				cap = ca;
			}
		}
		return cap;
	}
	
	public String getFile(String file, ArrayList<String> result) throws ClientProtocolException, IOException{
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
	
	public ArrayList<String> execute(String filePath, String name, String codeRef){
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
		Long startTime;
		Long endTime;
		try {
			startTime = System.currentTimeMillis();
			response = client.execute(post);
			endTime = System.currentTimeMillis();
			System.out.println(" :: time elapsed :: "
					+ (endTime - startTime));
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
	
	public ArrayList<String> execute(String filePath, String codeRef){
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
	

	
	
	

}
