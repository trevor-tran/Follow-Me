package com.followme.requests;

import java.util.HashMap;
import java.util.Map;

import com.followme.group.GroupController;
import com.followme.trip.Location;
import com.followme.trip.TripController;
import com.followme.user.UserController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mysql.jdbc.Util;
import com.followme.util.JsonUtil;
import spark.Request;
import spark.Response;
import spark.Route;

public class RequestsHandler {

	public static Route HandleGroupIdRequest = (Request request, Response response) -> {
		if( !request.body().isEmpty()){
			Gson gson = new GsonBuilder().create();
			JsonObject json = gson.fromJson(request.body(), JsonObject.class);
			User user = new User(json);
			
			String groupId = GroupController.generateGroupId();
			
			//add info to database
			GroupController.addGroup(groupId, user.getId());
			addOrUpdateUser(groupId, user);
			
			return groupId;
			
		}else {
			//TODO:handle empty request body, have not tested yet.
			response.status(301);
			return null;
		}
		
	};
	
	public static Route HandleAddingMember = (Request request, Response response) -> {
		String groupId = request.queryParams("groupid");
		Gson gson = new GsonBuilder().create();
		JsonObject json = gson.fromJson(request.body(), JsonObject.class);
		User user = new User(json);
		
		addOrUpdateUser(groupId, user);
		
		return JsonUtil.dataToJson( getLeaderLocation(groupId));
	};
	
	public static Route HandleUpdatingLocation = (Request request, Response response) -> {
		String groupId = request.queryParams("groupid");
		Gson gson = new GsonBuilder().create();
		JsonObject json = gson.fromJson(request.body(), JsonObject.class);
		User user = new User(json);
		TripController.addOrUpdateMember(groupId, user.getId(), user.getLatitude(), user.getLongitude(), user.getHeading(), user.getSpeed(), true);
		return null; 
	};
	
	private static Location getLeaderLocation( String groupId){
		String leaderId = GroupController.getLeaderId(groupId);
		Location location = null;
		if (leaderId != ""){
			location = TripController.getLocation(groupId, leaderId);
		}
		return location;
	}
	
	private static void addOrUpdateUser(String groupId, User user){
			UserController.addOrUpdateUser(user.getId(), user.getName(), user.getPlatform());
			TripController.addOrUpdateMember(groupId, user.getId(), user.getLatitude(), user.getLongitude(), user.getHeading(), user.getSpeed(), false);
	}
}
