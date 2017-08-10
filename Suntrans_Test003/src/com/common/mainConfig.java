package com.common;

import com.common.controller.HelloController;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.template.Engine;

public class mainConfig extends JFinalConfig {
	
	public static void main(String[] args) {
        JFinal.start("WebRoot",8016,"/",5);
    }

	@Override
	public void configConstant(Constants me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configRoute(Routes me) {
		// TODO Auto-generated method stub
		me.add("/", HelloController.class,"/index");
	}

	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configPlugin(Plugins me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub

	}

}
