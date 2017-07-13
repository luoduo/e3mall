package cn.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.RegisterService;

@Controller
public class UserRegisterController {
	@Autowired 
	private RegisterService registerService;
	
	@RequestMapping("/page/register")
	public String showPageRegister(){
		
		return "register";
	}
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	@RequestMapping("/user/register")
	@ResponseBody
	public E3Result userRegister(TbUser user){
		E3Result e3Result= registerService.userRegister(user);
		return e3Result;
	}
	
	/**
	 * 用户注册时校验用户的输入信息
	 * @param checkData
	 * @param type
	 * @return
	 */
	@RequestMapping("/user/check/{checkData}/{type}")
	@ResponseBody
	public E3Result registerDataCheck(@PathVariable String checkData,@PathVariable Integer type){
		E3Result e3Result = registerService.registerDataCheck(checkData,type);
		return e3Result;
	}

}













