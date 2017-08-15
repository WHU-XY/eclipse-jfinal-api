package com.whu.jFinal.api;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.ICallback;
import com.jfinal.plugin.activerecord.Record;
import com.whu.jFinal.bean.Code;
import com.whu.jFinal.interceptor.TokenInterceptor;
import com.whu.jFinal.model.ScenceDeviceModel;
import com.whu.jFinal.response.InquiryDBResponse;
import com.whu.jFinal.response.ScenceDeviceResponse;

@Before(TokenInterceptor.class)
public class SmartBuildingAPIController extends BaseAPIController {

	@ClearInterceptor
	public void Inquiry_Scence_Model() {

		String sql = "SELECT a.name,a.scence_id FROM scp_smart_scence as a where a.user_id=1 and a.status=1";
		List<Record> nowScence = Db.find(sql);
		InquiryDBResponse response = new InquiryDBResponse();
		if (nowScence == null) {
			response.setCode(Code.FAIL).setMessage("Inquiry_Scence_Model fail，please try again");
			renderJson(response);
			return;
		}
		response.setInfo(nowScence);
		response.setMessage("Inquiry_Scence_Model Success");
		renderJson(response);

	}

	@ClearInterceptor
	public void Inquiry_Scence_Device() {
		String scence_id = getPara("scence_id");
		ScenceDeviceResponse response = new ScenceDeviceResponse();
		List<ScenceDeviceModel> list = new ArrayList<>();
		Db.execute(new ICallback() {
			@Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
				CallableStatement proc = null;
				try {
					proc = arg0.prepareCall("{call select_scence_device(?)}");
					proc.setString(1, scence_id);
					boolean hadResults = proc.execute();
					while (hadResults) {
						ResultSet rs = proc.getResultSet();

						while (rs != null && rs.next()) {
							ScenceDeviceModel model = new ScenceDeviceModel();
							model.setname(rs.getString(1));
							model.setstatus(rs.getString(2));
							model.setdev_id(rs.getString(3));
							model.setchannel_num(rs.getString(4));
							model.setchannel_id(rs.getString(5));
							list.add(model);
						}
						hadResults = proc.getMoreResults();

					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (proc != null) {
						proc.close();
					}
					if (arg0 != null) {
						arg0.close();
					}
				}
				return null;
			}
		});
		response.setInfo(list);
		response.setMessage("Inquiry_Scence_Device success");
		renderJson(response);// 返回数据模型 response其实是一个模型

	}
}
