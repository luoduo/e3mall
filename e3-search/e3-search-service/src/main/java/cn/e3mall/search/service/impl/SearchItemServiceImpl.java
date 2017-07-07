package cn.e3mall.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.SearchItemMapper;
import cn.e3mall.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;
	/**
	 * 把数据库中Item表中的数据查出来（用SearchItem来接收）放入solr索引库中
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	@Override
	public E3Result importItem() throws Exception {
		//从数据库中查询商品列表
		List<SearchItem> itemList = searchItemMapper.getItemList();
		//遍历列表插入到solr索引库
		for (SearchItem searchItem : itemList) {
			//创建一个solrserver对象
			//创建一个SolrInputDocument对象
			SolrInputDocument document = new SolrInputDocument();
			//向document中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			//把文档对象存入索引库
			solrServer.add(document);
		}
		//提交
		solrServer.commit();
		//返回成功
		return E3Result.ok();
	}

}
