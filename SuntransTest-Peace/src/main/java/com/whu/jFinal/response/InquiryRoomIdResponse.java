package com.whu.jFinal.response;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class InquiryRoomIdResponse extends BaseResponse {

	private List<Record> info;
	private List<Record> info1;
	private List<Record> info2;
	private List<Record> info3;

	public InquiryRoomIdResponse() {
        super();
    }

	public List<Record> getaccount() {
		return info;
	}

	public void setaccount(List<Record> info) {
		this.info = info;
	}
	public List<Record> getdev_channel() {
		return info1;
	}

	public void setdev_channel(List<Record> info) {
		this.info1 = info;
	}
	public List<Record> getmeter_info() {
		return info2;
	}

	public void setmeter_info(List<Record> info) {
		this.info2 = info;
	}
	public List<Record> getroom_stu() {
		return info3;
	}

	public void setroom_stu(List<Record> info) {
		this.info3 = info;
	}
}
