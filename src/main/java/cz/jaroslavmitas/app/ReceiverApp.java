package cz.jaroslavmitas.app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import cz.jaroslavmitas.dto.Adjustments;
import cz.jaroslavmitas.dto.Config;
import cz.jaroslavmitas.dto.ProductSale;
import cz.jaroslavmitas.dto.SaleAppConstants;
import cz.jaroslavmitas.dto.SaleRecord;
import cz.jaroslavmitas.service.IParsingService;
import cz.jaroslavmitas.service.IReportService;
import cz.jaroslavmitas.service.ParsingService;
import cz.jaroslavmitas.service.ReportService;

public class ReceiverApp {
	private ConnectionFactory factory = null;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageConsumer consumer = null;
	private boolean continueReceiving = true;
	private long messageCounter = 0;
	
	private Map<String, ArrayList<ProductSale>> sales = new HashMap<String, ArrayList<ProductSale>>();
	private Map<String, ArrayList<Adjustments>> adjustments = new HashMap<String, ArrayList<Adjustments>>();
	
	IParsingService parsingService = new ParsingService();
	IReportService reportService = new ReportService();
	
	public ReceiverApp() {

	}

	public void receiveMessage() {
		try {
			
			factory = new ActiveMQConnectionFactory(
					ActiveMQConnection.DEFAULT_BROKER_URL);
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("maki");
			consumer = session.createConsumer(destination);
			Message message = null; 
			
			while(continueReceiving) {
				message = consumer.receive();
				messageCounter++;
				
				
				
				if (message!= null && message instanceof TextMessage) {
					TextMessage text = (TextMessage) message;
					
					//parse message
					SaleRecord saleRecord = parseSaleRecord(text.getText());
					
					//process message
					processMessage(saleRecord);
					
				}
				
				if(messageCounter %  Config.SALES_COUNT_REPORT == 0) {
					reportService.printSaleReport(sales);
				}					
				
				
				if(messageCounter == Config.RECEIVER_STOPPING_COUNT) {
					continueReceiving = false;
					reportService.printAdjustmentReport(adjustments);
				}
				
				try {
					Thread.sleep(1000);
				}catch(InterruptedException ie) {
					ie.printStackTrace();
				}
			}
			
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ReceiverApp receiver = new ReceiverApp();
		receiver.receiveMessage();
	}
	
	
	private SaleRecord parseSaleRecord(String xmlMessageContent) {
		SaleRecord saleRecord = parsingService.parseMessage(xmlMessageContent);
		return saleRecord;
	}
	
	private void processMessage(SaleRecord saleRecord) {
		if(saleRecord == null || saleRecord.getSale() == null || saleRecord.getProduct() == null) {
			return;
		}
		
		if(SaleAppConstants.SINGLE_SALE.equals(saleRecord.getSale().getMessageType())) {
			//add one single sale to the sales
			addSingleSale(saleRecord);
		}else if(SaleAppConstants.MULTIPLE_SALE.equals(saleRecord.getSale().getMessageType())) {
			//add multiple sales to the sales
			addMultipleSales(saleRecord);
		}else if (SaleAppConstants.UPDATE_SALE.equals(saleRecord.getSale().getMessageType())) {
			//update sales of product type
			updateSales(saleRecord);	
		} else {
			System.out.println("Error: saleRecord was not processed!, +"+saleRecord);
		}
	}
		
		
		
	private void addSingleSale(SaleRecord saleRecord) {
		ArrayList<ProductSale> productSaleList = sales.get(saleRecord.getProduct().getName());
		
		if(productSaleList == null) {
			productSaleList = new ArrayList<ProductSale>();	
		}
		
		ProductSale productSale = new ProductSale();
		productSale.setAmount(1);
		productSale.setPrice(new BigDecimal(saleRecord.getProduct().getPrice()));
		productSale.setProductName(saleRecord.getProduct().getName());
		productSaleList.add(productSale);
		sales.put(saleRecord.getProduct().getName(), productSaleList);
	}
		
	private void addMultipleSales(SaleRecord saleRecord) {
		ArrayList<ProductSale> productSaleList = sales.get(saleRecord.getProduct().getName());
		
		if(productSaleList == null) {
			productSaleList = new ArrayList<ProductSale>();	
		}
		
		ProductSale productSale = new ProductSale();
		productSale.setAmount(saleRecord.getProduct().getAmount());
		productSale.setPrice(new BigDecimal(saleRecord.getProduct().getPrice()));
		productSale.setProductName(saleRecord.getProduct().getName());
		productSaleList.add(productSale);
		sales.put(saleRecord.getProduct().getName(), productSaleList);
	}
		
		
	private void updateSales(SaleRecord saleRecord) {
		ArrayList<Adjustments> adjustmentsList = adjustments.get(saleRecord.getProduct().getName());
		
		//saving adjustments
		if(adjustmentsList == null) {
			adjustmentsList = new ArrayList<Adjustments>();	
		}
		
		Adjustments adjustment = new Adjustments();
		adjustment.setName(saleRecord.getProduct().getName());
		adjustment.setOperation(saleRecord.getProduct().getOperation());
		adjustment.setValue(new BigDecimal(saleRecord.getProduct().getPrice()));
		adjustmentsList.add(adjustment);
		adjustments.put(saleRecord.getProduct().getName(), adjustmentsList);
		
		//doing adjustments
		ArrayList<ProductSale> productSaleList = sales.get(saleRecord.getProduct().getName());
		
		if(productSaleList == null) {
			return; //no data, no update
		}
		for(ProductSale productSale: productSaleList) {
			if(SaleAppConstants.ADD.equals(saleRecord.getProduct().getOperation())) {
				productSale.setPrice(productSale.getPrice().add(new BigDecimal(saleRecord.getProduct().getPrice())));
			}else if (SaleAppConstants.SUBTRACT.equals(saleRecord.getProduct().getOperation())) {
				productSale.setPrice(productSale.getPrice().subtract(new BigDecimal(saleRecord.getProduct().getPrice())));
			}else if(SaleAppConstants.MULTIPLY.equals(saleRecord.getProduct().getOperation())) {
				productSale.setPrice(productSale.getPrice().multiply(new BigDecimal(saleRecord.getProduct().getPrice())));
			}
		}
		
	}
		
	
}