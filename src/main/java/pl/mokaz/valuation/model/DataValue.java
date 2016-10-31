package pl.mokaz.valuation.model;

import java.math.BigDecimal;

import com.opencsv.bean.CsvBindByName;

public class DataValue {
	@CsvBindByName
	private Long id;
	@CsvBindByName
	private BigDecimal price;
	@CsvBindByName
	private String currency;
	@CsvBindByName
	private Integer quantity;
	@CsvBindByName(column = "matching_id")
	private Long matchingId;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DataValue [id=");
		builder.append(id);
		builder.append(", price=");
		builder.append(price);
		builder.append(", currency=");
		builder.append(currency);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", matchingId=");
		builder.append(matchingId);
		builder.append("]");
		return builder.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getMatchingId() {
		return matchingId;
	}

	public void setMatchingId(Long matchingId) {
		this.matchingId = matchingId;
	}
}
