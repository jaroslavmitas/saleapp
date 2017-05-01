package cz.jaroslavmitas.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SaleRecord implements Serializable {

	private Sale sale;
	
	private Product product;

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "SaleRecord [sale=" + sale + ", product=" + product + "]";
	}
	
		
}
