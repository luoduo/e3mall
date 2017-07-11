package cn.e3mall.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.search.service.impl.SearchItemServiceImpl;

/**
 * listener
 * addItem商品添加监听器，
 * @author Administrator
 */
public class AddItemMessageListener implements MessageListener {

	@Autowired
	private SearchItemServiceImpl searchItemService;
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage =null;
			Long itemId = null;
			System.out.println("_______________________---------------------------------");
			//先睡一会儿，防止没有完成数据库插入就去查询，等待事务提交
			Thread.sleep(1500);
			//取商品id
			if(message instanceof TextMessage){
				textMessage= (TextMessage) message;
				
				//接收消息内容
				itemId=Long.parseLong(textMessage.getText());
			}
			
			//向索引库添加文档
			searchItemService.addDocument(itemId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
