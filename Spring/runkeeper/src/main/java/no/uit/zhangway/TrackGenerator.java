package no.uit.zhangway;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import no.uit.zhangway.model.Path;
import no.uit.zhangway.model.Track;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

public class TrackGenerator {

	public static void main(String[] args) {
		// Gson gson = new Gson();
		// Initialize a list of type DataObject
		for (int j = 5000; j <= 260000; j += 5000) {
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
			/*
			 * objList.add(new DataObject(0, "zero")); objList.add(new
			 * DataObject(1, "one")); objList.add(new DataObject(2, "two"));
			 */
			// Convert the object to a JSON string
			String json = new Gson().toJson(track);
			// System.out.println(json);
			try {
				FileUtils.writeStringToFile(new File(j + ".json"), json);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("finished");

	}

}
