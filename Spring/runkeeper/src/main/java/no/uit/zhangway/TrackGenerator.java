package no.uit.zhangway;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import no.uit.zhangway.model.Path;
import no.uit.zhangway.model.Track;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class TrackGenerator {

	public static void main(String[] args) {
		
		
		//ObjectWriter writer = mapper.defaultPrettyPrintingWriter();
		
		/*
		try {
			 
			JsonFactory jfactory = new JsonFactory();
			JsonGenerator jGenerator = jfactory.createGenerator(new File("./user.json"), JsonEncoding.UTF8);
		 
			
			//JsonGenerator jGenerator = jfactory.createJsonParser(new File("c:\\user.json"), JsonEncoding.UTF8);
			jGenerator.writeStartObject(); // {
		 
			jGenerator.writeStringField("name", "mkyong"); // "name" : "mkyong"
			jGenerator.writeNumberField("age", 29); // "age" : 29
		 
			jGenerator.writeFieldName("messages"); // "messages" :
			jGenerator.writeStartArray(); // [
		 
			jGenerator.writeString("msg 1"); // "msg 1"
			jGenerator.writeString("msg 2"); // "msg 2"
			jGenerator.writeString("msg 3"); // "msg 3"
		 
			jGenerator.writeEndArray(); // ]
		 
			jGenerator.writeEndObject(); // }
		 
			jGenerator.close();
		 
		     } catch (JsonGenerationException e) {
		 
			e.printStackTrace();
		 
		     } catch (JsonMappingException e) {
		 
			e.printStackTrace();
		 
		     } catch (IOException e) {
		 
			e.printStackTrace();
		 
		     }
		 */
		// Gson gson = new Gson();
		// Initialize a list of type DataObject
		//for (int j = 5000; j <= 260000; j += 5000) {
			int j = 6;
			Track track = new Track("Running", "None",
					"Sun, 18 Nov 2013 1:41:00", "test");
			List<Path> objList = new ArrayList<Path>();
			BigDecimal startLongitude = new BigDecimal("-70.566466");
			BigDecimal startLatitude = new BigDecimal("-33.42536");
			for (int i = 0; i < j; i++) {
				Path point = new Path();
				double stepLong = 0.5 * i;
				double stepLat = 0.5 * i;

				if (i == 0) {
					point.setType("start");
					point.setTimestamp(0);
					point.setLongitude(startLongitude);
					point.setLatitude(startLatitude);
				} else {
					if (i == j - 1) {
						point.setType("end");
					} else {
						point.setType("gps");
					}
					point.setTimestamp(8 * i);
					BigDecimal longitude = new BigDecimal("-70.566466").add(
							new BigDecimal(stepLong)).setScale(6,
							BigDecimal.ROUND_DOWN);
					BigDecimal latitude = new BigDecimal("-33.42536").add(
							new BigDecimal(stepLat)).setScale(6,
							BigDecimal.ROUND_DOWN);
					point.setLongitude(longitude);
					point.setLatitude(latitude);

				}
				objList.add(point);

			}
			track.setPath(objList);
			ObjectMapper mapper = new ObjectMapper();
			try {
				 
				// convert user object to json string, and save to a file
				mapper.writeValue(new File("./user.json"), track);
		 
				// display to console
				//System.out.println(mapper.writeValueAsString(track));
				System.out.println(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(track));
		 
			} catch (JsonGenerationException e) {
		 
				e.printStackTrace();
		 
			} catch (JsonMappingException e) {
		 
				e.printStackTrace();
		 
			} catch (IOException e) {
		 
				e.printStackTrace();
		 
			}
		 
			/*
			 * objList.add(new DataObject(0, "zero")); objList.add(new
			 * DataObject(1, "one")); objList.add(new DataObject(2, "two"));
			 */
			/*
			// Convert the object to a JSON string
			String json = new Gson().toJson(track);
			// System.out.println(json);
			try {
				FileUtils.writeStringToFile(new File(j + ".json"), json);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		//}
		System.out.println("finished");

	}

}
