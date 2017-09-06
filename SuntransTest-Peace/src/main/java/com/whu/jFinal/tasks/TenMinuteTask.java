package com.whu.jFinal.tasks;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.ICallback;

/**
*@author WHU-XY 作者 E-mail:2013301470047@whu.edu.cn
*@version 1.0 创建时间：2017年9月6日下午12:14:29
*/
public class TenMinuteTask implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub

		Db.execute(new ICallback() {
			@Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
				CallableStatement proc = null;
				try {
					proc = arg0.prepareCall("{call calculate_ammeter_ten_minute_data()}");
					proc.execute();
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
		System.out.println("TenMinuteTask done"); 
	}

}
