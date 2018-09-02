package com.jt.order.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jt.dubbo.pojo.Order;
import com.jt.dubbo.pojo.OrderItem;
import com.jt.dubbo.pojo.OrderShipping;
import com.jt.order.mapper.OrderItemMapper;
import com.jt.order.mapper.OrderMapper;
import com.jt.order.mapper.OrderShippingMapper;

public class RabbitMQOrderService {
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	//实现订单入库操作
	public void saveOrder(Order order){
		String orderId=order.getOrderId();
		Date date=new Date();
		
		//状态:1、未付款 2、已付款3、未发货 4、已发货 5、交易完成
		order.setStatus(1);
		order.setCreated(date);
		order.setUpdated(date);
		order.setOrderId(orderId);
		orderMapper.insert(order);//实现订单入库
		System.out.println("入库成功！");
		
		OrderShipping orderShipping=order.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		orderShippingMapper.insert(orderShipping);
		System.out.println("订单物流入库成功");
		
		List<OrderItem>orderItems=order.getOrderItems();
		for(OrderItem orderItem:orderItems){
			orderItem.setOrderId(orderId);
			orderItem.setCreated(date);
			orderItem.setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		System.out.println("消息队列订单入库成功");
		
	}
}
