package com.mlongbo.jfinal.api;

public class ExampleAPIController extends BaseAPIController {

	public void example() {
		renderArgumentError("Hello World!");
        /*String loginName = getPara("loginName");
        if (StringUtils.isEmpty(loginName)) {
            renderArgumentError("loginName can not be null");
            return;
        }
        //检查手机号码是否被注册
        boolean exists = Db.findFirst("SELECT * FROM t_user WHERE loginName=?", loginName) != null;
        renderJson(new BaseResponse(exists ? Code.SUCCESS:Code.FAIL, exists ? "registered" : "unregistered"));*/
    }
}
