package com.whu.jFinal.model;

import java.util.Map;

import com.jfinal.plugin.activerecord.Model;

public class StudentInfo extends Model<StudentInfo> {

	public static final String student_id = "student_id";
    
	public static StudentInfo dao = new StudentInfo();
	
	@Override
	public Map<String, Object> getAttrs() {
		return super.getAttrs();
	}
}
