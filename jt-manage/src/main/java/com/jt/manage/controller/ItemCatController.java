package com.jt.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.manage.service.ItemCatService;
import com.jt.manage.vo.EasyUITree;
/**
 * defaultValue="0",  默认值
 * required=true, 默认关闭节点
 * value="id"  代表接收的参数
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/item/cat")
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/list")
	@ResponseBody
	public List<EasyUITree> findItemCatList(@RequestParam(defaultValue="0",required=true,value="id") Long parentId){
		//查询一级商品分类目录
		//List<EasyUITree>treeList=itemCatService.findItemCatList(parentId);
		List<EasyUITree>treeList=itemCatService.findItemCatCache(parentId);
		return treeList;
	}
}
