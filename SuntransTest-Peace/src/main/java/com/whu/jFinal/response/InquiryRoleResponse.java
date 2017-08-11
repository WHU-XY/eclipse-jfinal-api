package com.whu.jFinal.response;

import java.util.List;


import com.whu.jFinal.model.RolesInfo;

public class InquiryRoleResponse extends BaseResponse {
	
	private List<RolesInfo> info;
	
	public InquiryRoleResponse() {
        super();
    }
	
	public List<RolesInfo> getInfo() {
        return info;
    }
	
	public void setInfo(List<RolesInfo> info) {
        this.info = info;
    }
}
