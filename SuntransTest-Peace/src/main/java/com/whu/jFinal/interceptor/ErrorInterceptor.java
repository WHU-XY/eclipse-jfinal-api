package com.whu.jFinal.interceptor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.whu.jFinal.bean.Code;
import com.whu.jFinal.interceptor.ErrorInterceptor;
import com.whu.jFinal.response.BaseResponse;


public class ErrorInterceptor implements Interceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(ErrorInterceptor.class);

	public void intercept(Invocation arg0) {
		// TODO Auto-generated method stub
		try
        {
			arg0.invoke();
        } 
		catch (Exception e)
		{
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            arg0.getController().renderJson(new BaseResponse(Code.ERROR, "服务器出错"));
        }
	}


}
