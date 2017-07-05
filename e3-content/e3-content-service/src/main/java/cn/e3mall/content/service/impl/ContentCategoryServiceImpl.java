package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<TreeNode> getContentCategoryList(long parnetId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parnetId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<TreeNode> resultList = new ArrayList<TreeNode>();
		for (TbContentCategory contentCategory : list) {
			TreeNode node = new TreeNode();
			node.setId(contentCategory.getId());
			node.setText(contentCategory.getName());
			node.setState(contentCategory.getIsParent()?"closed":"open");
			resultList.add(node);
		}
		return resultList;
	}

	@Override
	public E3Result addContentCategory(long parentId, String name) {
		TbContentCategory category = new TbContentCategory();
		category.setParentId(parentId);
		category.setName(name);
		category.setCreated(new Date());
		category.setUpdated(new Date());
		category.setSortOrder(1);
		category.setIsParent(false);
		category.setStatus(1);
		contentCategoryMapper.insert(category);
		TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parentNode.getIsParent()){
			parentNode.setIsParent(true);
			parentNode.setUpdated(new Date());
			contentCategoryMapper.updateByPrimaryKey(parentNode);
		}
		return E3Result.ok(category);
	}
	//根据id删除节点数据
	@Override
	public E3Result deleteContentCategory(long id){
		//根据id查询节点数据
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		//判断是否为父节点
		if (!contentCategory.getIsParent()) {
			//不是父节点，执行删除操作
			contentCategoryMapper.deleteByPrimaryKey(id);
			//查询该父节点数据的子节点数据数量
			TbContentCategoryExample example = new TbContentCategoryExample();
			Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo(contentCategory.getParentId());
			List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
			int size = list.size();
			//判断该节点的父节点下还有没有节点
			if(0==size){
				//没有节点,把其变为子节点
				TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
				parentNode.setIsParent(false);
				//更新父节点
				contentCategoryMapper.updateByPrimaryKey(parentNode);
			}
			//返回数据，提示用户已删除成功
			return E3Result.ok();
		}
		//返回数据，提示用户删除失败
		/*return E3Result.build(404, "包含子节点对象，删除失败！！！");*/
		return E3Result.ok();
	}

}
