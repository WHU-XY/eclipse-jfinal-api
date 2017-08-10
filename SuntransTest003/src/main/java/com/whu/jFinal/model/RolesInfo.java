package com.whu.jFinal.model;

import java.util.Map;

import com.jfinal.plugin.activerecord.Model;

public class RolesInfo extends Model<RolesInfo> {
	
	public static final String role_id = "role_id";

	public static RolesInfo dao = new RolesInfo();

	@Override
	public Map<String, Object> getAttrs() {
		return super.getAttrs();
	}

}
