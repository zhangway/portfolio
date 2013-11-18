package no.uit.zhangway;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import no.uit.zhangway.model.FitnessActivity;
import no.uit.zhangway.model.FitnessActivityFeed;
import no.uit.zhangway.model.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class Client {
	
	public static final String PRODUCTION_URL = "http://api.runkeeper.com";
    public static final String USER_RESOURCE = "/user";
    public static final String FITNESS_ACTIVITIES = "/fitnessActivities";
    

    private final HttpClient httpClient;
    private final String accessToken;
    private final String url;
    private final Gson gson;

    private User user;

    public Client(String accessToken) {
        this(PRODUCTION_URL, accessToken);
    }

    public Client(String url, String accessToken) {
        this.url = url;
        this.accessToken = accessToken;
        /*
        HttpConnectionManagerParams cmparams = new HttpConnectionManagerParams();
        cmparams.setSoTimeout(10000);
        cmparams.setTcpNoDelay(true);
        HttpConnectionManager manager = new SimpleHttpConnectionManager();
        manager.setParams(cmparams);
        params = new HttpClientParams();
        params.setSoTimeout(5000);
        client = new HttpClient(params, manager);
        */
        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000000);
        HttpConnectionParams.setSoTimeout(httpParams,3000000);
        httpClient = new DefaultHttpClient(httpParams);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        
        
        gson = new Gson();
        user = getUser();
    }
    
    public User getUser(Callback<User> callback) {
        HttpGet get = createHttpGetRequest(USER_RESOURCE, ContentTypes.USER);
        return execute(get, User.class, callback);
    }
    
    public User getUser() {
        return getUser(null);
    }
    
    private HttpGet createHttpGetRequest(String resource, String contentType) {
        String fullUrl = String.format("%s%s", url, resource);
        HttpGet get = new HttpGet(fullUrl);
        get.addHeader("Accept", contentType);
        get.addHeader("Authorization", "Bearer " + accessToken);
        return get;
    }
    
    private HttpPost createHttpPostRequest(String resource, String contentType) {
        String fullUrl = String.format("%s%s", url, resource);
        HttpPost post = new HttpPost(fullUrl);
        post.addHeader("Content-Type", contentType);
        post.addHeader("Authorization", "Bearer " + accessToken);
        
        return post;
    }
    
    private <T> T execute(HttpGet get, Class<T> clazz) {
        return execute(get, clazz, null);
    }
    
    public FitnessActivityFeed getFitnessActivities(Callback<FitnessActivityFeed> callback) {
        HttpGet get = createHttpGetRequest(user.getFitnessActivities(), ContentTypes.FITNESS_ACTIVITY_FEED);
        return execute(get, FitnessActivityFeed.class, callback);
    }

    public FitnessActivityFeed getFitnessActivities() {
        return getFitnessActivities(null);
    }
    
    public void createFitnessActivity(){
    	String filename = null;
    	
    	for(int i = 20000; i <= 300000; i+=20000){
    		filename = "C:/Users/zw/spring/bbb/"+i+".json";
    		//filename = "C:/Users/zw/Downloads/"+i+"_points.json";
    		createFitnessActivity(filename);
    	}
    	
    }
    
    public void createFitnessActivity(String filename){
    	
    	HttpPost post = createHttpPostRequest(FITNESS_ACTIVITIES, ContentTypes.NEW_FITNESS_ACTIVITY);
    	StringBuffer sb = new StringBuffer();
		String line;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//String json = sb.toString();
    	StringEntity input = null;
    	//System.out.println(sb.toString());
		try {
			input = new StringEntity(sb.toString());
			
			input.setContentEncoding("UTF-8");
			//post.setHeader("Content-Length",""+input.getContentLength());
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		
		post.setEntity(input);
    	//execute_post(post, clazz, callback)
		CloseableHttpResponse  response = null;
    	try {
    		//long start = System.nanoTime();
    		long t1=System.currentTimeMillis(); 
            response = (CloseableHttpResponse) httpClient.execute(post);
            long t=System.currentTimeMillis()-t1; 
            //long end = System.nanoTime() - start;
            //System.out.println("latency:"+t);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_CREATED) {
                //String entity = EntityUtils.toString(response.getEntity());
            	//get all headers
            	/*
            	Header[] headers = response.getAllHeaders();
            	for (Header header : headers) {
            		System.out.println("Key : " + header.getName() 
            		      + " ,Value : " + header.getValue());
            	}
                */
            	//get header by 'key'
            	String location = response.getFirstHeader("Location").getValue();
            	System.out.println("latency:"+t+",filename:"+filename+",Location:"+location);
            	HttpEntity entity = response.getEntity();
                if (entity != null){
                	EntityUtils.consume(entity);
                }/*
                HttpEntity entity = response.getEntity();
                String entityAsString = EntityUtils.toString(entity);
                Map<String, String> map = new HashMap<String, String>();
                map = (Map<String, String>) new Gson().fromJson(entityAsString, map.getClass());
                
            	System.out.println("Location:"+map.get("Location"));
            	*/
                //T parsedObject = gson.fromJson(entity, clazz);
                /*
                if (callback != null) {
                    callback.success(parsedObject, entity);
                }
                return parsedObject;
                */
            } else {
                throw new ClientException("Unexpected statusCode " + statusCode);
            }
        } catch (IOException e) {
            throw new ClientException(e);
        } 
    	
    }

    public FitnessActivityFeed getNextFitnessActivities(FitnessActivityFeed fitnessActivityFeed) {
        String next = fitnessActivityFeed.getNext();
        HttpGet get = createHttpGetRequest(next, ContentTypes.FITNESS_ACTIVITY_FEED);
        return execute(get, FitnessActivityFeed.class, null);
    }

    public FitnessActivity getFitnessActivity(String resource, Callback<FitnessActivity> callback) {
        HttpGet get = createHttpGetRequest(resource, ContentTypes.FITNESS_ACTIVITY);
        return execute(get, FitnessActivity.class, callback);
    }

    public FitnessActivity getFitnessActivity(String resource) {
        return getFitnessActivity(resource, null);
    }


   

    private <T> T execute(HttpGet get, Class<T> clazz, Callback<T> callback) {
        try {
        	long t1=System.currentTimeMillis(); 
            
            
            HttpResponse response = httpClient.execute(get);
            long t=System.currentTimeMillis()-t1; 
            System.out.println("Latency:"+t);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                String entity = EntityUtils.toString(response.getEntity());
                T parsedObject = gson.fromJson(entity, clazz);
                if (callback != null) {
                    callback.success(parsedObject, entity);
                }
                return parsedObject;
            } else if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                throw new SecurityException();
            } else {
                throw new ClientException("Unexpected statusCode " + statusCode);
            }
        } catch (IOException e) {
            throw new ClientException(e);
        }
    }
    
    private <T> T execute_post(HttpPost post, Class<T> clazz, Callback<T> callback) {
        try {
            HttpResponse response = httpClient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_CREATED) {
                String entity = EntityUtils.toString(response.getEntity());
                T parsedObject = gson.fromJson(entity, clazz);
                if (callback != null) {
                    callback.success(parsedObject, entity);
                }
                return parsedObject;
            } else {
                throw new ClientException("Unexpected statusCode " + statusCode);
            }
        } catch (IOException e) {
            throw new ClientException(e);
        }
    }

}
