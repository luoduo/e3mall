package cn.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${session_expire}")
	private Integer sessionExpire;

	@Override
	public E3Result login(String username, String password) {
		// 1.根据用户名去数据库中进行查询用户信息
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> userList = userMapper.selectByExample(example);
		// 判断用户是否存在
		if (!(userList !=null && userList.size()>0)) {
			//用户不存在返回
			// 返回（登录失败）
			return E3Result.build(400, "用户名或密码错误！！");
		}
		// ②用户存在
		// 判断用户密码是否正确
		TbUser user = userList.get(0);
		// ①用户密码不正确
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
			// 返回（登录失败）
			return E3Result.build(400, "用户名或密码错误！！");
		}
		// ②如果密码正确，把用户信息保存到redis中（hash类型），key为tooken
		// 生成token，（防止其他人欺骗系统，造成登陆假象：）
		String token = UUID.randomUUID().toString();
		// 把用户信息中的密码设置为空
		user.setPassword(null);
		// 把用户的登陆信息放入redis中
		jedisClient.hset("user_session:"+token, "user", JsonUtils.objectToJson(user));
		//设置过期时间
		jedisClient.expire("user_session:"+token, sessionExpire);
		//返回token,在表现层将token写入cookie
		return E3Result.ok(token);
	}

	/**
	 * 用户登录后首页展示用户名
	 * 参数：String token
	 * E3Result（json）
	 */
	@Override
	public E3Result getUserByToken(String token) {
		// 2、根据token查询用户信息，查询redis
		String json = jedisClient.hget("user_session:"+token, "user");
		
		// 3、如果没有用户信息，登录过期
		if(StringUtils.isBlank(json)){
			return E3Result.build(400, "请重新登录！！");
		}
		// 4、如果有用户信息，是登录状态返回用户信息
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		
		// 5、重置过期时间。
		jedisClient.expire("user_session:"+token, sessionExpire);
		return E3Result.ok(user);
	}

}
