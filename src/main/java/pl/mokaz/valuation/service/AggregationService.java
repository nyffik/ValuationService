package pl.mokaz.valuation.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pl.mokaz.valuation.model.DataValue;

public class AggregationService {

	private final Map<String, BigDecimal> ratio;
	private final String targetCurrency;
	private final Map<Long, Integer> matchings;

	public AggregationService(Map<String, BigDecimal> ratio, String targetCurrency, Map<Long, Integer> matchings) {
		this.ratio = ratio;
		this.targetCurrency = targetCurrency;
		this.matchings = matchings;
	}

	public Map<Long, List<DataValue>> groupDatas(List<DataValue> datas) {
		return datas.stream().map(this::convert).sorted(sort().reversed())
				.collect(Collectors.groupingBy(DataValue::getMatchingId));
	}

	public Map<Long, List<DataValue>> limitData(Map<Long, List<DataValue>> group) {

		System.out.println(group);
		Map<Long, List<DataValue>> map = new HashMap<>(group);

		map.replaceAll((k, l) -> l.stream().limit(matchings.get(k)).collect(Collectors.toList()));
		System.out.println(map);
		return map;
	}

	public Map<Long, List<DataValue>> skipData(Map<Long, List<DataValue>> group) {

		System.out.println(group);
		Map<Long, List<DataValue>> map = new HashMap<>(group);

		map.replaceAll((k, l) -> l.stream().skip(matchings.get(k)).collect(Collectors.toList()));
		System.out.println(map);
		return map;
	}

	Comparator<DataValue> sort() {
		return (d1, d2) -> d1.totalPrice().compareTo(d2.totalPrice());
	}

	DataValue convert(DataValue dataValue) {
		dataValue.setPrice(
				dataValue.getPrice().multiply(ratio.get(dataValue.getCurrency())).setScale(2, RoundingMode.HALF_UP));
		dataValue.setCurrency(targetCurrency);
		return dataValue;
	}

}
