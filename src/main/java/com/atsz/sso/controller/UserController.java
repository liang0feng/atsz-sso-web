package com.atsz.sso.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atsz.manager.pojo.User;
import com.atsz.sso.service.UserService;
import com.atsz.sso.utils.APIReturnMaps;

@Controller
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private UserService userService;

	/**
	 * 验证是否可用 
	 * GET
	 * http://sso.atguigu.com/user/check/{param}/{type}
	 * 可选参数1、2、3分别代表username、phone、email
	 */
	@RequestMapping(value="check/{param}/{type}",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Boolean> check(@PathVariable("param") String param,
			@PathVariable("type") String type){
		
		
		try {
			boolean isValid = userService.check(param,type);
			return ResponseEntity.status(HttpStatus.OK).body(isValid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
	}
	
	
	/**
	 * 用户注册
	 * POST
	 * http://sso.atguigu.com/user/register
	 */
	@RequestMapping(value="register",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<HashMap<String, Object>> check(User user){
		
		
		try {
			boolean isOK = userService.register(user);
			if (!isOK) {
				//注册失败. 请校验数据后请再提交数据.
				return ResponseEntity.status(HttpStatus.OK).body(APIReturnMaps.map("400", "注册失败. 请校验数据后请再提交数据.", null));
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(APIReturnMaps.map("200", "OK", user.getUsername()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIReturnMaps.map("500", "注册失败. 网络连接错误.", null));
	}
	
	/**
	 * 用户名密码登录
	 * POST
	 * http://sso.atguigu.com/user/login
	 * username //用户名
	 * password //密码
	 */
	@RequestMapping(value="login",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<HashMap<String, Object>> login(User user){
		try {
			String ticket = userService.login(user);
			if (ticket == null) {
				return ResponseEntity.status(HttpStatus.OK).body(APIReturnMaps.map("200", "用户名或密码错误！", ticket));
			}
			return ResponseEntity.status(HttpStatus.OK).body(APIReturnMaps.map("200", "OK", ticket));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	/**
	 * ticket登录
	 * GET
	 * http://sso.atguigu.com/user/{ticket}
	 * ticket //用户登录凭证
	 * 
	 */
	@RequestMapping(value="{ticket}",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<HashMap<String,Object>> ticketLog(@PathVariable("ticket") String ticket){
		try {
			User user = userService.ticketLog(ticket);
			
			if (user == null) {
				//ticket不存在
				HashMap<String, Object> hashMap = APIReturnMaps.map("200", "ticket不存在,请重新登录！", user);
				return ResponseEntity.status(HttpStatus.OK).body(hashMap);
			}
			HashMap<String, Object> hashMap = APIReturnMaps.map("200", "OK", user);
			return ResponseEntity.status(HttpStatus.OK).body(hashMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIReturnMaps.map("500", "网络连接错误！", null));
	}
	
	
	
	
	
	
	
	
	
}
