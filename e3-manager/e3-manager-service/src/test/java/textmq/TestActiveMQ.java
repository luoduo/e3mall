package textmq;

import static org.junit.Assert.*;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * activemq发送消息测试
 * @author Administrator
 *
 */
public class TestActiveMQ {
	@Test
	public void testQueueProducer() throws Exception {
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//从容器中获取JMSTemplate对象
		JmsTemplate jmsTemplate = (JmsTemplate) applicationContext.getBean("jmsTemplate");
		//从容器中获得一个Destination对象
		Queue queue  = (Queue) applicationContext.getBean("queueDestination");
		//使用jmsTemplate对象发送消息
		jmsTemplate.send(queue, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage("spring activemq test1");
				return textMessage;
			}
		});
		
	}

}
