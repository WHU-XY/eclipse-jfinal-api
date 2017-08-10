package com.whu.suntrans;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.render.ViewType;
import com.whu.suntrans.context.Context;
import com.whu.suntrans.handler.ContextHandler;
import com.whu.suntrans.interceptor.ErrorInterceptor;
import com.whu.suntrans.model.BlogInfo;
import com.whu.suntrans.plugin.HikariCPPlugin;
import com.whu.suntrans.router.ActionRouter;




public class AppConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants arg0) {
		// TODO Auto-generated method stub
		arg0.setDevMode(true);//开启开发模式
		arg0.setEncoding("UTF-8");
		arg0.setViewType(ViewType.JSP);
	}

	@Override
	public void configHandler(Handlers arg0) {
		// TODO Auto-generated method stub
		arg0.add(new ContextHandler());
	
	}

	@Override
	public void configInterceptor(Interceptors arg0) {
		// TODO Auto-generated method stub
		arg0.add(new ErrorInterceptor());
	}

	@Override
	public void configPlugin(Plugins arg0) {
		// TODO Auto-generated method stub
		loadPropertyFile("jdbc.properties");
        HikariCPPlugin hcp = new HikariCPPlugin(getProperty("jdbcUrl"), 
                getProperty("user"), 
                getProperty("password"), 
                getProperty("driverClass"), 
                getPropertyToInt("maxPoolSize"));
        
        arg0.add(hcp);
        
        ActiveRecordPlugin arp = new ActiveRecordPlugin(hcp);
        arg0.add(arp);
		
		arp.addMapping("blog","id", BlogInfo.class);//用户表
	}

	@Override
	public void configRoute(Routes arg0) {
		arg0.add(new ActionRouter()); //页面路由
	}
	@Override
    public void afterJFinalStart() {
        Context.me().init();
    }

    @Override
    public void beforeJFinalStop() {
        Context.me().destroy();
    }

}
