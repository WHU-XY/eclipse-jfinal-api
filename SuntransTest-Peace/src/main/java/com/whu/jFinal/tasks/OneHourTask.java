package com.whu.jFinal.tasks;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.ICallback;

public class OneHourTask implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub	
		Db.execute(new ICallback() {
			@Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
				CallableStatement proc = null;
				try {
					proc = arg0.prepareCall("{call calculate_ammeter_hour_data()}");
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
		Db.execute(new ICallback() {
			@Override
			public Object call(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
				CallableStatement proc = null;
				try {
					proc = arg0.prepareCall("{call calculate_3ammeter_hour_data()}");
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
		System.out.println("OneHourTask done"); 
	}

}
