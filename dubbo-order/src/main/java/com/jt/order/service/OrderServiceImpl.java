package com.jt.order.service;

import java.util.Date;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.jt.dubbo.pojo.Order;
import com.jt.dubbo.pojo.OrderItem;
import com.jt.dubbo.pojo.OrderShipping;
import com.jt.dubbo.service.DubboOrderService;
import com.jt.order.mapper.OrderItemMapper;
import com.jt.order.mapper.OrderMapper;
import com.jt.order.mapper.OrderShippingMapper;

public class OrderServiceImpl implements DubboOrderService{

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Override
	public String saveOrder(Order order) {
		String orderId=order.getUserId()+""+System.currentTimeMillis();
		order.setOrderId(orderId);
		//将order信息写入消息队列
		rabbitTemplate.convertAndSend("save.order",order);
		return orderId;
	}
	@Override
	public Order findOrderById(String id) {
		// TODO Auto-generated method stub
		return orderMapper.findOrderById(id);
	}
	
	/**
	 * 要求实现三张表同时入库
	 * 1.先入库order表
	 * 2.入库orderShipping
	 * 3.入库orderItem
	 * 实现订单的异步入库，使用消息队列
	 */
	/*@Override
	public String saveOrder(Order order) {
		Date date=new Date();
		String orderId=order.getUserId()+""+System.currentTimeMillis();
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
		System.out.println("订单入库操作成功");
		return orderId;
	}

	@Override
      public Order findOrderById(String id) {
		
		return orderMapper.findOrderById(id);
	}*/

}
