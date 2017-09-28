package com.whu.jFinal.router;

import com.jfinal.config.Routes;
import com.whu.jFinal.api.AccountAPIController;
import com.whu.jFinal.api.CommonAPIController;
import com.whu.jFinal.api.FileAPIController;
import com.whu.jFinal.api.InquiryAPIController;
import com.whu.jFinal.api.SmartBuildingAPIController;


public class APIRouter extends Routes {

	@Override
	public void config() {
		// TODO Auto-generated method stub
		 add("/api", CommonAPIController.class);
	        //用户相关
	     add("/api/account", AccountAPIController.class);
	        //文件相关
	     add("/api/fs",FileAPIController.class);
	     //查询有关
	     add("/api/inquiry",InquiryAPIController.class);
	     //智慧建筑
	     add("/api/smartbuilding",SmartBuildingAPIController.class);
	}

}
