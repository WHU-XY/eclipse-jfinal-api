package com.whu.jFinal.response;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class InquiryDBResponse extends BaseResponse{
	private List<Record> info;
	private int count;
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public InquiryDBResponse() {
        super();
    }

	public List<Record> getInfo() {
		return info;
	}

	public void setInfo(List<Record> info) {
		this.info = info;
	}
}
