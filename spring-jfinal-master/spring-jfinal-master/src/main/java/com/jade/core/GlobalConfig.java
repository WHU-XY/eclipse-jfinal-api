package com.jade.core;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jade.model.Member;
import com.jade.util.PropertiesUtil;
import com.jade.xml.RouteGroup;
import com.jade.xml.RouteItem;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.kit.JaxbKit;
import com.jfinal.ext.plugin.sqlinxml.SqlInXmlPlugin;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;

/**   
 * 项目API引导式配置
 * @author jiangyf   
 * @since 2016年10月9日 上午11:22:41 
 * @version V1.0   
 */
public class GlobalConfig extends JFinalConfig {
	private static final Logger log = LoggerFactory.getLogger(GlobalConfig.class);
	public static Map<String, String> ROUTES =  new HashMap<String, String>();
	public static Map<String, String> STATUS_CODE = new HashMap<String, String>();

	/**
	 * 配置常量
	 */
	@Override
	public void configConstant(Constants me) {
		//加载少量必要配置，第一次使用use加载的配置为主配置，以后只需用PropKit.get(key)获取值
//		PropKit.use("common.properties");
		//开发模式配置
		me.setDevMode(true);
//		me.setDevMode(PropKit.getBoolean("devMode"));
		//模板路径配置
		me.setBaseViewPath("WEB-INF/templates/");
		//视图类型设置（默认freemarker）
		me.setViewType(ViewType.FREE_MARKER);
		//初始化状态码
		initStatusCode();
	}

	/**
	 * 配置访问路由，注 ："/member/index"同"/member"
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void configRoute(Routes me) {
		//配置个性定制路由
//		me.add(new MyRoutes());
		//params：定位的controller, 对应的controller类, 响应路径（默认为第一个参数）
//		me.add("/", MemberController.class);
//		me.add("/member", MemberController.class, "/member");
		
		log.info("----init configRoute");
	    File[] files = new File(GlobalConfig.class.getClassLoader().getResource("").getFile())
	    	.listFiles(new FileFilter(){
		      public boolean accept(File pathname){
		        return pathname.getName().endsWith("route.xml");
		      }
		    });
	    String type;
	    String pkge;
	    try {
	    	for (File xmlfile : files) {
	  	      RouteGroup group = JaxbKit.unmarshal(xmlfile, RouteGroup.class);
	  	      type = group.type;
	  	      pkge = group.pkge;
	  	      GlobalConfig.ROUTES.put(pkge, ("".equals(type) ? "" : type+"/"));
	  	      for (RouteItem routeItem : group.routeItems) {
	  	    	  me.add(type+routeItem.controllerkey, 
	  	    			  (Class<? extends Controller>)Class.forName(pkge+"."+routeItem.className));
	  	      }
	  	    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	 * 配置插件，插件结构是jfinal的主要拓展方式，方便创建插件并应用到项目中
	 */
	@Override
	public void configPlugin(Plugins me) {
		log.info("----init configPlugin");
		
		SqlInXmlPlugin sqlInXmlPlugin = new SqlInXmlPlugin();
		me.add(sqlInXmlPlugin);
		log.info("----sqlinXmlPlugin plugin added");
		
		//非第一次使用use加载的配置需通过每次指定配置文件名或创建对象的方式取值
//		Prop prop = PropKit.use("jdbc.properties");
//		String jdbcUrl = prop.get("url").trim();
//		String user = prop.get("username").trim();
//		String password = prop.get("password").trim();
		
		loadPropertyFile("jdbc.properties");
		String jdbcUrl = getProperty("url").trim();
		String user = getProperty("username").trim();
		String password = getProperty("password").trim();
		String driverClass = getProperty("driver").trim();
		Integer maxPoolSize = Integer.valueOf(getProperty("maxPoolSize").trim());
		Integer minPoolSize = Integer.valueOf(getProperty("minPoolSize").trim());
		Integer initialPoolSize = Integer.valueOf(getProperty("initialPoolSize").trim());
		Integer maxIdleTime = Integer.valueOf(getProperty("maxIdleTime").trim());
		Integer acquireIncrement = Integer.valueOf(getProperty("acquireIncrement").trim());
		//配置数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(jdbcUrl, user, password, driverClass, 
				maxPoolSize, minPoolSize, initialPoolSize, maxIdleTime, acquireIncrement);
		//配置数据库访问插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(c3p0Plugin);
		log.info("----C3p0Plugin plugin added");
		
		//映射数据库表到模型
		arp.addMapping("member", "ID",  Member.class);
		me.add(arp);
		log.info("----ActiveRecordPlugin plugin added");
		
//		MemcachedPlugin memcached = new MemcachedPlugin();
//		me.add(memcached);
//		Object object = MemcachedKit.getInstance().getObject("memcached");
//		log.info("----Memcached plugin added; "+(object!=null?object.toString():""));
		
        File file = new File(SqlKit.class.getClassLoader().getResource("").getFile());
        File[] files = file.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith("sql.xml")) {
                    return true;
                }
                return false;
            }
        });
        log.info("----load sql resource files");
        
        try {
	        for (File xmlfile : files) {
	           log.info(xmlfile.getPath());
	        }
        } catch (Exception ex){
        	ex.printStackTrace();
        }
	}

	/**
	 * 配置全局拦截器,配置粒度分为：global，class，method
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		log.info("----init configInterceptor");
	}

	/**
	 * 配置处理器，处理器可接管所有web请求，对应用有完全控制权，方便实现更高层的功能拓展
	 */
	@Override
	public void configHandler(Handlers me) {
		log.info("----init configHandler");
		me.add(new ContextPathHandler());
	}
	
	/** 
	* 初始化状态码
	*/
	public void initStatusCode(){
		log.info("----init StatusCode");
        PropertiesUtil.propertiesToMap("status-code.properties", STATUS_CODE);
	}

}
