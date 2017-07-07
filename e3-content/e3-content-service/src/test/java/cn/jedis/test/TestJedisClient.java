package cn.jedis.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.jedis.JedisClient;

public class TestJedisClient {

	@Test
	public void testJedisClient() throws Exception {
		// 初始化Spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-redis.xml");
		// 从容器中获得JedisClient对象
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		jedisClient.set("keys", "100");
		String result = jedisClient.get("keys");
		System.out.println(result);

	}

}
