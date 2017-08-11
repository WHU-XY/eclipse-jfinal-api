package com.whu.jFinal.response;


import java.util.Map;


public class InquiryResponse extends BaseResponse {

	private Map<String, Object> info;
	
	
	public InquiryResponse() {
        super();
    }
	
	public Map<String, Object> getInfo() {
        return info;
    }
	public void setInfo(Map<String, Object> info) {
        this.info = info;
    }
}
