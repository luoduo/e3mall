package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.search.service.SearchItemService;

/**
 * 索引库维护Controller
 * @author Administrator
 *
 */
@Controller
public class SolrController {
	
	@Autowired
	private SearchItemService searchItemService;
	
	/**
	 * 索引库维护方法（把数据库中的数据添加（更新）到索引库）
	 * 请求：http://localhost:8084/index/item/import
	 * 参数：
	 * 响应：E3Result (成功/失败)
	 */
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3Result importItem(){
		E3Result result;
		try{
			result = searchItemService.importItem();
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return E3Result.build(500, e.getMessage());
		}
	}

}
