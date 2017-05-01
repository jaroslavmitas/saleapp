package cz.jaroslavmitas.dto;

import java.math.BigDecimal;

public class Adjustments {
	
	private String operation;
	private String name;
	private BigDecimal value;
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "Adjustments [operation=" + operation + ", name=" + name
				+ ", value=" + value + "]";
	}
	
}
