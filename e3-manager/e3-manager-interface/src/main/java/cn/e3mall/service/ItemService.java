package cn.e3mall.service;
import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

public interface ItemService {
	
	TbItem getItemById(long id);
	
	DataGridResult getItemList(int page, int rows);
	
	E3Result addItem(TbItem tbItem,String desc);
	
	TbItemDesc getItemDescById(Long itemId);

}
