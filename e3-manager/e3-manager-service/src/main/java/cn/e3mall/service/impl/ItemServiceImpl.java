package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jsmTemplate;
	@Resource
	private Destination topicDestination;
	
	@Autowired
	private JedisClient jedisClient;
	@Value("${item.cache.expire}")
	private Integer itemCacheExpire;

	/**
	 * 根据id查询商品
	 */
	@Override
	public TbItem getItemById(long itemId) {
		// 查询缓存
		try {
			String json = jedisClient.get("ITEM_INFO:" + itemId + ":base");
			if (StringUtils.isNotBlank(json)) {
				// 如果缓存中有数据直接返回
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//redis缓存中没有，查询数据库获取数据
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//把结果添加到缓存中
		try {
			jedisClient.set("ITEM_INFO:" + itemId + ":base",JsonUtils.objectToJson(item));
			//设置过期时间
			jedisClient.expire("ITEM_INFO:" + itemId + ":base", itemCacheExpire);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public DataGridResult getItemList(int page, int rows) {
		// 1）设置分页信息
		PageHelper.startPage(page, rows);
		// 2）执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		// 3）取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		// 4）返回结果
		DataGridResult result = new DataGridResult();
		result.setRows(list);
		result.setTotal(total);
		return result;
	}

	@Override
	public E3Result addItem(TbItem item, String desc) {

		// 生成商品id
		final long itemId = IDUtils.genItemId();
		// 补全TbItem对象属性
		item.setId(itemId);
		// 商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		// 向商品表插入数据
		itemMapper.insert(item);
		// 创建一个TbItemDesc对象
		TbItemDesc itemDesc = new TbItemDesc();
		// 补全TbItemDesc的属性
		itemDesc.setItemId(itemId);
		itemDesc.setCreated(date);
		itemDesc.setUpdated(date);
		itemDesc.setItemDesc(desc);
		// 执行插入
		itemDescMapper.insert(itemDesc);
		// 发送一个商品添加消息
		jsmTemplate.send(topicDestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId + "");
				return textMessage;
			}
		});
		return E3Result.ok();

	}

}
