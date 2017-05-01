package cz.jaroslavmitas.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.jaroslavmitas.dto.Adjustments;
import cz.jaroslavmitas.dto.ProductSale;

public class ReportService implements IReportService {

	
	/*
	 * (non-Javadoc)
	 * @see cz.jaroslavmitas.service.IReportService#printSaleReport(java.util.Map)
	 */
	public void printSaleReport(Map<String, ArrayList<ProductSale>> sales) {
		if(sales == null) {
			return;
		}
		Map<String, Long> productCounter = new HashMap<String, Long>(); 
		
		Set<String> saleNames = sales.keySet();
		BigDecimal totalSum = null;
		
		System.out.println("\n\n\nPrint sale report");
		
		for(String saleName: saleNames) {
			totalSum = new BigDecimal(0);
			List<ProductSale> productSaleList = sales.get(saleName);
			Long counter = 0L;
			
			for(ProductSale productSale : productSaleList) {
				counter +=  productSale.getAmount();
				totalSum = totalSum.add(productSale.getPrice().multiply(new BigDecimal(productSale.getAmount())));
			}
			productCounter.put(saleName, counter);
			System.out.println("Sale type:" + saleName+", counter:"+counter+", total sum:"+ totalSum);
		}
		
		
			
		
	}

	/*
	 * (non-Javadoc)
	 * @see cz.jaroslavmitas.service.IReportService#printAdjustmentReport(java.util.Map)
	 */
	public void printAdjustmentReport(Map<String, ArrayList<Adjustments>> adjustments) {
		if(adjustments == null) {
			return;
		}
		
		System.out.println("\n\n\nAdjustment report:");
		Set<String> saleNames = adjustments.keySet();
		for(String saleName: saleNames) {
			List<Adjustments> adjustmentsList = adjustments.get(saleName);
			System.out.println("Sale type: "+saleName);
			for(Adjustments adjustment: adjustmentsList) {
				System.out.println("    operation:"+adjustment.getOperation() +", value: "+adjustment.getValue());
			}
		} 
	}
	
}
