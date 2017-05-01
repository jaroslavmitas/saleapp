package cz.jaroslavmitas.service;

import java.util.ArrayList;
import java.util.Map;

import cz.jaroslavmitas.dto.Adjustments;
import cz.jaroslavmitas.dto.ProductSale;

public interface IReportService {
	
	/**
	 * Printing report name of sale, sales count and total value
	 */
	
	public void printSaleReport(Map<String, ArrayList<ProductSale>> sales);
	
	
	/**
	 * Printing adjustment report for sale 
	 */
	public void printAdjustmentReport(Map<String, ArrayList<Adjustments>> adjustments);
	
}
