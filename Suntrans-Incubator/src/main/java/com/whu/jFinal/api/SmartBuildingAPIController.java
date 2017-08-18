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
import com.whu.jFinal.common.Require;
import com.whu.jFinal.interceptor.TokenInterceptor;
import com.whu.jFinal.model.ScenceDeviceModel;
import com.whu.jFinal.response.InquiryDBResponse;
import com.whu.jFinal.response.ScenceDeviceResponse;

@Before(TokenInterceptor.class)
public class SmartBuildingAPIController extends BaseAPIController {

	
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

	public void Inquiry_Scence_Device() {
		String scence_id = getPara("scence_id");

		// 校验必填项参数
		if (!notNull(Require.me().put(scence_id, "scence_id can not be null"))) {
			return;
		}

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

	public void Inquiry_Controll_Index() {

		String user_level = getPara("user_level");

		// 校验必填项参数
		if (!notNull(Require.me().put(user_level, "user_level can not be null"))) {
			return;
		}

		InquiryDBResponse response = new InquiryDBResponse();
		List<Record> list = new ArrayList<>();
		Db.execute(new ICallback() {
			@Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
				CallableStatement proc = null;
				try {
					proc = arg0.prepareCall("{call select_controll_dev(?)}");
					proc.setString(1, user_level);
					boolean hadResults = proc.execute();
					while (hadResults) {
						ResultSet rs = proc.getResultSet();

						while (rs != null && rs.next()) {
							Record dev = new Record();
							dev.set("dev_id", rs.getString(1));
							dev.set("title", rs.getString(2));
							list.add(dev);
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
		response.setMessage("Inquiry_Index success");
		renderJson(response);// 返回数据模型 response其实是一个模型

	}

	public void Inquiry_Controll_DevInfo() {

		String dev_id = getPara("dev_id");

		// 校验必填项参数
		if (!notNull(Require.me().put(dev_id, "dev_id can not be null"))) {
			return;
		}

		InquiryDBResponse response = new InquiryDBResponse();
		List<Record> list = new ArrayList<>();
		Db.execute(new ICallback() {
			@Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
				CallableStatement proc = null;
				try {
					proc = arg0.prepareCall("{call select_controll_devInfo(?)}");
					proc.setString(1, dev_id);
					boolean hadResults = proc.execute();
					while (hadResults) {
						ResultSet rs = proc.getResultSet();

						while (rs != null && rs.next()) {
							Record dev = new Record();
							dev.set("dev_id", rs.getString(1));
							dev.set("channel_id", rs.getString(2));
							dev.set("channel_num", rs.getString(3));
							dev.set("name", rs.getString(4));
							dev.set("status", rs.getString(5));
							list.add(dev);
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
		response.setMessage("Inquiry_Controll_DevInfo success");
		renderJson(response);// 返回数据模型 response其实是一个模型

	}


	public void Inquiry_Environment_Index() {

		String user_level = getPara("user_level");

		// 校验必填项参数
		if (!notNull(Require.me().put(user_level, "user_level can not be null"))) {
			return;
		}

		InquiryDBResponse response = new InquiryDBResponse();
		List<Record> list = new ArrayList<>();
		if (!user_level.equals("0")) {
			response.setCode(Code.FAIL);
			response.setMessage("Inquiry_Environment_Index failed");
			renderJson(response);// 返回数据模型 response其实是一个模型
			return;
		}		
		Db.execute(new ICallback() {
			@Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
				CallableStatement proc = null;
				try {
					proc = arg0.prepareCall("{call select_Environment_info()}");
					boolean hadResults = proc.execute();
					while (hadResults) {
						ResultSet rs = proc.getResultSet();

						while (rs != null && rs.next()) {
							Record dev = new Record();
							dev.set("dev_id", rs.getString(1));
							dev.set("update_time", rs.getString(2));
							dev.set("pm1", rs.getString(3)+"ug/m³");
							dev.set("pm10", rs.getString(4)+"ug/m³");
							dev.set("pm25", rs.getString(5)+"ug/m³");
							dev.set("jiaquan", rs.getString(6)+"ug/m³");
							dev.set("yanwu", rs.getString(7)+"ug/m³");
							dev.set("wendu", rs.getString(8)+"℃");
							dev.set("shidu", rs.getString(9)+"%Rh");
							dev.set("renyuan", rs.getString(10));
							dev.set("x-zhou", rs.getString(11));
							dev.set("y-zhou", rs.getString(12));
							dev.set("z-zhou", rs.getString(13));
							dev.set("zhengdong", rs.getString(14));
							dev.set("guangzhao", rs.getString(15));
							dev.set("daqiya", rs.getString(16)+"KPa");
							list.add(dev);
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
		response.setMessage("Inquiry_Environment_Index success");
		renderJson(response);// 返回数据模型 response其实是一个模型

	}
	
	public void Inquiry_Ammeter_Index() {

		String user_level = getPara("user_level");

		// 校验必填项参数
		if (!notNull(Require.me().put(user_level, "user_level can not be null"))) {
			return;
		}

		InquiryDBResponse response = new InquiryDBResponse();
		List<Record> list = new ArrayList<>();
		Db.execute(new ICallback() {
			@Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
				CallableStatement proc = null;
				try {
					proc = arg0.prepareCall("{call select_ammeter_dev(?)}");
					proc.setString(1, user_level);
					boolean hadResults = proc.execute();
					while (hadResults) {
						ResultSet rs = proc.getResultSet();

						while (rs != null && rs.next()) {
							Record dev = new Record();
							dev.set("dev_id", rs.getString(1));
							dev.set("sno", rs.getString(2));
							list.add(dev);
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
		response.setMessage("Inquiry_Ammeter_Index success");
		renderJson(response);// 返回数据模型 response其实是一个模型
	}
	

	public void Inquiry_Ammeter_Current() {

		String sno = getPara("sno");

		// 校验必填项参数
		if (!notNull(Require.me().put(sno, "user_level can not be null"))) {
			return;
		}

		InquiryDBResponse response = new InquiryDBResponse();
		List<Record> list = new ArrayList<>();
		Db.execute(new ICallback() {
			@Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
				CallableStatement proc = null;
				try {
					proc = arg0.prepareCall("{call select_ammeter_current(?)}");
					proc.setString(1, sno);
					boolean hadResults = proc.execute();
					while (hadResults) {
						ResultSet rs = proc.getResultSet();
						while (rs != null && rs.next()) {
							Record dev = new Record();
							dev.set("dev_id", rs.getString(1));
							dev.set("sno", rs.getString(2));
							dev.set("update_time", rs.getString(3));
							dev.set("E", rs.getString(4)+"kw.h");
							dev.set("U", rs.getString(5)+"V");
							dev.set("I", rs.getString(6)+"A");
							dev.set("P", rs.getString(7)+"KW");
							dev.set("PR", rs.getString(8));
							list.add(dev);
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
		response.setMessage("Inquiry_Ammeter_Current success");
		renderJson(response);// 返回数据模型 response其实是一个模型
	}
	

	@ClearInterceptor
	public void Inquiry_Ammeter_History() {

		String sno = getPara("sno");
		String data_type = getPara("data_type");
		// 校验必填项参数
		if (!notNull(Require.me()
				.put(data_type, "data_type can not be null")
				.put(sno, "sno can not be null"))) {
			return;
		}

		InquiryDBResponse response = new InquiryDBResponse();
		List<Record> list = new ArrayList<>();
		Db.execute(new ICallback() {
			@Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
				CallableStatement proc = null;
				try {
					proc = arg0.prepareCall("{call select_ammeter_history(?,?)}");
					proc.setString(1, sno);
					proc.setString(2, data_type);
					boolean hadResults = proc.execute();
					while (hadResults) {
						ResultSet rs = proc.getResultSet();
						while (rs != null && rs.next()) {
							Record dev = new Record();
							dev.set("data", rs.getString(1));
							dev.set("update_time", rs.getString(2));
							list.add(dev);
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
		response.setMessage("Inquiry_Ammeter_History success");
		renderJson(response);// 返回数据模型 response其实是一个模型
	}

    
}
