package com.whu.jFinal;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.render.ViewType;
import com.whu.jFinal.plugin.HikariCPPlugin;
import com.whu.jFinal.config.Context;
import com.whu.jFinal.handler.APINotFoundHandler;
import com.whu.jFinal.handler.ContextHandler;
import com.whu.jFinal.interceptor.ErrorInterceptor;
import com.whu.jFinal.model.FeedBack;
import com.whu.jFinal.model.RegisterCode;
import com.whu.jFinal.model.User;
import com.whu.jFinal.router.APIRouter;
import com.whu.jFinal.router.ActionRouter;

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
		arg0.add(new APINotFoundHandler());
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
		
		arp.addMapping("t_user", User.USER_ID, User.class);//用户表
        arp.addMapping("t_register_code", RegisterCode.MOBILE, RegisterCode.class); //注册验证码对象
        arp.addMapping("t_feedback", FeedBack.class); //意见反馈表

	}

	@Override
	public void configRoute(Routes arg0) {
		// TODO Auto-generated method stub
		arg0.add(new APIRouter());//接口路由
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
