package com.whu.jFinal.api;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
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
    @ClearInterceptor
    public void checkUser() {
        String loginName = getPara("loginName");
        if (StringUtils.isEmpty(loginName)) {
            renderArgumentError("loginName can not be null");
            return;
        }
        //检查手机号码是否被注册
        boolean exists = Db.findFirst("SELECT * FROM scp_user WHERE loginName=?", loginName) != null;
        renderJson(new BaseResponse(exists ? Code.SUCCESS:Code.FAIL, exists ? "registered" : "unregistered"));
    }
    
    /**
     * 1. 检查是否被注册*
     * 2. 发送短信验证码*
     */
    @ClearInterceptor
    public void sendCode() {
        String loginName = getPara("loginName");
        if (StringUtils.isEmpty(loginName)) {
            renderArgumentError("loginName can not be null");
            return;
        }

        //检查手机号码有效性
        if (!SMSUtils.isMobileNo(loginName)) {
            renderArgumentError("mobile number is invalid");
            return;
        }

        //检查手机号码是否被注册
        if (Db.findFirst("SELECT * FROM scp_user WHERE loginName=?", loginName) != null) {
            renderJson(new BaseResponse(Code.ACCOUNT_EXISTS,"mobile already registered"));
            return;
        }

        String smsCode = SMSUtils.randomSMSCode(4);
        //发送短信验证码
        if (!SMSUtils.sendCode(loginName, smsCode)) {
            renderFailed("sms send failed");
            return;
        }
        
        //保存验证码数据
        RegisterCode registerCode = new RegisterCode()
                .set(RegisterCode.MOBILE, loginName)
                .set(RegisterCode.CODE, smsCode);

        //保存数据
        if (Db.findFirst("SELECT * FROM scp_register_code WHERE mobile=?", loginName) == null) {
            registerCode.save();
        } else {
            registerCode.update();
        }
        
        renderJson(new BaseResponse("sms sended"));
        
    }
    
	/**
	 * 用户注册
	 */
    @ClearInterceptor()
	public void register(){
		//必填信息
		String loginName = getPara("loginName");//登录帐号
        int code = getParaToInt("code", 0);//手机验证码
        int sex = getParaToInt("sex", 0);//性别
        String password = getPara("password");//密码
		String username = getPara("username");//昵称
    	//头像信息，为空则使用默认头像地址
    	String avatar = getPara("avatar", AppProperty.me().defaultUserAvatar());

        //校验必填项参数
		if(!notNull(Require.me()
                .put(loginName, "username can not be null")
                .put(code, "code can not be null")//根据业务需求决定是否使用此字段
                .put(password, "password can not be null")
                .put(username, "nickName can not be null"))){
			return;
		}

        //检查账户是否已被注册
        if (Db.findFirst("SELECT * FROM scp_user WHERE loginName=?", loginName) != null) {
            renderJson(new BaseResponse(Code.ACCOUNT_EXISTS, "mobile already registered"));
            return;
        }
        
        //检查验证码是否有效, 如果业务不需要，则无需保存此段代码
        if (Db.findFirst("SELECT * FROM scp_register_code WHERE mobile=? AND code = ?", loginName, code) == null) {
            renderJson(new BaseResponse(Code.CODE_ERROR,"code is invalid"));
            return;
        }
        
		//保存用户数据
		String userId = RandomUtils.randomCustomUUID();
		String salt = SMSUtils.randomSMSCode(6);

		new User()
                .set("userId", userId)
                .set(User.LOGIN_NAME, loginName)
		        .set(User.PASSWORD, StringUtils.encodePassword(salt+password, "md5"))
                .set(User.NICK_NAME, username)
		        .set(User.CREATION_DATE, DateUtils.getNowTimeStamp())
		        .set(User.SEX, sex)
                .set(User.AVATAR, avatar)
                .set("salt", salt)
                .save();
		
        //删除验证码记录
        Db.update("DELETE FROM scp_register_code WHERE mobile=? AND code = ?", loginName, code);
        
		//返回数据
		renderJson(new BaseResponse("success"));
	}
	
	
    /**
     * 登录接口
     */
    @ClearInterceptor()
    public void login() {
        String username = getPara("username");
        String password = getPara("password");
        //校验参数, 确保不能为空
        if (!notNull(Require.me()
                .put(username, "username can not be null")
                .put(password, "password can not be null")
        )) {
            return;
        }
        String sql = "SELECT * FROM scp_user WHERE username=?";
        User nowUser = User.user.findFirst(sql, username);
        LoginResponse response = new LoginResponse();
        if (nowUser == null) {
            response.setCode(Code.FAIL).setMessage("username is error");
            renderJson(response);
            return;
        }else if(!StringUtils.encodePassword(nowUser.getStr("salt")+password, "md5").equals(nowUser.getStr("password"))) {
        	 response.setCode(Code.FAIL).setMessage("password is error");
             renderJson(response);
             return;
        }
        Map<String, Object> userInfo = new HashMap<String, Object>(nowUser.getAttrs());
        userInfo.remove(PASSWORD);
        userInfo.remove("salt");
        response.setInfo(userInfo);
        response.setMessage("login success");
        response.setToken(TokenManager.getMe().generateToken(nowUser));
        renderJson(response);
    }

    /**
     * 资料相关的接口
     */
    public void profile() {
        String method = getRequest().getMethod();
        if ("get".equalsIgnoreCase(method)) { //查询资料
            getProfile();
        } else if ("put".equalsIgnoreCase(method)) { //修改资料
            updateProfile();
        } else {
            render404();
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
            user.set(NICK_NAME, username);
            flag = true;
        }

        String email = getPara("email");
        if (StringUtils.isNotEmpty(email)) {
            user.set(EMAIL, email);
            flag = true;
        }
        
        String avatar = getPara("avatar");
        if (StringUtils.isNotEmpty(avatar)) {
            user.set(AVATAR, avatar);
            flag = true;
        }

        //修改性别
        Integer sex = getParaToInt("sex", null);
        if (null != sex) {
            if (!User.checkSex(sex)) {
                renderArgumentError("sex is invalid");
                return;
            }
            user.set(SEX, sex);
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
        if (!"put".equalsIgnoreCase(getRequest().getMethod())) {
            render404();
            return;
        }
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
    	if(StringUtils.encodePassword(oldPwd, "md5").equalsIgnoreCase(nowUser.getStr(PASSWORD))){
    		boolean flag = nowUser.set(User.PASSWORD, StringUtils.encodePassword(newPwd, "md5")).update();
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
    	getUser().set(User.AVATAR, avatar).update();
    	renderSuccess("success");
    }
}

