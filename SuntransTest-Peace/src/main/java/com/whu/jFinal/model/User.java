package com.whu.jFinal.model;

import com.jfinal.plugin.activerecord.Model;

import java.util.Map;

/**
 * @author malongbo
 * @date 2015/2/13
 */
public class User extends Model<User> {
	public static String USER_ID = "userId";
	public static String USERNAME = "username";
	public static String PASSWORD = "password";
	public static String STATUS = "status";
	

	
	private static final long serialVersionUID = 1L;
	public static final User user = new User();

    /**
     * 获取用户id*
     * @return 用户id
     */
    public String userId() {
        return getStr(USER_ID);
        
    }
    
	@Override
	public Map<String, Object> getAttrs() {
		return super.getAttrs();
	}
}
