package com.jt.manage.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.jt.common.vo.EasyUIResult;
import com.jt.manage.mapper.ItemDescMapper;
import com.jt.manage.mapper.ItemMapper;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.service.ItemService;
@Service
public class ItemServiceImpl implements ItemService{

	@Autowired
	private ItemMapper itemMapper;
    @Autowired
	private ItemDescMapper itemDescMapper;
	
	@Override
	public EasyUIResult findItemByPage(Integer page, Integer rows) {
		//int total=itemMapper.findItemCount();
		int total=itemMapper.selectCount(null);
		int start=(page-1)*rows;
		List<Item>itemList=itemMapper.findItemListByPage(start,rows);
		return new EasyUIResult(total, itemList);
	}

/**
 * responseBody解析说明
 * 如果解析的时对象 则默认使用UTF-8编码格式
 * 如果解析的时String，则默认使用IOS-8859-1
 */
	@Override
	public String findItemCatNameById(Long itemId) {
		String name=itemMapper.findItemCatNameById(itemId);
	
		return itemMapper.findItemCatNameById(itemId);
	}

	
	//封装item数据
@Override
public void saveItem(Item item,String desc) {

	item.setStatus(1);//表示正常上架
	item.setCreated(new Date());
	item.setUpdated(item.getCreated());
	itemMapper.insert(item);
	
	ItemDesc itemDesc=new ItemDesc();
	itemDesc.setItemDesc(desc);
	itemDesc.setItemId(item.getId());
	itemDesc.setCreated(item.getCreated());
	itemDesc.setUpdated(item.getCreated());
	itemDescMapper.insert(itemDesc);
}

@Override
public void updateItem(Item item,String desc) {

	item.setUpdated(new Date());
	itemMapper.updateByPrimaryKeySelective(item);
	ItemDesc itemDesc=new ItemDesc();
	itemDesc.setItemDesc(desc);
	itemDesc.setItemId(item.getId());
	itemDesc.setUpdated(item.getCreated());
	itemDescMapper.updateByPrimaryKey(itemDesc);
}

@Override
public void deleteItem(Long[] ids) {

	itemMapper.deleteByIDS(ids);
}

@Override
public void updateState(Long[] ids, int status) {
	itemMapper.updateState(ids,status);
	
}

@Override
public ItemDesc findItemDescById(Long itemId) {
	
	return itemDescMapper.selectByPrimaryKey(itemId);
}

@Override
public Item findItemById(Long itemId) {

	return itemMapper.selectByPrimaryKey(itemId);
}
}
