package com.whu.jFinal.api;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.whu.jFinal.bean.Code;
import com.whu.jFinal.common.Require;
import com.whu.jFinal.common.token.TokenManager;
import com.whu.jFinal.config.AppProperty;
import com.whu.jFinal.interceptor.TokenInterceptor;
import com.whu.jFinal.model.RegisterCode;
import com.whu.jFinal.model.User;
import com.whu.jFinal.response.BaseResponse;
import com.whu.jFinal.response.DatumResponse;
import com.whu.jFinal.response.LoginResponse;
import com.whu.jFinal.utils.DateUtils;
import com.whu.jFinal.utils.RandomUtils;
import com.whu.jFinal.utils.SMSUtils;
import com.whu.jFinal.utils.StringUtils;

import static com.whu.jFinal.model.User.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 用户账号相关的接口*
 *
 * 检查账号是否被注册: GET /api/account/checkUser
 * 发送注册验证码: POST /api/account/sendCode
 * 注册: POST /api/account/register
 * 登录： POST /api/account/login
 * 查询用户资料: GET /api/account/profile
 * 修改用户资料: PUT /api/account/profile
 * 修改密码: PUT /api/account/password
 * 修改头像: PUT /api/account/avatar
 *
 * @author malongbo
 */
@Before(TokenInterceptor.class)
public class AccountAPIController extends BaseAPIController {
	private static Logger log = Logger.getLogger(AccountAPIController.class);

    /**
     * 检查用户账号是否被注册*
     */
    @Clear
    public void checkUser() {
        String tel_num = getPara("tel_num");
        if (StringUtils.isEmpty(tel_num)) {
            renderArgumentError("tel_num为空");
            return;
        }
        //检查手机号码是否被注册
        boolean exists = Db.findFirst("SELECT * FROM stp_api_user WHERE tel_num=?", tel_num) != null;
        renderJson(new BaseResponse(exists ? Code.SUCCESS:Code.FAIL, exists ? "已注册" : "未注册"));
    }
    
    /**
     * 1. 检查是否被注册*
     * 2. 发送短信验证码*
     */
    @Clear
    public void sendCode() {
        String tel_num = getPara("tel_num");
        if (StringUtils.isEmpty(tel_num)) {
            renderArgumentError("tel_num为空");
            return;
        }

        //检查手机号码有效性
        if (!SMSUtils.isMobileNo(tel_num)) {
            renderArgumentError("手机号码格式不对");
            return;
        }

        //检查手机号码是否被注册
        if (Db.findFirst("SELECT * FROM stp_api_user WHERE tel_num=?", tel_num) != null) {
            renderJson(new BaseResponse(Code.ACCOUNT_EXISTS,"手机号已注册"));
            return;
        }

        String smsCode = SMSUtils.randomSMSCode(4);
        //发送短信验证码
        if (!SMSUtils.sendCode(tel_num, smsCode)) {
            renderFailed("验证码发送失败");
            return;
        }
        
        //保存验证码数据
        RegisterCode registerCode = new RegisterCode()
                .set(RegisterCode.MOBILE, tel_num)
                .set(RegisterCode.CODE, smsCode);

        //保存数据
        if (Db.findFirst("SELECT * FROM stp_api_register_code WHERE mobile=?", tel_num) == null) {
            registerCode.save();
        } else {
            registerCode.update();
        }
        
        renderJson(new BaseResponse("验证码已发送"));
        
    }
    
	/**
	 * 用户注册
	 */
    @Clear
	public void register(){
		//必填信息
		String tel_num = getPara("tel_num");//登录帐号
        int code = getParaToInt("code", 0);//手机验证码
        int sex = getParaToInt("sex", 0);//性别
        String password = getPara("password");//密码
		String username = getPara("username");//昵称
    	//头像信息，为空则使用默认头像地址
    	String avatar = getPara("avatar", AppProperty.me().defaultUserAvatar());

        //校验必填项参数
		if(!notNull(Require.me()
                .put(tel_num, "loginName为空")
                .put(code, "code为空")//根据业务需求决定是否使用此字段
                .put(password, "password为空")
                .put(username, "username为空"))){
			return;
		}

        //检查账户是否已被注册
        if (Db.findFirst("SELECT * FROM stp_api_user WHERE tel_num=?", tel_num) != null) {
            renderJson(new BaseResponse(Code.ACCOUNT_EXISTS, "mobile已注册"));
            return;
        }
        
        //检查验证码是否有效, 如果业务不需要，则无需保存此段代码
        if (Db.findFirst("SELECT * FROM stp_api_register_code WHERE mobile=? AND code = ?", tel_num, code) == null) {
            renderJson(new BaseResponse(Code.CODE_ERROR,"验证码不正确"));
            return;
        }
        
		//保存用户数据
		String userId = RandomUtils.randomCustomUUID();
		String salt = SMSUtils.randomSMSCode(6);
		new User()
                .set("userId", 0)
                .set(User.PASSWORD, StringUtils.encodePassword(salt+password, "md5"))
                .set(User.USERNAME, username)		    
                .set("salt", salt)
                .save();
		
        //删除验证码记录
        Db.update("DELETE FROM stp_api_register_code WHERE mobile=? AND code = ?", tel_num, code);
        
		//返回数据
		renderJson(new BaseResponse("成功"));
	}
	
	
    /**
     * 登录接口
     */
    @Clear
    public void login() {
        String username = getPara("username");
        String password = getPara("password");
        //校验参数, 确保不能为空
        if (!notNull(Require.me()
                .put(username, "用户名为空")
                .put(password, "密码为空")
        )) {
            return;
        }
        String sql = "SELECT * FROM stp_api_user WHERE username=?";
        User nowUser = User.user.findFirst(sql, username);
        LoginResponse response = new LoginResponse();
        if (nowUser == null) {
            response.setCode(Code.FAIL).setMessage("用户名出错");
            renderJson(response);
            return;
        }else if(!StringUtils.encodePassword(nowUser.getStr("salt")+password, "md5").equals(nowUser.getStr("password"))) {
        	 response.setCode(Code.FAIL).setMessage("密码出错");
             renderJson(response);
             return;
        }
        
        String token = TokenManager.getMe().generateToken(nowUser);
        
        if(Db.find("SELECT *FROM stp_api_user_token WHERE username=?",username).isEmpty()) {
        	Record usertoken = new Record().set("username", username).set("token", token);
        	Db.save("stp_api_user_token", usertoken);
        }else {
        	Db.update("UPDATE stp_api_user_token set token=? where username=?", token,username);
        }
        Map<String, Object> userInfo = new HashMap<String, Object>(nowUser.getAttrs());
        userInfo.remove(PASSWORD);
        userInfo.remove("salt");
        response.setInfo(userInfo);
        response.setMessage("登录成功");
        response.setToken(token);
        renderJson(response);
    }

    /**
     * 资料相关的接口
     */
    public void profile() {
        String method = getRequest().getMethod();
        if("post".equalsIgnoreCase(method)) {
        	updateData();
        }else {
        	render404();
        }
//        if ("get".equalsIgnoreCase(method)) { //查询资料
//            getProfile();
//        } else if ("put".equalsIgnoreCase(method)) { //修改资料
//            updateProfile();
//        } else {
//            render404();
//        }
    }


    private void updateData() {
		// TODO Auto-generated method stub
  
        BaseResponse response = new BaseResponse();
        String token = getPara("token");
        User user = TokenManager.getMe().validate(token);
        String tel = getPara("tel");
        if(user.getInt("role")==0) {
        	renderJson(response.setCode(Code.FAIL).setMessage("管理员账户暂没开通"));
        }else {
        	long stu_id=user.getLong("studentID");
            
            if(Db.find("SELECT *FROM stp_hp_student WHERE studentID=? and telephone=?",stu_id,tel).isEmpty()) {
            	Db.update("UPDATE stp_hp_student set telephone=? where studentID=?", tel,stu_id);
            }else {
            	renderJson(response.setCode(Code.FAIL).setMessage("电话号码已占用"));
            }
            renderJson(response.setCode(Code.SUCCESS).setMessage("更新成功"));
        }
        
	}

	/**
     * 查询用户资料
     */
    private void getProfile() {
        String userId = getPara("userId");
        User resultUser = null;
        if (StringUtils.isNotEmpty(userId)) {
            resultUser = User.user.findById(userId);
        } else {
            resultUser = getUser();
        }

        DatumResponse response = new DatumResponse();
        
        if (resultUser == null) {
            response.setCode(Code.FAIL).setMessage("user is not found");
        } else {
            HashMap<String, Object> map = new HashMap<String, Object>(resultUser.getAttrs());
            map.remove(PASSWORD);
            response.setDatum(map);
        }

        renderJson(response);
    }

    /**
     * 修改用户资料
     */
    private void updateProfile() {
        boolean flag = false;
        BaseResponse response = new BaseResponse();
        User user = getUser();
        String username = getPara("username");
        if (StringUtils.isNotEmpty(username)) {
            user.set(USERNAME, username);
            flag = true;
        }
        if (flag) {
            boolean update = user.update();
            renderJson(response.setCode(update ? Code.SUCCESS : Code.FAIL).setMessage(update ? "update success" : "update failed"));
        } else {
            renderArgumentError("must set profile");
        }
    }

    /**
     * 修改密码
     */
    public void password(){
    	//根据用户id，查出这个用户的密码，再跟传递的旧密码对比，一样就更新，否则提示旧密码错误
    	String oldPwd = getPara("oldPwd");
    	String newPwd = getPara("newPwd");
    	if(!notNull(Require.me()
    			.put(oldPwd, "old password can not be null")
    			.put(newPwd, "new password can not be null"))){
    		return;
    	}
    	//用户真实的密码
        User nowUser = getUser();
    	if(StringUtils.encodePassword(nowUser.getStr("salt")+oldPwd, "md5").equalsIgnoreCase(nowUser.getStr(PASSWORD))){
    		boolean flag = nowUser.set(User.PASSWORD, StringUtils.encodePassword(nowUser.getStr("salt")+newPwd, "md5")).update();
            renderJson(new BaseResponse(flag?Code.SUCCESS:Code.FAIL, flag?"success":"failed"));
    	}else{
            renderJson(new BaseResponse(Code.FAIL, "oldPwd is invalid"));
    	}
    }
    
    /**
     * 修改头像接口
     * /api/account/avatar
     */
    public void avatar() {
        if (!"put".equalsIgnoreCase(getRequest().getMethod())) {
            renderJson(new BaseResponse(Code.NOT_FOUND));
            return;
        }
    	String avatar=getPara("avatar");
    	if(!notNull(Require.me()
    			.put(avatar, "avatar url can not be null"))){
    		return;
    	}
    }
}

