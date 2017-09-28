package com.jade.core;

import com.jade.controller.api.MemberController;
import com.jfinal.config.Routes;

/** 
* 个性定制路由 
* @author jiangyf
* @date 2016年1月8日 上午9:59:55  
*/
public class MyRoutes extends Routes {

	/**
	 * 优点：让文件更简洁，便于团队开发，避免多人同时修改配置文件造成版本冲突
	 */
	@Override
	public void config() {
		add("/member", MemberController.class);
	}

}
