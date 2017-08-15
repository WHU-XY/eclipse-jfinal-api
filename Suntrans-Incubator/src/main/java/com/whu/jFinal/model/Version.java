package com.whu.jFinal.model;

import java.util.Map;

import com.jfinal.plugin.activerecord.Model;

public class Version extends Model<Version> {

	public static final String type = "type";
	public static Version dao = new Version();
	
	@Override
	public Map<String, Object> getAttrs() {
		return super.getAttrs();
	}

}
