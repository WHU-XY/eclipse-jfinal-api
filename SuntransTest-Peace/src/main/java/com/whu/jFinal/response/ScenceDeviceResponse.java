package com.whu.jFinal.response;

import java.util.List;

import com.whu.jFinal.model.ScenceDeviceModel;

public class ScenceDeviceResponse extends BaseResponse {

	private List<ScenceDeviceModel> info;

	public ScenceDeviceResponse() {
        super();
    }

	public List<ScenceDeviceModel> getInfo() {
		return info;
	}

	public void setInfo(List<ScenceDeviceModel> info) {
		this.info = info;
	}
}
