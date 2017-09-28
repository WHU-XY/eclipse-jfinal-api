package org.zhangfc.demo4ssh.mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zhangfc.demo4ssh.domain.User;
import org.zhangfc.demo4ssh.service.UserService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("")
	public String home(){
		return "index";
	}
	
	@RequestMapping("/json")
	@ResponseBody
	public List<User> json(){
		return userService.getAllUsers();
	}
	
	@RequestMapping("/admin")
	@ResponseBody
	public List<String> admin(){
		return Arrays.asList("zhangsan", "lisi", "wangwu");
	}
}
