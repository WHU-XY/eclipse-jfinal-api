package com.whu.jFinal.api;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.ICallback;
import com.jfinal.plugin.activerecord.Record;
import com.whu.jFinal.bean.Code;
import com.whu.jFinal.common.Require;
import com.whu.jFinal.interceptor.TokenInterceptor;
import com.whu.jFinal.model.RolesInfo;
import com.whu.jFinal.model.StudentInfo;
import com.whu.jFinal.model.TestModel;

import com.whu.jFinal.model.Version;
import com.whu.jFinal.response.InquiryDBResponse;
import com.whu.jFinal.response.InquiryResponse;
import com.whu.jFinal.response.InquiryRoleResponse;
import com.whu.jFinal.response.InquiryRoomIdResponse;
import com.whu.jFinal.response.LoginResponse;
import com.whu.jFinal.response.TestResponse;

@Before(TokenInterceptor.class)
public class InquiryAPIController extends BaseAPIController {

	public void Inquiry_UserInfo() {

		String student_id = getPara("student_id");
		// 校验参数, 确保不能为空
		if (!notNull(Require.me().put(student_id, "student_id can not be null"))) {
			return;
		}
		String sql = "SELECT * FROM admin_student_info WHERE student_id=?";
		StudentInfo nowStudent = StudentInfo.dao.findFirst(sql, student_id);
		InquiryResponse response = new InquiryResponse();
		if (nowStudent == null) {
			response.setCode(Code.FAIL).setMessage("student_id is error");
			renderJson(response);
			return;
		}
		Map<String, Object> studentInfo = new HashMap<String, Object>(nowStudent.getAttrs());
		studentInfo.remove("password");
		response.setInfo(studentInfo);
		response.setMessage("Inquiry_UserInfo Success");
		renderJson(response);
	}

	public void Inquiry_RolesInfo() {
		String sql = "SELECT * FROM admin_role_info";
		List<RolesInfo> nowRole = RolesInfo.dao.find(sql);
		InquiryRoleResponse response = new InquiryRoleResponse();
		if (nowRole == null) {
			response.setCode(Code.FAIL).setMessage("Inquiry_RolesInfo fail，please try again");
			renderJson(response);
			return;
		}
		response.setInfo(nowRole);
		response.setMessage("Inquiry_RolesInfo Success");
		renderJson(response);

	}

	public void Inquiry_Version() {

		String type = getPara("type");

		String sql = "SELECT * FROM admin_version_info WHERE type=?";
		Version nowVersion = Version.dao.findFirst(sql, type);
		InquiryResponse response = new InquiryResponse();
		if (nowVersion == null) {
			response.setCode(Code.FAIL).setMessage("type is error");
			renderJson(response);
			return;
		}
		Map<String, Object> VersionInfo = new HashMap<String, Object>(nowVersion.getAttrs());
		response.setInfo(VersionInfo);
		response.setMessage("Inquiry_Version Success");
		renderJson(response);

	}

	public void Inquiry_Building() {

		String sql = "SELECT room_id,area,building,floor,room_num FROM admin_room_info";
		List<Record> nowBuilding = Db.find(sql);
		InquiryDBResponse response = new InquiryDBResponse();
		if (nowBuilding == null) {
			response.setCode(Code.FAIL).setMessage("Inquiry_Building fail，please try again");
			renderJson(response);
			return;
		}
		response.setInfo(nowBuilding);
		response.setMessage("Inquiry_Building Success");
		renderJson(response);

	}

	public void Inquiry_Building_Detail() {

		String area = getPara("area");
		String building = getPara("building");
		String floor = getPara("floor");
		List<Record> nowBuilding;

		if (!notNull(Require.me().put(area, "area can not be null").put(building, "building can not be null").put(floor,
				"floor can not be null"))) {
			return;
		}
		if (floor.equals("0")) {
			String sql = "SELECT room_id,area,building,floor,room_num FROM admin_room_info where area=? and building=? ";
			nowBuilding = Db.find(sql, area, building);
		} else {
			String sql = "SELECT room_id,area,building,floor,room_num FROM admin_room_info where area=? and building=? and floor=?";
			nowBuilding = Db.find(sql, area, building, floor);
		}
		InquiryDBResponse response = new InquiryDBResponse();
		if (nowBuilding.isEmpty()) {
			response.setCode(Code.FAIL).setMessage("area、building or floor is error");
			renderJson(response);
			return;
		}
		response.setInfo(nowBuilding);
		response.setMessage("Inquiry_Building_Detail Success");
		renderJson(response);

	}

	public void Inquiry_Student() {

		String sql = "SELECT a.room_id,a.area,a.building,a.floor,a.room_num,b.student_name,b.academy,b.professional"
				+ " FROM admin_room_info as a,admin_student_info as b where a.room_id=b.room_id";
		List<Record> nowBuilding = Db.find(sql);
		InquiryDBResponse response = new InquiryDBResponse();
		if (nowBuilding == null) {
			response.setCode(Code.FAIL).setMessage("Inquiry_Student fail，please try again");
			renderJson(response);
			return;
		}
		response.setInfo(nowBuilding);
		response.setMessage("Inquiry_Student Success");
		renderJson(response);

	}

	public void Inquiry_Student_Detail() {

		String area = getPara("area");
		String building = getPara("building");
		String floor = getPara("floor");
		List<Record> nowStudent;

		if (!notNull(Require.me()
				.put(area, "area can not be null").
				put(building, "building can not be null").
				put(floor,"floor can not be null")))
		{
			return;
		}
		if (floor.equals("0")) {
			String sql = "SELECT a.room_id,a.area,a.building,a.floor,a.room_num,b.student_name,b.academy,b.professional"
					+ " FROM admin_room_info as a,admin_student_info as b where a.room_id=b.room_id and a.area=? and a.building=?";
			nowStudent = Db.find(sql, area, building);
		} else {
			String sql = "SELECT a.room_id,a.area,a.building,a.floor,a.room_num,b.student_name,b.academy,b.professional"
					+ " FROM admin_room_info as a,admin_student_info as b where a.room_id=b.room_id and a.area=? and a.building=? and a.floor=?";
			nowStudent = Db.find(sql, area, building, floor);
		}
		InquiryDBResponse response = new InquiryDBResponse();
		if (nowStudent.isEmpty()) {
			response.setCode(Code.FAIL).setMessage("area、building or floor is error");
			renderJson(response);
			return;
		}
		response.setInfo(nowStudent);
		response.setMessage("Inquiry_Student_Detail Success");
		renderJson(response);

	}
	
	public void Inquiry_RoomDetail_RoomId() {

		String room_id = getPara("room_id");
		List<Record> nowRoom;
		List<Record> nowRoom1;
		List<Record> nowRoom2;
		List<Record> nowRoom3;

		if (!notNull(Require.me()
				.put(room_id,"room_id can not be null")))
		{
			return;
		}
		
		String sql = "SELECT a.account_balance,a.subsidy,a.mon_elec_consumption,a.total_elec_consumption "
				+ "FROM admin_account_elec_info as a where a.room_id=?";
		nowRoom = Db.find(sql,room_id);
		
		String sql1 = "SELECT b.name,b.status FROM admin_device_channel as b,admin_device_info as a where b.dev_id=a.dev_id and a.room_id=?";
		nowRoom1 = Db.find(sql1,room_id);
		
		String sql2 = "SELECT a.power,a.electricity,a.U_value,a.I_value,a.power_rate FROM admin_elec_meter_data as a,admin_elec_meter_info as b where b.dev_id=a.dev_id and b.room_id=?";
		nowRoom2 = Db.find(sql2,room_id);
		
		String sql3 = "SELECT student_name FROM admin_student_info where room_id=?";
		nowRoom3 = Db.find(sql3,room_id);
		
		InquiryRoomIdResponse response = new InquiryRoomIdResponse();
		if (nowRoom.isEmpty()||nowRoom1.isEmpty()||nowRoom2.isEmpty()||nowRoom3.isEmpty()) {
			response.setCode(Code.FAIL).setMessage("room_id is error");
			renderJson(response);
			return;
		}
		
		response.setaccount(nowRoom);
		response.setdev_channel(nowRoom1);
		response.setmeter_info(nowRoom2);
		response.setroom_stu(nowRoom3);
		
		response.setMessage("Inquiry_RoomDetail_RoomId Success");
		renderJson(response);
 
	}

	public void Inquiry_Data_History() {

		String room_id = getPara("room_id");
		String type = getPara("type");
		List<Record> nowStudent=null;

		if (!notNull(Require.me()
				.put(room_id,"room_id can not be null")
				.put(type,"type can not be null")
				))
		{
			return;
		}
		if (type.equals("account_balance")) {
			String sql = "SELECT account_balance from admin_account_elec_info where room_id=?";
			nowStudent = Db.find(sql,room_id);
		} 
		else if(type.equals("U_value")){
			String sql = "SELECT a.U_value from admin_elec_meter_data as a,admin_elec_meter_info as b where b.dev_id=a.dev_id and b.room_id=?";
			nowStudent = Db.find(sql,room_id);
		}else if(type.equals("I_value")){
			String sql = "SELECT a.I_value from admin_elec_meter_data as a,admin_elec_meter_info as b where b.dev_id=a.dev_id and b.room_id=?";
			nowStudent = Db.find(sql,room_id);
		}else if(type.equals("power")) {
			String sql = "SELECT a.power from admin_elec_meter_data as a,admin_elec_meter_info as b where b.dev_id=a.dev_id and b.room_id=?";
			nowStudent = Db.find(sql,room_id);
		}else if(type.equals("power_rate")) {
			String sql = "SELECT a.power_rate from admin_elec_meter_data as a,admin_elec_meter_info as b where b.dev_id=a.dev_id and b.room_id=?";
			nowStudent = Db.find(sql,room_id);
		}
		/*
		 * switch(type) { case "account_balance": String sql =
		 * "SELECT a.room_id,a.area,a.building,a.floor,a.room_num,b.student_name,b.academy,b.professional"
		 * +
		 * " FROM admin_room_info as a,admin_student_info as b where a.room_id=b.room_id and a.area=? and a.building=?"
		 * ; nowStudent = Db.find(sql,room_id);break; }
		 */
		InquiryDBResponse response = new InquiryDBResponse();
		if (nowStudent.isEmpty()) {
			response.setCode(Code.FAIL).setMessage("area、building or floor is error");
			renderJson(response);
			return;
		}
		response.setInfo(nowStudent);
		response.setMessage("Inquiry_Student_Detail Success");
		renderJson(response);

	}
	
	//测试用mysql存储过程
	@ClearInterceptor
	public void Inquiry_Data_Service() {

		LoginResponse response = new LoginResponse();
		
		final Map<String, Object> map = new HashMap<String, Object>();
        Db.execute(new ICallback() {
        	@Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
            	CallableStatement proc = null;
                try {
                    proc = arg0.prepareCall("{call add_pro(?,?,?)}");
                    proc.setInt(1, 122);
                    proc.setInt(2, 2);
                    proc.registerOutParameter(3, Types.INTEGER);
                    proc.execute();
                    map.put("nickName",proc.getInt(3));
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    if(proc!=null){
                        proc.close();
                    }
                    if(arg0!=null){
                    	arg0.close();
                    }  
                }
            	return null;
			}
        });
        response.setInfo(map);
        renderJson(response);
	}
	
	
	@ClearInterceptor
	public void Inquiry_Data_Service1() {

		
		
		TestResponse response = new TestResponse();
		List<TestModel> list=new ArrayList<>();
        Db.execute(new ICallback() {
            @Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
            	CallableStatement proc = null;
                try {
                    proc = arg0.prepareCall("{call admin_procedure_01()}");
                    boolean hadResults = proc.execute();
                    while(hadResults) {
                    	ResultSet rs = proc.getResultSet();
                    	
                    	while(rs!=null&&rs.next()) {
                    		TestModel model = new TestModel();
                    		model.setuserId(rs.getString(1));
                    		model.setloginName(rs.getString(2));
                    		model.setnickName(rs.getString(3));
                    		model.setpassword(rs.getString(4));
                    		model.setsex(rs.getInt(5));
                    		model.setemail(rs.getString(6));
                    		list.add(model);
              
                    	}
                    	hadResults = proc.getMoreResults();
                    	
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    if(proc!=null){
                        proc.close();
                    }
                    if(arg0!=null){
                    	arg0.close();
                    }  
                }
            	return null;
			}
        });
       response.setInfo(list);
       response.setMessage("Inquiry_Data_Service1 success");
       renderJson(response);//返回数据模型 response其实是一个模型
        //renderJson(list);//list是List<TestModel>序列，实际应用时运用上一种方法更好有具体格式
        
	}
	
	@ClearInterceptor
	public void Inquiry_Data_Service2() {

		
		
		TestResponse response = new TestResponse();
		List<TestModel> list=new ArrayList<>();
        Db.execute(new ICallback() {
            @Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
            	CallableStatement proc = null;
                try {
                    proc = arg0.prepareCall("{call admin_procedure_01()}");
                    boolean hadResults = proc.execute();
                    while(hadResults) {
                    	ResultSet rs = proc.getResultSet();
                    	
                    	while(rs!=null&&rs.next()) {
                    		TestModel model = new TestModel();
                    		model.setuserId(rs.getString(1));
                    		model.setloginName(rs.getString(2));
                    		model.setnickName(rs.getString(3));
                    		model.setpassword(rs.getString(4));
                    		model.setsex(rs.getInt(5));
                    		model.setemail(rs.getString(6));
                    		list.add(model);
              
                    	}
                    	hadResults = proc.getMoreResults();
                    	
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    if(proc!=null){
                        proc.close();
                    }
                    if(arg0!=null){
                    	arg0.close();
                    }  
                }
            	return null;
			}
        });
       response.setInfo(list);
       response.setMessage("Inquiry_Data_Service1 success");
       renderJson(response);//返回数据模型 response其实是一个模型
        //renderJson(list);//list是List<TestModel>序列，实际应用时运用上一种方法更好有具体格式
        
	}
}
 