package com.whu.jFinal.handler;

import com.jfinal.handler.Handler;
import com.whu.jFinal.config.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author malongbo
 */
public class ContextHandler extends Handler {
    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        Context.me().setRequest(request);
        next.handle(target, request, response, isHandled);
    }
}
