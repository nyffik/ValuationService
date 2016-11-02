package pl.mokaz.valuation.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import pl.mokaz.valuation.model.DataResult;
import pl.mokaz.valuation.model.DataValue;
import pl.mokaz.valuation.service.inf.DataReader;

public class ValuationService {

	private final DataReader<List<DataValue>> dataReader;
	private AggregationService aggregationService;

	public ValuationService(DataReader<List<DataValue>> dataReader, DataReader<Map<String, BigDecimal>> currencyReader,
			DataReader<Map<Long, Integer>> matchingsReader, String targetCurrency) {
		super();
		this.dataReader = dataReader;
		aggregationService = new AggregationService(currencyReader.read(), targetCurrency, matchingsReader.read());
	}

	public List<DataResult> calculate() {
		List<DataValue> data = dataReader.read();

		final Map<Long, List<DataValue>> groupDatas = aggregationService.groupDatas(data);
		Map<Long, List<DataValue>> skipData = aggregationService.skipData(groupDatas);
		Map<Long, List<DataValue>> limitData = aggregationService.limitData(groupDatas);

		return limitData.entrySet().stream().map(e -> getResult(e, skipData)).collect(Collectors.toList());
	}

	private DataResult getResult(Entry<Long, List<DataValue>> entry, Map<Long, List<DataValue>> skipData) {

		DataResult dataResult = new DataResult();
		dataResult.setMatchingId(entry.getKey());

		List<DataValue> values = entry.getValue();

		dataResult.setCurrency(getCurrency(values));

		dataResult.setTotalPrice(getTotalPrice(values));
		dataResult.setAvgPrice(getAvgPrice(dataResult.getTotalPrice(), values));
		dataResult.setIgnoredCount(getQuantity(skipData.get(entry.getKey())));
		return dataResult;
	}

	private BigDecimal getAvgPrice(BigDecimal totalPrice, List<DataValue> values) {
		Integer collect = getQuantity(values);
		System.out.println(collect);
		return totalPrice.divide(new BigDecimal(collect));
	}

	private Integer getQuantity(List<DataValue> values) {
		return values.stream().collect(Collectors.summingInt(DataValue::getQuantity));
	}

	private BigDecimal getTotalPrice(List<DataValue> values) {
		return values.stream().map(DataValue::totalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private String getCurrency(List<DataValue> values) {
		return values.stream().reduce(this::getCurrency).get().getCurrency();
	}

	private DataValue getCurrency(DataValue d1, DataValue d2) {
		d1.setCurrency(d2.getCurrency());
		return d1;
	}

}
