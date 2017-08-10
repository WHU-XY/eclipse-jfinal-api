package com.whu.suntrans;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.whu.suntrans.action.IndexController;
import com.whu.suntrans.model._MappingKit;

public class AppConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		// TODO Auto-generated method stub
		PropKit.use("a_little_config.txt");
		me.setDevMode(PropKit.getBoolean("devMode", false));
	}

	@Override
	public void configRoute(Routes me) {
		// TODO Auto-generated method stub
		me.add("/", IndexController.class,"/index");	// 第三个参数为该Controller的视图存放路径
	}

	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub

	}
	
	public static DruidPlugin createDruidPlugin() {
		return new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
	}

	@Override
	public void configPlugin(Plugins me) {
		// TODO Auto-generated method stub
		DruidPlugin druidPlugin = createDruidPlugin();
		me.add(druidPlugin);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		// 所有映射在 MappingKit 中自动化搞定
		_MappingKit.mapping(arp);
		me.add(arp);
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
