package cn.e3mall.sso.service;

import cn.e3mall.common.pojo.E3Result;

public interface LoginService {
	E3Result login(String username,String password);
	E3Result getUserByToken(String token);
}
