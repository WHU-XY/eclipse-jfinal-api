package com.whu.suntrans.handler;

import com.jfinal.handler.Handler;
import com.whu.suntrans.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ContextHandler extends Handler {
    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        Context.me().setRequest(request);
        this.nextHandler.handle(target, request, response, isHandled);
    }
}
