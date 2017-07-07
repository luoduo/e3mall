package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

@Controller
@RequestMapping("/content")
public class ContentController {

	@Autowired
	private ContentService contentServcie;

	/**
	 * 请求：/content/query/list 
	 * 参数：categoryId=91&page=1&rows=20 
	 * 响应：json,DataGridResult
	 * 
	 */
	@RequestMapping("/query/list")
	@ResponseBody
	public DataGridResult findContentList(long categoryId,int page,int rows) {
		DataGridResult result = contentServcie.findContentList(categoryId,page,rows);
		return result;
	}
	
	/**
	 * 添加content功能
	 * 请求：http://localhost:8084/content/save
	 * 参数： TbContent
	 * 响应：json,List<TbContent>
	 */
	@RequestMapping("/save")
	@ResponseBody
	public E3Result addContent(TbContent content){
		E3Result result = contentServcie.addContent(content); 
		return result;
	}
	
}
