package com.demo.common;

import com.demo.controller.HelloController;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.template.Engine;

public class DemoConfig extends JFinalConfig {
	
	public static void main(String[] args) {
		/**
		 * �ر�ע�⣺Eclipse ֮�½����������ʽ
		 */
		JFinal.start("WebRoot", 80, "/", 5);

		/**
		 * �ر�ע�⣺IDEA ֮�½����������ʽ������ eclipse ֮���������һ������
		 */
		// JFinal.start("WebRoot", 80, "/");
	}

	@Override
	public void configConstant(Constants me) {
		// TODO Auto-generated method stub
		me.setDevMode(PropKit.getBoolean("devMode", false));
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
		me.add(new ContextPathHandler("contextPath"));
	}
	
}