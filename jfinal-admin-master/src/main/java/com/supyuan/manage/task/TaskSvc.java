package com.supyuan.manage.task;

import java.util.List;

import com.supyuan.jfinal.base.BaseService;
import com.supyuan.system.menu.SysMenu;

public class TaskSvc extends BaseService {

	/**
	 * 获取根目录下拉框
	 * 
	 * 2015年4月28日 上午11:42:54 flyfox 369191470@qq.com
	 * 
	 * @param selected
	 * @return
	 */
	public String selectMenu(String selected) {
		List<ManageTask> list = ManageTask.dao.find(" select distinct dept_name,dept_id from suntrans_dept_pro_info  order by id ");
		StringBuffer sb = new StringBuffer();
		for (ManageTask menu : list) {
			sb.append("<option value=");
			sb.append(menu.get("dept_id"));
			sb.append(" ");
			if(menu.getStr("dept_id").equals(selected))
			sb.append("selected");
			sb.append(">");
			sb.append(menu.getStr("dept_name"));
			sb.append("</option>");
		}
		return sb.toString();
	}

	/**
	 * 获取父节点名称
	 * 
	 * 2015年4月28日 上午11:43:07 flyfox 369191470@qq.com
	 * 
	 * @param model
	 * @return
	 */
	public String getParentName(ManageTask model) {
		Integer parentid = model.getInt("parentid");
		if (parentid == null || parentid == 0) {
			return "根目录";
		}
		String parentName = SysMenu.dao.findById(model.getInt("parentid")).getStr("name");
		return parentName;
	}

	/**
	 * 根据父节点获取List
	 * 
	 * 2015年4月28日 上午11:43:07 flyfox 369191470@qq.com
	 * 
	 * @param parentid
	 * @return
	 */
	public List<ManageTask> getListByParentid(int parentid) {
		List<ManageTask> list = ManageTask.dao.findByWhere(" where  status = 1 and parentid = ? order by sort ", parentid);
		return list;
	}
}
