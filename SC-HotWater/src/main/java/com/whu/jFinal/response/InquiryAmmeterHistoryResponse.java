package com.whu.jFinal.response;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class InquiryAmmeterHistoryResponse extends BaseResponse {

	private List<Record> info;
	private List<Record> info1;
	private List<Record> info2;
	public InquiryAmmeterHistoryResponse() {
        super();
    }
	
	public List<Record> getDay_data() {
		return info;
	}

	public void setDay_data(List<Record> info) {
		this.info = info;
	}
	public List<Record> getWeek_data() {
		return info1;
	}

	public void setWeek_data(List<Record> info) {
		this.info1 = info;
	}
	public List<Record> getMonth_data() {
		return info2;
	}

	public void setMonth_data(List<Record> info) {
		this.info2 = info;
	}
}
