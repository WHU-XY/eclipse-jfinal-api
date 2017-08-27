package com.whu.jFinal.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.whu.jFinal.bean.Code;
import com.whu.jFinal.common.token.TokenManager;
import com.whu.jFinal.model.User;
import com.whu.jFinal.response.BaseResponse;
import com.whu.jFinal.utils.StringUtils;

/**
 * Token拦截器
 * @author malongbo
 * @date 15-1-18
 * @package com.pet.project.interceptor
 */
public class TokenInterceptor implements Interceptor {
    public void intercept(Invocation ai) {
        Controller controller = ai.getController();
        String token = controller.getPara("token");
        if (StringUtils.isEmpty(token)) {
            controller.renderJson(new BaseResponse(Code.ARGUMENT_ERROR, "token can not be null"));
            return;
        }

        User user = TokenManager.getMe().validate(token);
        if (user == null) {
            controller.renderJson(new BaseResponse(Code.TOKEN_INVALID, "token is invalid"));
            return;
        }
        
        controller.setAttr("user", user);
        ai.invoke();
    }
}
