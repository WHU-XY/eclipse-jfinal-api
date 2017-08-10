package com.fpliu.jfinal.controller;

import java.util.HashMap;
import java.util.Map;

import com.fpliu.jfinal.model.PersonInfoModel;
import com.jfinal.core.Controller;

public class IndexController extends Controller {

	public void index() {
		renderText("this is index");
	}

	public void test() {
		renderText("this is test");
	}
	public void doc() {
		String sql = "SELECT *FROM person_info";
		PersonInfoModel nowPerson = PersonInfoModel.user.findFirst(sql);
		setAttr("PersonInfo", nowPerson);
		render("doc/index.html");   
	}
}
