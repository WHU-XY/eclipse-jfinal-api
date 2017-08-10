package com.whu.jFinal.router;

import com.jfinal.config.Routes;
import com.whu.jFinal.api.AccountAPIController;
import com.whu.jFinal.api.CommonAPIController;
import com.whu.jFinal.api.FileAPIController;


public class APIRouter extends Routes {

	@Override
	public void config() {
		// TODO Auto-generated method stub
		 add("/api", CommonAPIController.class);
	        //用户相关
	     add("/api/account", AccountAPIController.class);
	        //文件相关
	     add("/api/fs",FileAPIController.class);
	}

}
