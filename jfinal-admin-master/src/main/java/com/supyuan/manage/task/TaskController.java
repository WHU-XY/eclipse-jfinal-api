package com.supyuan.manage.task;

import com.jfinal.plugin.activerecord.Page;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.jfinal.component.db.SQLUtils;


@ControllerBind(controllerKey = "/manage/task")
public class TaskController extends BaseProjectController {
	
	private static final String path = "/pages/task/task_info/task_";
	
	TaskSvc svc = new TaskSvc();

	public void index() {
		list();
	}
	
	public void list() {

		ManageTask model = getModelByAttr(ManageTask.class);
		SQLUtils sql = new SQLUtils(" from suntrans_dept_pro_info as t where 1=1 ");		
		
		if (model.getAttrValues().length != 0) {
			sql.setAlias("t");
			sql.whereLike("dept_name", model.getStr("dept_name"));
			sql.setAlias("t");
			sql.whereLike("main_pro_name", model.getStr("main_pro_name"));
			sql.setAlias("t");
			sql.whereLike("task_charger", model.getStr("task_charger"));
		}
	    sql.append(" order by t.id desc");		
		Page<ManageTask> page = ManageTask.dao.paginate(getPaginator(), "select t.* ", //
				sql.toString().toString());
		
		setAttr("page", page);
		setAttr("attr", model);
		render(path + "list.html");
	}
    
	public void add() {
		setAttr("parentSelect", svc.selectMenu("1"));
		render(path + "add.html");
	}
	
	public void view() {
		ManageTask model = ManageTask.dao.findById(getParaToInt());
		String parent = new TaskSvc().getParentName(model);
		model.put("parentname", parent);
		setAttr("model", model);
		render(path + "view.html");
	}
	
	public void save() {
		Integer pid = getParaToInt();
		ManageTask model = getModel(ManageTask.class);
		String dept_id = model.getStr("dept_id");
		switch(dept_id) {
		case "1": model.set("dept_name","市场部");break;
		case "2": model.set("dept_name","研发部");break;
		case "3": model.set("dept_name","工程部");break;
		case "4": model.set("dept_name","生产部");break;
		case "5": model.set("dept_name","财保部");break;
		case "6": model.set("dept_name","行管部");break;
		}
		// 日志添加
		Integer userid = getSessionUser().getUserID();
		String now = getNow();
		model.put("update_id", userid);
		model.put("update_time", now);
		if (pid != null && pid > 0) { // 更新
			model.update();
		} else { // 新增
			model.remove("id");
			model.save();
		}
		renderMessage("保存成功");
		//list();
	}
	
	public void edit() {
		ManageTask model = ManageTask.dao.findById(getParaToInt());
		setAttr("parentSelect", svc.selectMenu(model.getStr("dept_id")));
		setAttr("model", model);
		render(path + "edit.html");
	}
	public void delete() {
		// 日志添加
		ManageTask model = new ManageTask();
		Integer userid = getSessionUser().getUserID();
		String now = getNow();
		model.put("update_id", userid);
		model.put("update_time", now);

		model.deleteById(getParaToInt());
		list();
	}
}
