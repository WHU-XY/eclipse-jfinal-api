package com.whu.jFinal.api;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.ICallback;
import com.jfinal.plugin.activerecord.Record;
import com.whu.jFinal.bean.Code;
import com.whu.jFinal.common.Require;
import com.whu.jFinal.interceptor.TokenInterceptor;
import com.whu.jFinal.response.InquiryAmmeterHistoryResponse;
import com.whu.jFinal.response.InquiryDBResponse;
import com.whu.jFinal.response.InquiryRoomIdResponse;

@Before(TokenInterceptor.class)
public class InquiryAPIController extends BaseAPIController {
	
	@Clear
	public void Inquiry_Department_Info() {

		String sql = "SELECT distinct a.departmentID,a.departmentName FROM stp_roominfo as a";
		List<Record> area = Db.find(sql);
		List<Record> building=null;
		List<Record> floor=null;
		for(int i=0;i<area.size();i++) {
		
			String sql1 = "SELECT distinct a.building,a.building_name FROM stp_roominfo as a where a.departmentID=? order by a.building asc";
			building = Db.find(sql1,area.get(i).getInt("departmentID"));
			for(int j=0;j<building.size();j++) {
				String sql2 = "SELECT distinct a.floor,a.floor_name FROM stp_roominfo as a where a.departmentID=? and a.building=? order by a.floor asc";
				floor = Db.find(sql2,area.get(i).getInt("departmentID"),building.get(j).getInt("building"));
				building.get(j).set("sublist", floor);
			}
			area.get(i).set("sublist", building);
		}
		
		InquiryDBResponse response = new InquiryDBResponse();
		if (area.size() == 0) {
			response.setCode(Code.FAIL).setMessage("Inquiry_Department_Info Fail");
			renderJson(response);
			return;
		}
		response.setInfo(area);
		response.setMessage("Inquiry_Department_Info Success");
		renderJson(response);

	}
	
	@Clear
	public void Inquiry_Room_Info() {

		String departmentID = getPara("departmentID");
		String building = getPara("building");
		String floor = getPara("floor");

		// 校验必填项参数
		if (!notNull(Require.me()
				.put(departmentID, "departmentID can not be null")
				.put(building, "building can not be null")
				.put(floor, "floor can not be null")
				))
		{
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
					proc = arg0.prepareCall("{call inquiry_room_info(?,?,?)}");
					proc.setString(1, departmentID);
					proc.setString(2, building);
					proc.setString(3, floor);
					boolean hadResults = proc.execute();
					while (hadResults) {
						ResultSet rs = proc.getResultSet();

						while (rs != null && rs.next()) {
							Record dev = new Record();
							dev.set("departmentID", rs.getInt(1));
							dev.set("departmentName", rs.getString(2));
							dev.set("building", rs.getInt(3));
							dev.set("building_name", rs.getString(4));
							dev.set("floor", rs.getInt(5));
							dev.set("floor_name", rs.getString(6));
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
		String sql = "SELECT a.room_id,a.dormitory,b.`status` FROM stp_roominfo as a,stp_rooms as b where a.departmentID=? and a.building=? and a.floor=? and b.id=a.room_id order by a.dormitory";
		List<Record> roomlist=null;
		for(int i=0;i<list.size();i++) {
			roomlist=Db.find(sql,list.get(i).getInt("departmentID"),list.get(i).getInt("building"),list.get(i).getInt("floor"));
			list.get(i).set("sublist", roomlist);
		}
		response.setInfo(list);
		response.setMessage("Inquiry_Room_Info success");
		renderJson(response);// 返回数据模型 response其实是一个模型

	}
	
	@Clear
	public void Inquiry_Room_Detail_Byrid() {

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
		
		String sql = "SELECT a.id,a.balans,a.status,b.E_usevalue as dayuse,c.E_usevalue as monthuse,c.E_value as totaluse FROM stp_rooms as a,stp_api_ammeter_this_day_data as b,"
				+ "stp_api_ammeter_this_month_data as c where a.id=b.room_id and b.room_id=c.room_id and c.room_id=?";
		nowRoom = Db.find(sql,room_id);
		
		String sql1 = "SELECT b.id,b.num,b.name,b.status,c.addr,b.slc_id,d.status as sd_status FROM stp_slc_channel as b,stp_slc as c,stp_rooms as d where c.room_id=? and c.id=b.slc_id and d.id=c.room_id";
		nowRoom1 = Db.find(sql1,room_id);
		
		String sql2 = "SELECT a.id,a.V_value,a.I_value,a.P_value,a.PR_value,a.E_value FROM stp_electricity_current as a where a.room_id=?";
		nowRoom2 = Db.find(sql2,room_id);
		
		String sql3 = "SELECT name,academy,studentID,telephone FROM stp_hp_student where room_id=?";
		nowRoom3 = Db.find(sql3,room_id);
		
		InquiryRoomIdResponse response = new InquiryRoomIdResponse();
		if (nowRoom.isEmpty()||nowRoom1.isEmpty()||nowRoom2.isEmpty()||nowRoom3.isEmpty()) {
			response.setCode(Code.FAIL).setMessage("room_id is error");
			renderJson(response);
			return;
		}
		List<Record> list = new ArrayList<>();
		for(int i=0;i<5;i++) {
			Record info = new Record();
			if(i==0) {
				info.set("name", "电压");
				info.set("unit","V");
				if(nowRoom2.get(0).getBigDecimal("V_value")==null) {
					info.set("value",null);
				}else {
					info.set("value",nowRoom2.get(0).getBigDecimal("V_value").toString());
				}
			}else if(i==1) {
				info.set("name", "电流");
				info.set("unit","A");
				if(nowRoom2.get(0).getBigDecimal("I_value")==null) {
					info.set("value",null);
				}else {
					info.set("value",nowRoom2.get(0).getBigDecimal("I_value").toString());
				}
			}else if(i==2) {
				info.set("name", "功率");
				info.set("unit","KW");
				if(nowRoom2.get(0).getBigDecimal("P_value")==null) {
					info.set("value",null);
				}else {
					info.set("value",nowRoom2.get(0).getBigDecimal("P_value").toString());
				}
			}else if(i==3) {
				info.set("name", "功率因数");
				info.set("unit","");
				if(nowRoom2.get(0).getBigDecimal("PR_value")==null) {
					info.set("value",null);
				}else {
					info.set("value",nowRoom2.get(0).getBigDecimal("PR_value").toString());
				}
			}else if(i==4) {
				info.set("name", "电表值");
				info.set("unit","度");
				if(nowRoom2.get(0).getBigDecimal("E_value")==null) {
					info.set("value",null);
				}else {
					info.set("value",nowRoom2.get(0).getBigDecimal("E_value").toString());
				}
			}
			info.set("data_type",i+1);
			list.add(info);
		}
		response.setaccount(nowRoom);
		response.setdev_channel(nowRoom1);
		response.setmeter_info(list);
		response.setroom_stu(nowRoom3);
		
		response.setMessage("Inquiry_RoomDetail_RoomId Success");
		renderJson(response);

	}
	
	@Clear
	public void Inquiry_Room_Detail_Bysid() {

		String studentID = getPara("studentID");
		List<Record> nowRoom;
		List<Record> nowRoom1;
		List<Record> nowRoom2;
		List<Record> nowRoom3;

		if (!notNull(Require.me()
				.put(studentID,"username can not be null")))
		{
			return;
		}
		
		String sql = "SELECT d.room_id,a.balans,a.status,b.E_usevalue as dayuse,c.E_usevalue as monthuse,c.E_value as totaluse FROM stp_rooms as a,stp_hp_student as d,"
				+ "stp_api_ammeter_this_day_data as b,stp_api_ammeter_this_month_data as c where a.id=d.room_id and a.id=b.room_id and b.room_id=c.room_id and d.studentID=?";
		nowRoom = Db.find(sql,studentID);
		
		String sql1 = "SELECT a.id,b.room_id,a.num,a.name,a.status,c.addr FROM stp_slc_channel as a,stp_hp_student as b,stp_slc as c where a.slc_id=b.room_id and b.studentID=? and c.room_id=b.room_id";
		nowRoom1 = Db.find(sql1,studentID);
		
		String sql2 = "SELECT a.id,b.room_id,a.V_value,a.I_value,a.P_value,a.PR_value,a.E_value FROM stp_electricity_current as a,stp_hp_student as b where a.room_id=b.room_id and b.studentID=?";
		nowRoom2 = Db.find(sql2,studentID);
		
		String sql3 = "SELECT b.room_id,a.name,a.academy,a.studentID,a.telephone FROM stp_hp_student as a,stp_hp_student as b where a.room_id=b.room_id and b.studentID=?";
		nowRoom3 = Db.find(sql3,studentID);
		
		InquiryRoomIdResponse response = new InquiryRoomIdResponse();
		if (nowRoom.isEmpty()||nowRoom1.isEmpty()||nowRoom2.isEmpty()||nowRoom3.isEmpty()) {
			response.setCode(Code.FAIL).setMessage("username is error");
			renderJson(response);
			return;
		}
		
		List<Record> list = new ArrayList<>();
		for(int i=0;i<5;i++) {
			Record info = new Record();
			if(i==0) {
				info.set("name", "电压");
				info.set("unit","V");
				info.set("value",nowRoom2.get(0).get("V_value").toString());
			}else if(i==1) {
				info.set("name", "电流");
				info.set("unit","A");
				info.set("value",nowRoom2.get(0).get("I_value").toString());
			}else if(i==2) {
				info.set("name", "功率");
				info.set("unit","KW");
				info.set("value",nowRoom2.get(0).get("P_value").toString());
			}else if(i==3) {
				info.set("name", "功率因数");
				info.set("unit","");
				info.set("value",nowRoom2.get(0).get("PR_value").toString());
			}else if(i==4) {
				info.set("name", "用电量");
				info.set("unit","度");
				info.set("value",nowRoom2.get(0).get("E_value").toString());
			}
			info.set("data_type",i+1);
			list.add(info);
		}
		
		response.setaccount(nowRoom);
		response.setdev_channel(nowRoom1);
		response.setmeter_info(list);
		response.setroom_stu(nowRoom3);
		
		response.setMessage("Inquiry_RoomDetail_RoomId Success");
		renderJson(response);

	}
	
	@Clear
	public void Inquiry_Ammeter_History() {

		String room_id = getPara("room_id");
		String data_type = getPara("data_type");

		if (!notNull(Require.me()
				.put(room_id,"room_id can not be null")
				.put(data_type,"data_type can not be null")
				))
		{
			return;
		}		
		
		InquiryAmmeterHistoryResponse response = new InquiryAmmeterHistoryResponse();
		List<Record> list = new ArrayList<>();
		Db.execute(new ICallback() {
			@Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
				CallableStatement proc = null;
				try {
					proc = arg0.prepareCall("{call inquiry_ammeter_history_day(?,?)}");
					proc.setString(1, room_id);
					proc.setString(2, data_type);
					boolean hadResults = proc.execute();
					while (hadResults) {
						ResultSet rs = proc.getResultSet();
                        int i=1;
						while (rs != null && rs.next()) {
							Record dev = new Record();
							dev.set("x", i);
							dev.set("data", rs.getString(2));
							dev.set("update_time", rs.getString(1).substring(0, 19));
							list.add(dev);
							i++;
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
		
		List<Record> list1 = new ArrayList<>();
		Db.execute(new ICallback() {
			@Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
				CallableStatement proc = null;
				try {
					proc = arg0.prepareCall("{call inquiry_ammeter_history_week(?,?)}");
					proc.setString(1, room_id);
					proc.setString(2, data_type);
					boolean hadResults = proc.execute();
					while (hadResults) {
						ResultSet rs = proc.getResultSet();
                        int i=1;
						while (rs != null && rs.next()) {
							Record dev = new Record();
							dev.set("x", i);
							dev.set("data", rs.getString(2));
							dev.set("update_time", rs.getString(1));
							list1.add(dev);
							i++;
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
		
		List<Record> list2 = new ArrayList<>();
		Db.execute(new ICallback() {
			@Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
				CallableStatement proc = null;
				try {
					proc = arg0.prepareCall("{call inquiry_ammeter_history_month(?,?)}");
					proc.setString(1, room_id);
					proc.setString(2, data_type);
					boolean hadResults = proc.execute();
					while (hadResults) {
						ResultSet rs = proc.getResultSet();
                        int i=1;
						while (rs != null && rs.next()) {
							Record dev = new Record();
							dev.set("x", i);
							dev.set("data", rs.getString(2));
							dev.set("update_time", rs.getString(1));
							list2.add(dev);
							i++;
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
				
		response.setDay_data(list);
		response.setWeek_data(list1);
		response.setMonth_data(list2);
		response.setMessage("Inquiry_Ammeter_History success");
		renderJson(response);// 返回数据模型 response其实是一个模型

	}
	
	@Clear
	public void Inquiry_Student_Info() {

		String departmentID = getPara("departmentID");
		String building = getPara("building");
		String floor = getPara("floor");

		// 校验必填项参数
		if (!notNull(Require.me()
				.put(departmentID, "departmentID can not be null")
				.put(building, "building can not be null")
				.put(floor, "floor can not be null")
				))
		{
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
					proc = arg0.prepareCall("{call inquiry_student_info(?,?,?)}");
					proc.setString(1, departmentID);
					proc.setString(2, building);
					proc.setString(3, floor);
					boolean hadResults = proc.execute();
					while (hadResults) {
						ResultSet rs = proc.getResultSet();

						while (rs != null && rs.next()) {
							Record dev = new Record();
							dev.set("departmentID", rs.getInt(1));
							dev.set("departmentName", rs.getString(2));
							dev.set("building", rs.getString(3));
							dev.set("floor", rs.getString(4));
							dev.set("dormitory", rs.getString(5));
							dev.set("room_id", rs.getInt(6));
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
		List<Record> student=null;
		String sql = "SELECT  a.name,a.studentID,a.academy FROM stp_hp_student as a where a.room_id=?";
		for(int i=0;i<list.size();i++) {
			student=Db.find(sql,list.get(i).getInt("room_id"));
			list.get(i).set("sublist", student);
		}
		response.setInfo(list);
		response.setMessage("Inquiry_Student_Info success");
		renderJson(response);// 返回数据模型 response其实是一个模型

	}
	
	@Clear
	public void Inquiry_Student_Detail_BySid() {

		String studentID = getPara("studentID");

		// 校验必填项参数
		if (!notNull(Require.me()
				.put(studentID, "studentID can not be null")
				))
		{
			return;
		}
		
		String sql = "SELECT a.studentID,a.academy,a.major,a.telephone,b.departmentID,b.building,b.dormitory FROM stp_hp_student as a,stp_roominfo as b where a.studentID=? and b.room_id=a.room_id";
		List<Record> nowBuilding = Db.find(sql,studentID);
		InquiryDBResponse response = new InquiryDBResponse();
		if (nowBuilding == null) {
			response.setCode(Code.FAIL).setMessage("Inquiry_Student_Detail_BySid fail，please try again");
			renderJson(response);
			return;
		}
		response.setInfo(nowBuilding);
		response.setMessage("Inquiry_Student_Detail_BySid Success");
		renderJson(response);

	}
	
	@Clear
	public void Inquiry_3Ammeter_Current() {

		String departmentID = getPara("departmentID");
		String building = getPara("building");
		String floor = getPara("floor");
		if (!notNull(Require.me()
				.put(departmentID,"departmentID can not be null")
				.put(building,"building can not be null")
				.put(floor,"floor can not be null")
				))
		{
			return;
		}
		
		List<Record> list = new ArrayList<>();
		Db.execute(new ICallback() {
			@Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
				CallableStatement proc = null;
				try {
					proc = arg0.prepareCall("{call inquiry_3ammeter_current(?,?,?)}");
					proc.setString(1, departmentID);
					proc.setString(2, building);
					proc.setString(3, floor);
					boolean hadResults = proc.execute();
					while (hadResults) {
						ResultSet rs = proc.getResultSet();
						while (rs != null && rs.next()) {
							Record dev = new Record();
							dev.set("aV_value", rs.getFloat(1)+"V");
							dev.set("bV_value", rs.getFloat(2)+"V");
							dev.set("cV_value", rs.getFloat(3)+"V");
							dev.set("aA_value", rs.getFloat(4)+"A");
							dev.set("bA_value", rs.getFloat(5)+"A");
							dev.set("cA_value", rs.getFloat(6)+"A");
							dev.set("aP_value", rs.getFloat(7)+"W");
							dev.set("rP_value", rs.getFloat(8)+"W");
							dev.set("PR_value", rs.getFloat(9));
							dev.set("E_value", rs.getFloat(10)+"KW.H");
							dev.set("E_day_value", rs.getFloat(11)+"KW.H");
							dev.set("E_month_value", rs.getFloat(12)+"KW.H");
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
		
		InquiryDBResponse response = new InquiryDBResponse();
		if (list.isEmpty()) {
			response.setCode(Code.FAIL).setMessage("departmentID or building is error");
			renderJson(response);
			return;
		}
		response.setInfo(list);
		response.setMessage("Inquiry_3Ammeter_Current Success");
		renderJson(response);

	}
	
	//仅限学生用户查询
	@Clear
	public void Inquiry_StudentInfo_Byusername() {

		String username = getPara("username");
		String role = getPara("role");
		if (!notNull(Require.me()
				.put(username,"username can not be null")
				.put(role,"role can not be null")
				))
		{
			return;
		}
		
		List<Record> list = new ArrayList<>();
		List<Record> list1 = new ArrayList<>();
		if(role.equals("0")) {
			String sql = "SELECT b.username,b.studentID,b.userId,b.status from stp_api_user as b where b.username=?";
	        list = Db.find(sql,username);
		}else {	
		Db.execute(new ICallback() {
			@Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
				CallableStatement proc = null;
				try {
					proc = arg0.prepareCall("{call inquiry_studentinfo_byusername(?)}");
					proc.setString(1, username);
					boolean hadResults = proc.execute();
					while (hadResults) {
						ResultSet rs = proc.getResultSet();
						while (rs != null && rs.next()) {
							Record dev = new Record();
							dev.set("studentID", rs.getLong(1));
							dev.set("name", rs.getString(2));
							dev.set("room_id", rs.getInt(3));
							dev.set("academy", rs.getString(4));
							dev.set("major", rs.getString(5));
							dev.set("telephone", rs.getLong(6));
							dev.set("IDnumber", rs.getString(7));
							list1.add(dev);
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
		}
		InquiryDBResponse response = new InquiryDBResponse();
		if (list.isEmpty() && list1.isEmpty()) {
			response.setCode(Code.FAIL).setMessage("username is error");
			renderJson(response);
			return;
		}
		if(role.equals("0")) {
			response.setInfo(list);
		}else {
			response.setInfo(list1);
		}
		response.setMessage("Inquiry_StudentInfo_Byusername Success");
		renderJson(response);

	}
	
	
	@Clear
	public void Inquiry_Channel_Control_Log() {

		String room_id = getPara("room_id");
		String inquirytime = getPara("inquirytime");
		if (!notNull(Require.me()
				.put(room_id,"id can not be null")
				.put(inquirytime,"month can not be null")
				))
		{
			return;
		}
		

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Date date=null;
		try {
			date = df.parse(inquirytime);
		}catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		
		cal.setTime(date);
		cal.add(cal.MONTH, +1);
		Date thedate= cal.getTime();
		GregorianCalendar gclast = (GregorianCalendar) cal.getInstance();
		gclast.setTime(thedate);
		gclast.set(cal.DAY_OF_MONTH,1);
		String day_first = df.format(gclast.getTime());
		
		
		List<Record> list = new ArrayList<>();
		String sql = "SELECT b.user_id,b.created_at,b.`port`,b.`status` from stp_slc_control_log as b where b.addr=addr and b.created_at>? and b.created_at<?";
        list = Db.find(sql,inquirytime,day_first);
		
		List<Record> stuinfo;
		String sql1 = "SELECT a.name,b.userId FROM stp_hp_student as a,stp_api_user as b where room_id=? and b.userId=? and a.studentID=b.studentID";
		for(int i=0;i<list.size();i++) {
			stuinfo = Db.find(sql1,room_id,list.get(i).get("user_id"));
			if(stuinfo.size()==0) {
				list.get(i).set("name",null);
			}else {
				list.get(i).set("name", stuinfo.get(0).get("name"));
			}	
		  if(list.get(i).getBoolean("status")) {
			  	switch(list.get(i).getInt("port")) {
		  			case 1:list.get(i).set("message","打开了照明开关");break;
		  			case 2:list.get(i).set("message","打开了插座开关");break;
		  			case 3:list.get(i).set("message","打开了阳台开关");break;
		  			case 4:list.get(i).set("message","打开了洗手间开关");break;
		  			default:list.get(i).set("message","打开了总开关");break;
			  }   	
		   }else {
			   switch(list.get(i).getInt("port")) {
				  case 1:list.get(i).set("message","关闭了照明开关");break;
				  case 2:list.get(i).set("message","关闭了插座开关");break;
				  case 3:list.get(i).set("message","关闭了阳台开关");break;
				  case 4:list.get(i).set("message","关闭了洗手间开关");break;
				  default:list.get(i).set("message","关闭了总开关");break;
				  }   
		   }
		  
		}
		InquiryDBResponse response = new InquiryDBResponse();
		if (list.isEmpty()) {
			response.setCode(Code.FAIL).setMessage("room_id or inquirytimne is error");
			renderJson(response);
			return;
		}
		response.setInfo(list);
		response.setMessage("Inquiry_Channel_Control_Log Success");
		renderJson(response);

	}
  
	/*	public void Inquiry_UserInfo() {

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
		
		 * switch(type) { case "account_balance": String sql =
		 * "SELECT a.room_id,a.area,a.building,a.floor,a.room_num,b.student_name,b.academy,b.professional"
		 * +
		 * " FROM admin_room_info as a,admin_student_info as b where a.room_id=b.room_id and a.area=? and a.building=?"
		 * ; nowStudent = Db.find(sql,room_id);break; }
		 
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
	
	
	@Clear
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
	
	
	@Clear
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
        
	}*/
}
 