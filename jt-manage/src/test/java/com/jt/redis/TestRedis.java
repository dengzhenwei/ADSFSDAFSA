//package com.jt.redis;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.junit.Test;
//
//import com.mysql.fabric.xmlrpc.base.Array;
//
//import redis.clients.jedis.HostAndPort;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisCluster;
//import redis.clients.jedis.JedisPoolConfig;
//import redis.clients.jedis.JedisSentinelPool;
//import redis.clients.jedis.JedisShardInfo;
//import redis.clients.jedis.ShardedJedis;
//import redis.clients.jedis.ShardedJedisPool;
//
//public class TestRedis {
//
//	/**
//	 * 连接单台redis
//	 * 参数介绍：
//	 * redisIP地址
//	 * redis：6379
//	 * */
//	@Test
//	public void test01(){
//		Jedis jedis=new Jedis("192.168.220.136", 6379);
//		jedis.set("redis","redis入门案例");
//		System.out.println("获取redis中的数据:"+jedis.get("redis"));
//		
//		jedis.setex("1804", 100, "666");
//		
//	}
//	
//	@Test
//	public void testShard(){
//		//定义池的配置文件
//		JedisPoolConfig poolConfig=new JedisPoolConfig();
//		poolConfig.setMaxTotal(1000);
//		poolConfig.setMaxIdle(100);
//		
//		//准备分片集合对象
//		List<JedisShardInfo>shards=new ArrayList<>();
//		shards.add(new JedisShardInfo("192.168.220.136",6379));
//		shards.add(new JedisShardInfo("192.168.220.136",6380));
//		shards.add(new JedisShardInfo("192.168.220.136",6381));
//		ShardedJedisPool jedisPool=new ShardedJedisPool(poolConfig, shards);
//		ShardedJedis shardedJedis=jedisPool.getResource();
//		shardedJedis.set("1804","redis学习A");
//		System.out.println(shardedJedis.get("1804"));
//	}
//	
//	@Test
//	public void test03(){
//		HostAndPort hostAndPort=new HostAndPort("192.168.220.136",26379);
//		Set<String>sentinels=new HashSet<>();
//		sentinels.add("192.168.220.136:26379");
//		sentinels.add("192.168.220.136:26380");
//		sentinels.add("192.168.220.136:26381");
//		JedisSentinelPool sentinelPool=new JedisSentinelPool("mymaster",sentinels);
//		Jedis jedis=sentinelPool.getResource();
//		jedis.set("tom", "tomccc");
//		System.out.println(jedis.get("tom"));
//	}
//	
//	@Test
//	public void test04(){
//		JedisPoolConfig poolConfig=new JedisPoolConfig();
//		poolConfig.setMaxTotal(1000);
//		Set<HostAndPort>nodes=new HashSet<>();
//		nodes.add(new HostAndPort("192.168.220.136",7000));
//		nodes.add(new HostAndPort("192.168.220.136",7001));
//		nodes.add(new HostAndPort("192.168.220.136",7002));
//		nodes.add(new HostAndPort("192.168.220.136",7003));
//		nodes.add(new HostAndPort("192.168.220.136",7004));
//		nodes.add(new HostAndPort("192.168.220.136",7005));
//		nodes.add(new HostAndPort("192.168.220.136",7006));
//		nodes.add(new HostAndPort("192.168.220.136",7007));
//		nodes.add(new HostAndPort("192.168.220.136",7008));
//		
//		JedisCluster cluster=new JedisCluster(nodes,poolConfig);
//		cluster.set("1804","集群搭建成功");
//		System.out.println(cluster.get("1804"));
//		
//	}
//	
//}
