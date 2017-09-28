package com.jade.controller.api;

import com.jade.model.Member;
import com.jfinal.core.Controller;

public class MemberController extends Controller {
	public void index() {
		String account = getPara("account");
		Member member = Member.dao.findMemberByAccount(account);
		setAttr("member", member);
		renderJson();
	}

}
