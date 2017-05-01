package cz.jaroslavmitas.service;

import cz.jaroslavmitas.dto.SaleRecord;

public interface IParsingService {

	/**
	 * Parsing XML message to java DTO. 
	 * @param messageContent - XML message in String 
	 * @return SaleRecord 
	 */
	public SaleRecord parseMessage(String messageContent);
}
