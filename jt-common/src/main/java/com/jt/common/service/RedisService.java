package com.jt.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService {

	//有的工程需要，有的工程不需要。设置required=false，有就注入，没有就不注入。
    @Autowired(required = false)
    //private ShardedJedisPool jedisPool;
    private JedisSentinelPool sentinelPool;

    /*public void set(String key,String value){
    	ShardedJedis jedis=jedisPool.getResource();
    	jedis.set(key, value);
    	jedisPool.returnResource(jedis);
    }
    
    public void set(String key,int seconds,String value){
    	ShardedJedis jedis=jedisPool.getResource();
    	jedis.setex(key, seconds,value);
    	jedisPool.returnResource(jedis);
    }
    public String get(String key){
    	ShardedJedis jedis=jedisPool.getResource();
    	String result=jedis.get(key);
    	jedisPool.returnResource(jedis);
    	return result;
    }*/
    
    public void set(String key,String value){
		Jedis jedis = sentinelPool.getResource();
		jedis.set(key, value);
		sentinelPool.returnResource(jedis);
	}
	
	public String get(String key){
		Jedis jedis = sentinelPool.getResource();
		String result = jedis.get(key);
		sentinelPool.returnResource(jedis);
		return result;
	}
}
