package com.jt.manage.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/query")
	@ResponseBody
	public EasyUIResult findItemByPage(Integer page,Integer rows){
		
		return itemService.findItemByPage(page,rows);
	}
	@RequestMapping(value="/cat/queryItemName",produces="html/text;charset=utf-8")
	@ResponseBody
	public String findItemCatNameById(Long itemId){
		return itemService.findItemCatNameById(itemId);
	}
	//实现商品新增
	@RequestMapping("/save")
	@ResponseBody
	public SysResult saveItem(Item item,String desc){
		try {
			itemService.saveItem(item,desc);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"商品新增失败");
	}
	
	private static final Logger logger=Logger.getLogger(ItemController.class);
	@RequestMapping("/update")
	@ResponseBody
	public SysResult updateItem(Item item,String desc){
		try {
			itemService.updateItem(item,desc);
			logger.info("!!!!!!!记录状态");
			return SysResult.oK();
		} catch (Exception e) {
			logger.error("~~~~~~~~"+e.getMessage());
			e.printStackTrace();
		}
		return SysResult.build(201,"修改失败");
	}
	@RequestMapping("/delete")
	@ResponseBody
	public SysResult deleteItem(Long[] ids){
		try {
			itemService.deleteItem(ids);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"删除失败");
	}
	
	@RequestMapping("/instock")
	@ResponseBody
	public SysResult instock(Long[] ids){
		try {
			int status=2;
			itemService.updateState(ids,status);
			return SysResult.oK();
		} catch (Exception e) {
			
		}
		return SysResult.build(201,"下架失败");
	}
	@RequestMapping("/reshelf")
	@ResponseBody
	public SysResult reshelf(Long[] ids){
		try {
			int status=1;
			itemService.updateState(ids,status);
			return SysResult.oK();
		} catch (Exception e) {
			
		}
		return SysResult.build(201,"上架失败");
	}
	
	@RequestMapping("/query/item/desc/{itemId}")
	@ResponseBody
	public SysResult findItemDescById(@PathVariable Long itemId){
		try {
			ItemDesc itemDesc=itemService.findItemDescById(itemId);
			return SysResult.oK(itemDesc);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return SysResult.build(201, "商品详情查询失败");
	}
	
}
