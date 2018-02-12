package com.taotao.pageHeper;

import com.alibaba.dubbo.rpc.filter.EchoFilter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;

public class TestActiveMq {


    // producer 可以有多个
    @Test
    public void  testQueueProducer() throws Exception {
        // 创建连接对象
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://120.79.130.20:61616");
        // 使用connection对象的start对象
        Connection connection = connectionFactory.createConnection();
        connection.start();
        // 使用connection对象创建一个session对象  false: 不开始事务
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建destination 对象 queue topic 对象
        Queue queue = session.createQueue("test-queue");  // 消息队列名称
        // 使用session对象创建producer对象
        MessageProducer producer = session.createProducer(queue);
        // 创建一个TestMessage 对象
        TextMessage textMessage = session.createTextMessage("hello world2");
        // 发生消息
        producer.send(textMessage);
        // 关闭资源
        producer.close();
        session.close();
        connection.close();
    }

    /**
     * 消费者只有一个
     * @throws Exception
     */
    @Test
    public void testQueueConsumer() throws Exception{
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://120.79.130.20:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("test-queue");
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage)message;
                    try {
                        System.out.println(textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        System.in.read();
        consumer.close();
        session.close();
        connection.close();
    }



    // TopicC
    @Test
    public void testTopicProducer() throws Exception{
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://120.79.130.20:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic("test-topic");

        MessageProducer producer = session.createProducer(topic);
        TextMessage textMessage = session.createTextMessage("hello wrold");
        producer.send(textMessage);
        producer.close();
        session.close();
        connection.close();
    }


    @Test
    public void testTopicConsumser() throws Exception {
        //创建一个连接工厂对象
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://120.79.130.20:61616");
        //使用连接工厂对象创建一个连接
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //使用连接对象创建一个Session对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //使用Session创建一个Destination，Destination应该和消息的发送端一致。
        Topic topic = session.createTopic("test-topic");
        //使用Session创建一个Consumer对象
        MessageConsumer consumer = session.createConsumer(topic);
        //向Consumer对象中设置一个MessageListener对象，用来接收消息
        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                //取消息的内容
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        String text = textMessage.getText();
                        //打印消息内容
                        System.out.println(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        //系统等待接收消息
		/*while(true) {
			Thread.sleep(100);
		}*/
        System.out.println("topic消费者3.。。。");
        System.in.read();
        //关闭资源
        consumer.close();
        session.close();
        connection.close();
    }
}
