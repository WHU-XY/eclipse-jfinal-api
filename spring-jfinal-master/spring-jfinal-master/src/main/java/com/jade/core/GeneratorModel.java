package com.jade.core;

import javax.sql.DataSource;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.c3p0.C3p0Plugin;

/** 
* 快捷生成Model，BaseModel，Mapping，DataDictionary 
* @author jiangyf
* @date 2016年1月14日 下午6:55:54  
*/
public class GeneratorModel {
	
	/** 
	* 获取数据源 
	* @return DataSource
	*/
	public static DataSource getDataSource() {
		Prop p = PropKit.use("jdbc.properties");
		C3p0Plugin c3p0Plugin = new C3p0Plugin(p.get("url"), 
				p.get("username"), p.get("password"));
		c3p0Plugin.start();
		return c3p0Plugin.getDataSource();
	}
	
	/** 
	* 生成model等 
	*/
	/*public static void generate() {
		// base model 所使用的包名
		String baseModelPackageName = "com.jade.base";
		// base model 文件保存路径
		String baseModelOutputDir = PathKit.getRootClassPath() 
				+ "/../../../../src/com/jade/base";
		
		// model 所使用的包名 (MappingKit 默认使用的包名)
		String modelPackageName = "com.jade.model";
		// model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
		String modelOutputDir = baseModelOutputDir + "/..";
		
		// 创建生成器
		Generator gernerator = new Generator(getDataSource(), baseModelPackageName, 
				baseModelOutputDir, modelPackageName, modelOutputDir);
		// 添加不需要生成的表名
		gernerator.addExcludedTable("basic");
		// 设置是否在 Model 中生成 dao 对象
		gernerator.setGenerateDaoInModel(true);
		// 设置是否生成字典文件
		gernerator.setGenerateDataDictionary(true);
		// 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
//		gernerator.setRemovedTableNamePrefixes("t_");
		// 生成
		gernerator.generate();
	}*/
}




