package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.sso.service.LoginService;

@Controller
public class UserLoginController {
	
	@Autowired
	private LoginService loginService;
	
	/**
	 * 用户登陆页面展示
	 * 请求：/page/login
	 * 响应：login的静态页面
	 */
	@RequestMapping("/page/login")
	public String showPageLogin(){
		return "login";
	}
	
	/**
	 * 用户登陆
	 * 请求：/user/login  请求类型：POST
	 * 参数：username/ password
	 * 响应：E3Result
	 */
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public E3Result login(String username,String password,
			HttpServletRequest request,HttpServletResponse response){
		E3Result e3Result = loginService.login(username,password);
		//判断是否登录成功
		if(e3Result.getStatus()==200){
			//从e3result中获取token
			String token = (String) e3Result.getData();
			//将token写入cookie中
			CookieUtils.setCookie(request, response, "token", token);
		}
		return e3Result;
	}
	
	/**	
	 * 首页显示用户名
	 * url：/user/token/{token}
	 * 参数：token
	 * 返回值：E3Result（json）
	 */
	/*@RequestMapping(value="/user/token/{token}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUserByToken(@PathVariable String token, String callback) {
		E3Result e3Result = loginService.getUserByToken(token);
		//判断是否为jsonp请求
		if (StringUtils.isNotBlank(callback)) {
			return callback + "(" + JsonUtils.objectToJson(e3Result) + ");";
		}
		return JsonUtils.objectToJson(e3Result);
	}*/
	//支持jsonp的第二种方法
	@RequestMapping(value="/user/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token, String callback) {
		E3Result e3Result = loginService.getUserByToken(token);
		//判断是否为jsonp请求
		if (StringUtils.isNotBlank(callback)) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(e3Result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return e3Result;
	}
	

}
