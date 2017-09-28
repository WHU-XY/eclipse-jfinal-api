package com.jade.model;

import java.util.ArrayList;
import java.util.List;

import com.jade.util.RegexUtils;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;

public class Member extends Model<Member> {
	private static final long serialVersionUID = 8185408711950040645L;
	public static final Member dao = new Member();
	
	public Member findMemberByAccount(String account) {
		List<Object> params = new ArrayList<Object>();
		String sqlWhere = "";
		if (StrKit.notBlank(account)) {
			if (RegexUtils.isMobile(account)) {
				sqlWhere = "  Mobile=? AND ";
			} if (RegexUtils.isEmail(account)) {
				sqlWhere = "  Email=? AND ";
			} else {
				sqlWhere = "  Name=? AND ";
			}
			params.add(account);
		}
		if (StrKit.notBlank(sqlWhere)) {
			sqlWhere = " WHERE " +  sqlWhere.substring(0, sqlWhere.lastIndexOf("AND"));
		}
		return (Member) Member.dao.findFirst(SqlKit.sql("member.findMember")+sqlWhere, params.toArray());
	}

}
