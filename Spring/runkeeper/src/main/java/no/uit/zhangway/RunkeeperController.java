package no.uit.zhangway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.uit.zhangway.model.FitnessActivity;
import no.uit.zhangway.model.FitnessActivityFeed;
import no.uit.zhangway.model.FitnessActivityFeedItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

@Controller
public class RunkeeperController {

	private Client client;

	@RequestMapping(value = "/runkeeper", method = RequestMethod.GET)
	public String runkeeper(Model model) {

		model.addAttribute("client_id", "d2831aa1942f4f33ae2ce5dcb86d7e91");
		model.addAttribute("response_type", "code");
		model.addAttribute("redirect_uri",
				"http://localhost:8080/zhangway/index.htm");
		return "redirect:https://runkeeper.com/apps/authorize";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(@RequestParam("code") String code, Model model) {

		System.out.println("code: " + code);

		return "index";
	}

	@RequestMapping(value = "/gettoken", method = RequestMethod.POST)
	public String retrieve(@RequestParam("code") String code, Model model) {

		System.out.println("retrieve code: " + code);
		String token = convertToken(code, "d2831aa1942f4f33ae2ce5dcb86d7e91",
				"ec171aefb9b0406187c211c48138f24f",
				"http://localhost:8080/zhangway/index.htm");
		System.out.println("token: " + token);
		client = new Client(token);
		String filename;
		for (int k = 4; k <= 4; k += 1) {
			
			
			filename = "C:/Users/zw/spring/bbb/60000.json";
    		
    		try {
				client.createFitnessActivity(filename);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//String resource = "/fitnessActivities/";
			FitnessActivity fitnessActivity2 = client
					.getFitnessActivity(client.getLocation());
			
			
		}
		/*
		 * for(int j = 0; j<20; j++){
		 * 
		 * FitnessActivity fitnessActivity3 =
		 * client.getFitnessActivity(resource+list[j]);
		 * 
		 * try { Thread.sleep(2000); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } }
		 */
		FitnessActivityFeed fitnessActivities = client.getFitnessActivities();
		for (FitnessActivityFeedItem feed : fitnessActivities.getItems()) {
			FitnessActivity fitnessActivity = client.getFitnessActivity(feed
					.getUri());
			// assertObjectEqualsExpectedJson(fitnessActivity, "responses" +
			// feed.getUri() + ".json");

		}

		return "index";
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

}
