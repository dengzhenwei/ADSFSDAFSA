package com.jt.web.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.web.pojo.Item;
import com.jt.web.service.ItemService;

@Controller
public class SearchController {
	@Autowired
	private ItemService itemService;
	
	//实现请求拦截
		@RequestMapping("/search")
		public String search(String q,Model model){
			String key = null;
			try {
				//get请求手动编解码
				key = new String(q.getBytes("ISO-8859-1"),"UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			//查询商品集合
			List<Item>  itemList = itemService.findKey(key);
			//添加关键字
			model.addAttribute("query",key);
			model.addAttribute("itemList", itemList);
			return "search";
		}
}
