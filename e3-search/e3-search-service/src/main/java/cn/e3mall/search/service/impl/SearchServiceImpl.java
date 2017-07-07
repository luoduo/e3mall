package cn.e3mall.search.service.impl;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;

/**
 * 商品检索Service
 * @author Administrator
 *
 */
@Service
public class SearchServiceImpl implements SearchService {

	@Resource
	private SearchDao searchDao;
	
	@Override
	public SearchResult search(String keyword, int page,Integer rows) throws Exception {
		//创建一个SolrQuery的对象
		SolrQuery solrQuery = new SolrQuery();
		//设置查询条件
		solrQuery.setQuery(keyword);
		solrQuery.setStart( (page - 1) * rows);
		solrQuery.setRows(rows);
		solrQuery.set("df", "item_keywords");
		//开启高亮显示
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField(keyword);
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");
		//调用dao执行查询
		SearchResult search = searchDao.search(solrQuery);
		//根据总记录数据计算总页数
		long recordCount = search.getRecordCount();
		long pageCount = recordCount/rows;
		if(recordCount%rows > 0){
			pageCount++;
		}
		search.setPageCount(pageCount);
		return search;
	}

}
