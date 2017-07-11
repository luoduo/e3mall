package cn.e3mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	/**
	 * 根据商品id查询商品详细信息
	 * @param itemId
	 * @param model
	 * @return
	 */
	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable Long itemId,Model model){
		TbItem tbItem = itemService.getItemById(itemId);
		//转换成Item对象
		Item item = new Item(tbItem);
		//根据商品id查询商品描述
		TbItemDesc itemDesc = itemService.getItemDescById(itemId);
		//传递给jsp
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		
		return "item";
	}
}
