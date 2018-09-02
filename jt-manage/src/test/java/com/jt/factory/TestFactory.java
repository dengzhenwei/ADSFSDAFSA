package com.jt.factory;

import java.util.Calendar;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestFactory {

	@Test
	public void testFactory(){
		ApplicationContext context=new ClassPathXmlApplicationContext("/spring/factory.xml");
		Calendar calendar1=(Calendar) context.getBean("calendar1");
		Calendar calendar2=(Calendar) context.getBean("calendar2");
		System.out.println(calendar1.getTime());
		System.out.println(calendar2.getTime());
	}
}
