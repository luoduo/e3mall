package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import javax.swing.text.html.parser.ContentModel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

/**
 * 内容content业务层实现类
 * 
 * @author Administrator
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	@Autowired
	private JedisClient jedisClient;

	/**
	 * 根据categoryId分页查询TbContent ，redis作为缓存，先从redis缓存中查
	 */
	@Override
	public DataGridResult findContentList(long categoryId, int page, int rows) {

		PageHelper.startPage(page, rows);
		// 2）执行查询
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		// 3）取分页结果
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		// 4）返回结果
		DataGridResult result = new DataGridResult();
		result.setRows(list);
		result.setTotal(total);
		return result;
	}

	/**
	 * 添加content
	 */
	@Override
	public E3Result addContent(TbContent content) {
		content.setUpdated(new Date());
		content.setCreated(new Date());
		contentMapper.insert(content);
		// jedis缓存同步
		jedisClient.hdel("content_info", content.getCategoryId().toString());
		return new E3Result().ok();
	}

	/**
	 * 根据分类id查询content
	 */
	@Override
	public List<TbContent> getIndexSliderByCategoryId(Long categoryId) {
		// 查询缓存中的数据
		try {
			String json = jedisClient.hget("content_info", categoryId + "");
			// 如果从缓存中查出数据直接返回
			if (StringUtils.isNotBlank(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 如果查不到，查询数据库
		// 根据分类id查询内容列表
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		// 把结果放入redis缓存中
		try {

			jedisClient.hset("content_info", categoryId + "", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
