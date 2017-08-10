package com.whu.suntrans.action;

import com.jfinal.plugin.activerecord.Page;
import com.whu.suntrans.model.BaseDeptInfo;

public class DeptInfoService {
	
	private static final BaseDeptInfo dao = new BaseDeptInfo().dao();
	
	public Page<BaseDeptInfo> paginate(int pageNumber, int pageSize) {
		return dao.paginate(pageNumber, pageSize, "select *", "from suntrans_dept_info order by id asc");
	}
}
