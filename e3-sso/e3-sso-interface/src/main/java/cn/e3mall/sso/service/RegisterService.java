package cn.e3mall.sso.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbUser;

public interface RegisterService {
	E3Result registerDataCheck(String checkData,int type);
	E3Result userRegister(TbUser user);

}
