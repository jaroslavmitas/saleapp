package cz.jaroslavmitas.testsender;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class SenderMultiple {

	private ConnectionFactory factory = null;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageProducer producer = null;

	public SenderMultiple() {

	}

	public static void main(String[] args) {
		SenderMultiple sender = new SenderMultiple();
		sender.sendMessage();
	}
	
	
	public void sendMessage() {

		try {
			factory = new ActiveMQConnectionFactory(
					ActiveMQConnection.DEFAULT_BROKER_URL);
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("maki");
			producer = session.createProducer(destination);
			TextMessage message = session.createTextMessage();
			message.setText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
							"<sale-record>"+
							"	<sale>"+
							"		<sale-id>dep-web-000006</sale-id>"+
							"		<sale-type>MULTIPLE_SALE</sale-type>"+
							"	</sale>"+
							"	<product>"+
							"		<amount>10</amount>"+
							"		<name>apple</name>"+
							"		<value>0.2</value>"+
							"	</product>"+
							"</sale-record>");
			producer.send(message);
			System.out.println("Sent: " + message.getText());

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}