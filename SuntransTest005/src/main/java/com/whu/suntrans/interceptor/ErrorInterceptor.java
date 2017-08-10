package com.whu.suntrans.interceptor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;


import com.whu.suntrans.bean.Code;
import com.whu.suntrans.response.BaseResponse;


public class ErrorInterceptor implements Interceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(ErrorInterceptor.class);

	public void intercept(ActionInvocation arg0) {
		// TODO Auto-generated method stub
		try
        {
			arg0.invoke();
        } 
		catch (Exception e)
		{
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            arg0.getController().renderJson(new BaseResponse(Code.ERROR, "server error"));
        }
	}


}
