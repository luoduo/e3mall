package cn.e3mall.search.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;
/**
 * 商品检索（从solr索引库中进行检索）表现层SearchController
 * @author Administrator
 *
 */
@Controller
public class SearchController {
	
	@Autowired
	private SearchService searchService ;
	@Value("${search.item.rows}")
	private Integer itemRows;

	@RequestMapping("/search")
	public String search(String keyword,@RequestParam(defaultValue="1")int page,Model model) throws Exception{
		//把keyword转码
		if (StringUtils.isNotBlank(keyword)) {
			keyword = new String(keyword.getBytes("iso8859-1"), "utf-8");
		}
		//直接调用service层
		SearchResult result = searchService.search(keyword,page,itemRows);
		//把数据放入model中
		model.addAttribute("query", keyword);
		model.addAttribute("page", page);
		model.addAttribute("tatalPages", result.getPageCount());
		model.addAttribute("recourdCount", result.getRecordCount());
		model.addAttribute("itemList", result.getList());
		return "search";
	}
}
