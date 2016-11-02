package pl.mokaz.valuation.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import pl.mokaz.valuation.model.DataValue;
import pl.mokaz.valuation.service.inf.ConversionService;

public class DataValueConversionService implements ConversionService<DataValue> {

	private final Map<String, BigDecimal> ratio;
	private final String targetCurrency;

	public DataValueConversionService(Map<String, BigDecimal> ratio, String targetCurrency) {
		super();
		this.ratio = ratio;
		this.targetCurrency = targetCurrency;
	}

	@Override
	public DataValue convert(DataValue dataValue) {
		dataValue.setPrice(
				dataValue.getPrice().multiply(ratio.get(dataValue.getCurrency())).setScale(2, RoundingMode.HALF_UP));
		dataValue.setCurrency(targetCurrency);
		return dataValue;
	}
}
