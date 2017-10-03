package com.whu.jFinal.response;

import java.util.List;

import com.whu.jFinal.model.TestModel;

public class TestResponse extends BaseResponse {
	private List<TestModel> info;

	public TestResponse() {
        super();
    }

	public List<TestModel> getInfo() {
		return info;
	}

	public void setInfo(List<TestModel> info) {
		this.info = info;
	}
}
