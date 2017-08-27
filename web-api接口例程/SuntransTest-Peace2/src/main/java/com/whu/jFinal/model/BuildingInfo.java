package com.whu.jFinal.model;

import java.util.Map;

import com.jfinal.plugin.activerecord.Model;

public class BuildingInfo extends Model<BuildingInfo> {
	
	public static final String room_id = "room_id";

	public static BuildingInfo dao = new BuildingInfo();

	@Override
	public Map<String, Object> getAttrs() {
		return super.getAttrs();
	}
}
