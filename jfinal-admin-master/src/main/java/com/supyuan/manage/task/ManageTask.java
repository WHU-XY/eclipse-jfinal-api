package com.supyuan.manage.task;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;


@ModelBind(table = "suntrans_dept_pro_info")
public class ManageTask extends BaseProjectModel<ManageTask> {

	private static final long serialVersionUID = 1L;
	public static final ManageTask dao = new ManageTask();
}
