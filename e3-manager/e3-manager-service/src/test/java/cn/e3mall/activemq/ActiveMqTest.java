package cn.e3mall.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class ActiveMqTest {
	/*
	 * 点到点形式发送消息
	 */
	@Test
	public void testQueueProducer() throws Exception{
		//1.创建一个连接工厂对象,需要指定服务的ip及端口 tcp是规定的
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.136.128:61616");
		//2.使用工厂对象创建一个connection对象
		Connection connection = connectionFactory.createConnection();
		//3.开启连接,调用connection对象的start方法
		connection.start();
		//4.创建一个session对象 第一个参数:是否开启事务 ,如果开启事务,第二个参数无意义,一般不开启事务
		//第二个参数:应答模式.一般自动应答或者手动应答.一般是自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.使用session对象创建一个Destination对象,两种形式queue,topic
		Queue queue = session.createQueue("test-queue");
		//6.使用session对象创建一个Producer对象
		MessageProducer producer = session.createProducer(queue);
		//7.创建一个消息对象.可以使用TextMessage
//		TextMessage textMessage = new ActiveMQTextMessage();
//		textMessage.setText("hello");
		TextMessage textMessage = session.createTextMessage(" activemq");
		//8发送消息
		producer.send(textMessage);
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	@Test
	public void testQueueConsumer() throws Exception{
		//创建一个ConnectionFactory对象连接MO服务区
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.136.128:61616");
		//创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//使用Connection对象创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建一个Destination对象,queue对象
		Queue queue = session.createQueue("spring-queue");
		//使用session对象创建一个消费者对象
		MessageConsumer consumer = session.createConsumer(queue);
		//接受消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//打印结果
				TextMessage textMessage = (TextMessage)message;
				try {
					String  text  = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
				
			}
		});
		//等待接受消息,等待敲击键盘
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	/*
	 * 群发,发完消息,如果没有消费者接受,消息就丢失了,
	 */
	@Test
	public void testTopicProducer() throws Exception{
		//1.创建一个连接工厂对象,需要指定服务的ip及端口 tcp是规定的
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.136.128:61616");
		//2.使用工厂对象创建一个connection对象
		Connection connection = connectionFactory.createConnection();
		//3.开启连接,调用connection对象的start方法
		connection.start();
		//4.创建一个session对象 第一个参数:是否开启事务 ,如果开启事务,第二个参数无意义,一般不开启事务
		//第二个参数:应答模式.一般自动应答或者手动应答.一般是自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.使用session对象创建一个Destination对象,两种形式queue,topic
		Topic topic = session.createTopic("test-topic");
		//6.使用session对象创建一个Producer对象
		MessageProducer producer = session.createProducer(topic);
		//7.创建一个消息对象.可以使用TextMessage
//		TextMessage textMessage = new ActiveMQTextMessage();
//		textMessage.setText("heello");
		TextMessage textMessage = session.createTextMessage("topic activemq");
		//8发送消息
		producer.send(textMessage);
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	@Test
	public void testTopicConsumer() throws Exception{
		//创建一个ConnectionFactory对象连接MO服务区
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.136.128:61616");
		//创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//使用Connection对象创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建一个Destination对象,queue对象
		Topic topic = session.createTopic("test-topic");
		//使用session对象创建一个消费者对象
		MessageConsumer consumer = session.createConsumer(topic);
		//接受消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//打印结果
				TextMessage textMessage = (TextMessage)message;
				try {
					String  text  = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
				
			}
		});
		//等待接受消息,等待敲击键盘
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
