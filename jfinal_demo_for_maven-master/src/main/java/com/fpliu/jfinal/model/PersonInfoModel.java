package com.fpliu.jfinal.model;

import java.util.Map;

import com.jfinal.plugin.activerecord.Model;

public class PersonInfoModel extends Model<PersonInfoModel> {

	public static String id = "id";
	public static final PersonInfoModel user = new PersonInfoModel();
	
	
	@Override
	public Map<String, Object> getAttrs() {
		return super.getAttrs();
	}

}
