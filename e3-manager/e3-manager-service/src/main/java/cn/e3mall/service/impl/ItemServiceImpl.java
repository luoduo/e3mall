package cn.e3mall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.service.ItemService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;

	@Override
	public cn.e3mall.pojo.TbItem getItemById(long id) {
		TbItem item = itemMapper.selectByPrimaryKey(id);
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

}
