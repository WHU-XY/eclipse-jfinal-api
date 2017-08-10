package com.whu.suntrans.action;


import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.whu.suntrans.model.BaseDeptInfo;

public class IndexController extends Controller {

	static DeptInfoService service = new DeptInfoService();
	
	public void index() {
		setAttr("blogPage", service.paginate(getParaToInt(0, 1), 10));
		render("index.html");
	}
	public void next1() {
		Page<BaseDeptInfo> newDeptInfo=service.paginate(getParaToInt(0, 1), 10);
		
		setAttr("blogPage", newDeptInfo);
		render("task.html");
	}
}
