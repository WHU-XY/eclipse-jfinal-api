package com.jade.handler;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

/** 
* 防注入处理器
* @author jiangyf
* @date 2016年1月8日 下午4:33:16  
*/
public class PreventInjectHandler extends Handler{
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        /*if (target.endsWith(".html")) target = target.substring(0, target.length() - 5);*/
        //获得所有请求参数名
        Enumeration<String> params = request.getParameterNames();
        String sql = "";
        while (params.hasMoreElements()) {
            //得到参数名
            String name = params.nextElement().toString();
            //得到参数对应值
            String[] value = request.getParameterValues(name);
            for (int i = 0; i < value.length; i++) {
                sql = sql + value[i];
            }
        }
        //有sql关键字，跳转到error.html
        if (sqlValidate(sql)) {
        	request.setAttribute("sqlInto", "1");
            //String ip = req.getRemoteAddr();
        } else {
        	request.setAttribute("sqlInto", "0");
        }
        nextHandler.handle(target, request, response, isHandled);
    }
	
    /** 
    * SQL关键字的校验 
    * @param str
    * @return boolean
    */
    protected static boolean sqlValidate(String str) {
        str = str.toLowerCase();//统一转为小写
        //过滤掉的sql关键字，可以手动添加 and；未添加，会过滤掉android参数
        String badStr = "'|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|" +
                "char|declare|sitename|net user|xp_cmdshell|;|or|+|like'|exec|execute|insert|create|drop|" +
                "table|from|grant|use|group_concat|column_name|" +
                "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|" +
                "chr|mid|master|truncate|char|declare|or|;|--|+|like|//|/|%|#|<|>";
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return true;
            }
        }
        return false;
    }
}
