package cz.jaroslavmitas.dto;

public class Product {

	private String name;
	
	private String price;
	
	private Long amount;
	
	private String operation;

	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", price=" + price + ", amount="
				+ amount + ", operation=" + operation + "]";
	}

}
