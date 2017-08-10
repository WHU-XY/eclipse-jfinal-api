package com.fpliu.jfinal;

import com.fpliu.jfinal.controller.IndexController;
import com.fpliu.jfinal.model.PersonInfoModel;
import com.fpliu.jfinal.plugin.HikariCPPlugin;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.render.ViewType;

public class MyConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants constants) {
		constants.setDevMode(true);//开启开发模式
		constants.setEncoding("UTF-8");
		constants.setViewType(ViewType.JSP);
	}

	@Override
	public void configHandler(Handlers handlers) {

	}

	@Override
	public void configInterceptor(Interceptors interceptors) {

	}

	@Override
	public void configPlugin(Plugins plugins) {
		
		loadPropertyFile("jdbc.properties");
		HikariCPPlugin hcp = new HikariCPPlugin(getProperty("jdbcUrl"), 
		getProperty("user"), 
	    getProperty("password"), 
	    getProperty("driverClass"), 
	    getPropertyToInt("maxPoolSize"));
			        
		plugins.add(hcp);
			        
		ActiveRecordPlugin arp = new ActiveRecordPlugin(hcp);
		plugins.add(arp);
		
		arp.addMapping("person_info", PersonInfoModel.id, PersonInfoModel.class);//用户表
		
	
	}

	@Override
	public void configRoute(Routes routes) {
		routes.add("/", IndexController.class);
	}

}