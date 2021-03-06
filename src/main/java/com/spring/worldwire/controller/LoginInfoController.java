package com.spring.worldwire.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spring.worldwire.model.LoginInfo;
import com.spring.worldwire.query.LoginInfoQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.worldwire.service.LoginInfoService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
public class LoginInfoController {
	
	@Autowired
	private LoginInfoService loginInfoService;
	
	@RequestMapping("/registerByMail")
	@ResponseBody
	public Map<String,Object> registerByEamil(String userName, String password,String email){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			LoginInfo info = new LoginInfo();
			info.setUserName(userName);
			info.setPassword(password);
			info.setEmail(email);
			Integer result = loginInfoService.registerByMail(info);
			map.put("data", result);
			map.put("msg", "success");
			map.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "error");
			map.put("status", -1);
		}
		return map;
	}

	@RequestMapping("/loginByMail")
	@ResponseBody
	public Map<String,Object> loginByMail(String email, String password, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			LoginInfoQuery query = new LoginInfoQuery();
			query.setEmail(email);
			query.setPassword(password);
			List<LoginInfo> list = loginInfoService.selectByQuery(query);
			if(CollectionUtils.isEmpty(list)){
				map.put("data", null);
				map.put("msg", "null result");
				map.put("status", -2);
				return map;
			}
			Cookie cookie = new Cookie("loginKey",list.get(0).getUserName());
			response.addCookie(cookie);

			map.put("data", list.get(0));
			map.put("msg", "success");
			map.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "error");
			map.put("status", -1);
		}
		return map;
	}

}
