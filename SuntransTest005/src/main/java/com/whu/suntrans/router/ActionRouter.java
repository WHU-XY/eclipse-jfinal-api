package com.whu.suntrans.router;

import com.jfinal.config.Routes;
import com.whu.suntrans.action.IndexAction;


public class ActionRouter extends Routes {

	@Override
	public void config() {
		// TODO Auto-generated method stub
		add("/index", IndexAction.class);
	}

}
