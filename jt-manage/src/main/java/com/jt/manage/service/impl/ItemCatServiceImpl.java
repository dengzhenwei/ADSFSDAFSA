package com.jt.manage.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.RedisService;
import com.jt.common.vo.ItemCatData;
import com.jt.common.vo.ItemCatResult;
import com.jt.manage.mapper.ItemCatMapper;
import com.jt.manage.pojo.ItemCat;
import com.jt.manage.service.ItemCatService;
import com.jt.manage.vo.EasyUITree;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
@Service
public class ItemCatServiceImpl implements ItemCatService{
	@Autowired
	private ItemCatMapper itemCatMapper;
	@Autowired
	//private Jedis jedis;
	//private RedisService redisService;
	private JedisCluster jedisCluster;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public List<EasyUITree> findItemCatList(Long parentId) {
		ItemCat itemCat = new ItemCat();
		itemCat.setParentId(parentId);
		List<ItemCat> itemCatList = itemCatMapper.select(itemCat);
		List<EasyUITree>	treeList = new ArrayList<EasyUITree>();
		for(ItemCat itemCatTemp:itemCatList){
			EasyUITree easyUITree = new EasyUITree();
			easyUITree.setId(itemCatTemp.getId());
			easyUITree.setText(itemCatTemp.getName());
				
			String state = itemCatTemp.getIsParent()?"closed":"open";
			easyUITree.setState(state);
			treeList.add(easyUITree);
		}
		
		return treeList;
	}

	@Override
	public List<EasyUITree> findItemCatCache(Long parentId) {
		List<EasyUITree> treeList = null;
		String key = "ITEM_CAT_"+parentId;
		String jsonData = jedisCluster.get(key);
		try{		if(StringUtils.isEmpty(jsonData)){
			//缓存中没有数据，需要查询数据库
			treeList = findItemCatList(parentId);
			String json = 
			objectMapper.writeValueAsString(treeList);
			jedisCluster.set(key, json); 
			return treeList;
			}else{
			EasyUITree[] trees = 	objectMapper.readValue(jsonData, EasyUITree[].class);
			treeList = Arrays.asList(trees);
		}
	}catch (Exception e){
			e.printStackTrace();
		}
		return treeList;
	}
	/*
	 思路：
	 parent_id=0查询全部的一级商品分类信息
	 循环遍历一级商品分类信息，parent_id=一级id查询二级商品分类信息
	 循环遍历二级商品分类信息，parent_id=二级id查询三级商品分类信息
	 */
	@Override
	public ItemCatResult findItemCatAll() {
		ItemCat itemCatTemp = new ItemCat();
		itemCatTemp.setStatus(1);
		List<ItemCat> itemCatListTemp = itemCatMapper.select(itemCatTemp);
		Map<Long, List<ItemCat>> map = new HashMap<>();
		//循环遍历数据，实现Map集合封装
		for (ItemCat itemCat : itemCatListTemp) {
			if(map.containsKey(itemCat.getParentId())){
				//parentId已经存在，对集合做追加操作
				map.get(itemCat.getParentId()).add(itemCat);
			}else{
				//父级不存在，当前元素是第一个
				List<ItemCat> childItemCatList = new ArrayList<>();
				childItemCatList.add(itemCat);
				map.put(itemCat.getParentId(), childItemCatList);
			}
		}
		//实现三级商品分类信息回写
		ItemCatResult itemCatResult = new ItemCatResult();
		
		//获取一级商品信息
		List<ItemCatData> itemCatDateList1 = new ArrayList<>();
		//获取一级商品分类数据
		for (ItemCat itemCat1 : map.get(0L)) {
			ItemCatData itemCatDate1 = new ItemCatData();
			
			itemCatDate1.setUrl("/products/"+itemCat1.getId()+".html");
			itemCatDate1.setName("<a href='"+itemCatDate1.getUrl()+"'>"+itemCat1.getName()+"</a>");
			itemCatDateList1.add(itemCatDate1);
			//准备二级商品分类信息
			List<ItemCatData>itemCatDataList2=new ArrayList<>();
			for(ItemCat itemCat2:map.get(itemCat1.getId())){
				ItemCatData itemCatData2=new ItemCatData();
				itemCatData2.setUrl("/products/"+itemCat2.getId()+".html");
				itemCatData2.setName(itemCat2.getName());
				//获取三级商品分类信息
				List<String>itemCatData3=new ArrayList<>();
				for(ItemCat itemCat3:map.get(itemCat2.getId())){
					itemCatData3.add("/products/"+itemCat3.getId()+"|"+itemCat3.getName());
				}
				itemCatData2.setItems(itemCatData3);
				itemCatDataList2.add(itemCatData2);
				
			}
			itemCatDate1.setItems(itemCatDataList2);
			
			
			
			//控制一级菜单显示长度
			if(itemCatDateList1.size()>13){
				break;
			}
		}
		
		//封装一级商品分类信息
		itemCatResult.setItemCats(itemCatDateList1);
		return itemCatResult;
	}

	/**
	 * 1.通过redis集群查询缓存
	 * 2.如果缓存中有数据。需要将json穿转化java对象
	 * 3.如果缓存中没有数据，则先查询数据库，之后将java对象转化为json串，之后保存到redis中，之后数据返回
	 */
	public ItemCatResult findItemCatAllCache() {
		ItemCatResult itemCatResult = null;
		String key = "ITEM_CAT_ALL";
		
		String result = jedisCluster.get(key);
		try {
			if(StringUtils.isEmpty(result)){
				//查询数据库
				itemCatResult = findItemCatAll();
				String jsonData = 
						objectMapper.writeValueAsString(itemCatResult);
				jedisCluster.set(key, jsonData);
				System.out.println("第一次执行");
			}else{
				itemCatResult = 
				objectMapper.readValue(result, ItemCatResult.class);
				System.out.println("查询缓存");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return itemCatResult;
}
}

