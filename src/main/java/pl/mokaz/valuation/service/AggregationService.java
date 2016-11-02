package pl.mokaz.valuation.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pl.mokaz.valuation.model.DataValue;
import pl.mokaz.valuation.service.inf.ConversionService;

public class AggregationService {

	private final Map<Long, Integer> matchings;
	private final ConversionService<DataValue> conversionService;

	public AggregationService(ConversionService<DataValue> conversionService, Map<Long, Integer> matchings) {
		this.matchings = matchings;
		this.conversionService = conversionService;
	}

	public Map<Long, List<DataValue>> groupDatas(List<DataValue> datas) {
		return datas.stream().map(this::convert).sorted(sort().reversed())
				.collect(Collectors.groupingBy(DataValue::getMatchingId));
	}

	private DataValue convert(DataValue dataValue) {
		return conversionService.convert(dataValue);
	}

	public Map<Long, List<DataValue>> limitData(Map<Long, List<DataValue>> group) {
		Map<Long, List<DataValue>> map = new HashMap<>(group);
		map.replaceAll((k, l) -> l.stream().limit(matchings.get(k)).collect(Collectors.toList()));
		return map;
	}

	public Map<Long, List<DataValue>> skipData(Map<Long, List<DataValue>> group) {
		Map<Long, List<DataValue>> map = new HashMap<>(group);
		map.replaceAll((k, l) -> l.stream().skip(matchings.get(k)).collect(Collectors.toList()));
		return map;
	}

	Comparator<DataValue> sort() {
		return (d1, d2) -> d1.totalPrice().compareTo(d2.totalPrice());
	}

}
