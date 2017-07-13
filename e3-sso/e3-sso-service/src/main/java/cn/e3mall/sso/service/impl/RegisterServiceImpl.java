package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private TbUserMapper userMapper;
	@Override
	public E3Result registerDataCheck(String checkData, int type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//判断用户要校验的数据类型
		//type//1:用户名 2：手机 3：邮箱
		if(type==1){
			criteria.andUsernameEqualTo(checkData);
		}else if(type==2){
			criteria.andPhoneEqualTo(checkData);
		}else if(type==3){
			criteria.andEmailEqualTo(checkData);
		}else{
			return E3Result.build(400, "数据类型错误");
		}
		//执行查询
		List<TbUser> userList = userMapper.selectByExample(example);
		//判断是否查询到结果
		if(userList != null&&userList.size()>0){
			//有查询到结果false
			return E3Result.ok(false);
		}
		//没有结果返回true
		return E3Result.ok(true);
	}
	@Override
	public E3Result userRegister(TbUser user) {
		//数据有效性校验
		//1.用户名进行校验
		if(StringUtils.isBlank(user.getUsername())){
			return E3Result.build(400, "用户名不能为空");
		}else {
			E3Result result = registerDataCheck(user.getUsername(),1);
			if ((boolean)result.getData()==false) {
				return E3Result.build(400, "用户名重复");
			}
		}
		//2。用户密码进行校验
		if(StringUtils.isBlank(user.getPhone())){
			return E3Result.build(400, "密码不能为空");
		}
		//3。用户邮箱进行校验
		if(StringUtils.isNotBlank(user.getEmail())){
			E3Result result = registerDataCheck(user.getEmail(),2);
			if ((boolean)result.getData()==false) {
				return E3Result.build(400, "邮箱重复");
			}
		}
		//4。用户手机号进行校验
		if(StringUtils.isBlank(user.getPhone())){
			return E3Result.build(400, "手机号不能为空");
		}else {
			E3Result result = registerDataCheck(user.getPhone(),2);
			if ((boolean)result.getData()==false) {
				return E3Result.build(400, "手机号重复");
			}
		}
		//补全pojo属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//用户密码进行md5加密
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		//用户信息插入到数据库
		userMapper.insert(user);
		//返回成功
		return E3Result.ok();
	}

}
