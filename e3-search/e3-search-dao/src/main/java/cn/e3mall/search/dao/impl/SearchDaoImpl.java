package cn.e3mall.search.dao.impl;


import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;

@Repository
public class SearchDaoImpl implements SearchDao {

	@Autowired
	private SolrServer solrServer;
	
	/**
	 * 根据条件，查询索引库中的数据，
	 * 并返回查询结果对象SearchResult
	 */
	@Override
	public SearchResult search(SolrQuery query) throws Exception {
		//根据查询条件查询索引库
		
		QueryResponse response = solrServer.query(query);
		//去查询结果总数
		SolrDocumentList solrDocumentList = response.getResults();
		long numFound = solrDocumentList.getNumFound();
		//取商品列表
		List<SearchItem> itemList = new ArrayList<SearchItem>();
		for (SolrDocument solrDocument : solrDocumentList) {
			SearchItem searchItem = new SearchItem();
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setTitle((String) solrDocument.get("item_title"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			searchItem.setPrice((long) solrDocument.get("item_price"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			itemList.add(searchItem);
		}
		SearchResult result = new SearchResult();
		result.setList(itemList);
		result.setPageCount(numFound);
		
		return result;
	}

}
