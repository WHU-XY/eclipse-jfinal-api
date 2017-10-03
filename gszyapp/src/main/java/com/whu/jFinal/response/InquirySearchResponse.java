package com.whu.jFinal.response;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

/**
*@author WHU-XY 作者 E-mail:2013301470047@whu.edu.cn
*@version 1.0 创建时间：2017年9月26日下午4:43:32
*/
public class InquirySearchResponse extends BaseResponse {
	private List<Record> roominfo;
	private List<Record> studentinfo;
	
	public InquirySearchResponse() {
        super();
    }
	public List<Record> getRoominfo() {
		return roominfo;
	}
	public void setRoominfo(List<Record> roominfo) {
		this.roominfo = roominfo;
	}
	public List<Record> getStudentinfo() {
		return studentinfo;
	}
	public void setStudentinfo(List<Record> studentinfo) {
		this.studentinfo = studentinfo;
	}
}
