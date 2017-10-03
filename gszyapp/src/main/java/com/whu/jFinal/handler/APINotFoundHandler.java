package com.whu.jFinal.handler;

import com.jfinal.core.JFinal;
import com.jfinal.handler.Handler;
import com.jfinal.render.RenderFactory;
import com.whu.jFinal.bean.Code;
import com.whu.jFinal.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * 处理404接口*
 * @author malongbo
 * @date 15-1-18
 * @package com.pet.project
 */
public class APINotFoundHandler extends Handler {
    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        if (!target.startsWith("/api")) {
        	next.handle(target, request, response, isHandled);
            //this.handle(target, request, response, isHandled);
            return;
        }
        
        if (JFinal.me().getAction(target, new String[1]) == null) {
            isHandled[0] = true;
            try {
                request.setCharacterEncoding("utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            BaseResponse myresponse = new BaseResponse();
            myresponse.setCode(Code.NOT_FOUND);
            myresponse.setMessage("resource is not found");
            RenderFactory renderfactory = new RenderFactory();
            renderfactory.getJsonRender(myresponse).setContext(request, response).render();
        } else {
        	next.handle(target, request, response, isHandled);
            //this.handle(target, request, response, isHandled);
        }
    }
}
