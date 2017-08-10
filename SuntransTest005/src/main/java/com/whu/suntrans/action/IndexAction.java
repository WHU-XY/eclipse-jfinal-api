package com.whu.suntrans.action;

import java.util.List;

import com.jfinal.core.Controller;
import com.whu.suntrans.model.BlogInfo;


public class IndexAction extends Controller {
    public void index () {

    	List<BlogInfo> nowBlog = BlogInfo.dao.find("select *from blog");
    	setAttr("blogPage",nowBlog);
        render("list.html");  
        }
}