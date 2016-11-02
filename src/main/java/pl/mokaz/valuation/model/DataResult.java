package pl.mokaz.valuation.model;

import java.math.BigDecimal;

import com.opencsv.bean.CsvBindByName;

public class DataResult {
	@CsvBindByName(column = "matching_id")
	private Long matchingId;
	@CsvBindByName(column = "total_price")
	private BigDecimal totalPrice;
	@CsvBindByName(column = "avg_price")
	private BigDecimal avgPrice;
	@CsvBindByName
	private String currency;
	@CsvBindByName(column = "ignored_products_count")
	private Integer ignoredCount;

	public Long getMatchingId() {
		return matchingId;
	}

	public void setMatchingId(Long matchingId) {
		this.matchingId = matchingId;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(BigDecimal avgPrice) {
		this.avgPrice = avgPrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getIgnoredCount() {
		return ignoredCount;
	}

	public void setIgnoredCount(Integer ignoredCount) {
		this.ignoredCount = ignoredCount;
	}

}
