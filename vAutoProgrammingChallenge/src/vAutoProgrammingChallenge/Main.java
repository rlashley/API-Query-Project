package vAutoProgrammingChallenge;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

import org.json.*;


public class Main {

	public static void main(String[] args) throws IOException, JSONException {
		Connection connection = new Connection();
		JSONArray cars = new JSONArray();
		JSONArray dealerName = new JSONArray();
		JSONArray submitJSON = new JSONArray();
		HashSet<String> dealerId = new HashSet();
		String dealerInfo = null;
		
		//Make Initial Connection to API and create JSON object
		JSONObject datasetId = new JSONObject(connection.connectToAPI("http://vautointerview.azurewebsites.net/api/datasetId"));
		
		//Second connection to API and create JSON objects from vehicles data
		JSONObject vehicleIds = new JSONObject(connection.connectToAPI("http://vautointerview.azurewebsites.net/api/" + datasetId.get("datasetId") + "/vehicles"));
		JSONArray fields = vehicleIds.getJSONArray("vehicleIds");
		
		//Third connection to API. Used String Set to remove duplicate dealers and added all cars into JSON Array
		for(int i=0;i<fields.length();i++) {
			JSONObject makeModelDealer = new JSONObject(connection.connectToAPI("http://vautointerview.azurewebsites.net/api/" + datasetId.get("datasetId") + "/vehicles/" + fields.get(i)));
			dealerId.add(makeModelDealer.get("dealerId").toString());
			cars.put(makeModelDealer);
		}		
		
		//Forth connection to API to get dealer names. Then converted these to JSON Objects again
		for(String dealer: dealerId) {
			JSONObject dealerInf = new JSONObject(connection.connectToAPI("http://vautointerview.azurewebsites.net/api/" + datasetId.get("datasetId") + "/dealers/" + dealer));
			dealerName.put(dealerInf);
		}
		
		//if dealerId matches JSON info, add vehicle to dealerName JSON Array
		for(int i=0;i<cars.length();i++) {
			for(int x=0;x<dealerName.length();x++) {
				if(cars.getJSONObject(i).get("dealerId").equals(dealerName.getJSONObject(x).get("dealerId"))) {
					JSONObject obj = new JSONObject(cars.getJSONObject(i).toString());
					obj.remove("dealerId");
					dealerName.getJSONObject(x).append("vehicles",obj);
				}
			}
		}
		
		//Post full dataset back in proper format
		JSONObject dealers = new JSONObject();
		dealers.put("dealers",dealerName);
		connection.sendToAPI("http://vautointerview.azurewebsites.net/api/" + datasetId.get("datasetId") + "/answer", dealers.toString());
	}
}
