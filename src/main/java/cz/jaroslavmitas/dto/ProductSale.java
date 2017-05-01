package cz.jaroslavmitas.dto;

import java.math.BigDecimal;

public class ProductSale {
	
	private String productName;
	private long   amount;
	private BigDecimal price;
	
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "ProductSale [productName=" + productName + ", amount=" + amount
				+ ", price=" + price + "]";
	}
	
}
