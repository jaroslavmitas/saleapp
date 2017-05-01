package cz.jaroslavmitas.service;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.digester3.Digester;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import cz.jaroslavmitas.dto.Product;
import cz.jaroslavmitas.dto.Sale;
import cz.jaroslavmitas.dto.SaleRecord;

public class ParsingService implements IParsingService{

	/*
	 * (non-Javadoc)
	 * @see cz.jaroslavmitas.service.IParsingService#parseMessage(java.lang.String)
	 */
	public SaleRecord parseMessage(String messageContent) {
		SaleRecord saleRecord = null;
		
		try {
			Digester digester = new Digester();
			digester.setValidating( false );
			
			//parsing
			digester.addObjectCreate("sale-record", SaleRecord.class );
			digester.addObjectCreate("sale-record/sale", Sale.class );
			digester.addBeanPropertySetter("sale-record/sale/sale-id","id");
			digester.addBeanPropertySetter("sale-record/sale/sale-type","messageType");
			digester.addSetNext("sale-record/sale", "setSale");
			
			digester.addObjectCreate("sale-record/product", Product.class );
			digester.addBeanPropertySetter("sale-record/product/operation","operation");
			digester.addBeanPropertySetter("sale-record/product/amount","amount");
			digester.addBeanPropertySetter("sale-record/product/name","name");
			digester.addBeanPropertySetter("sale-record/product/value","price");
			digester.addSetNext("sale-record/product", "setProduct");
			
			saleRecord = (SaleRecord)digester.parse( new InputSource(new StringReader(messageContent)) );
			
		}catch (SAXException se ) { 
			se.printStackTrace();
		}
		catch (IOException ioe) {
			 ioe.printStackTrace();
		}
		
		return saleRecord;
	}

}

