package pl.mokaz.valuation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.mokaz.valuation.service.TestPath.DATA_PATH;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import pl.mokaz.valuation.model.DataResult;

public class ValuationServiceTest {

	private Map<Long, Integer> matchings = new HashMap<>();
	private Map<String, BigDecimal> ratio = new HashMap<>();

	private AggregationService aggregationService;
	private ValuationService valuationService;

	@Before
	public void setUp() {

		ratio.put("GBP", BigDecimal.valueOf(2.4));
		ratio.put("EU", BigDecimal.valueOf(2.1));
		ratio.put("PLN", BigDecimal.valueOf(1));

		matchings.put(1l, 2);
		matchings.put(2l, 2);
		matchings.put(3l, 3);
		aggregationService = new AggregationService(new DataValueConversionService(ratio, "PLN"), matchings);
		valuationService = new ValuationService(new DataValueFromCSVReader(DATA_PATH.getPath()), aggregationService);
	}

	@Test
	public void shouldReturnProperDataResult() {

		List<DataResult> results = valuationService.calculate();

		assertThat(results).hasSize(3);
		results.sort(Comparator.comparingLong(DataResult::getMatchingId));

		DataResult dataResult1 = results.get(0);
		assertThat(dataResult1.getMatchingId()).isEqualTo(1l);
		assertThat(dataResult1.getCurrency()).isEqualTo("PLN");
		assertThat(dataResult1.getTotalPrice()).isEqualTo(new BigDecimal("12285.00"));
		assertThat(dataResult1.getAvgPrice()).isEqualTo(new BigDecimal("3071.25"));
		assertThat(dataResult1.getIgnoredCount()).isEqualTo(1);

		DataResult dataResult2 = results.get(1);
		assertThat(dataResult2.getMatchingId()).isEqualTo(2l);
		assertThat(dataResult2.getCurrency()).isEqualTo("PLN");
		assertThat(dataResult2.getTotalPrice()).isEqualTo(new BigDecimal("28350.00"));
		assertThat(dataResult2.getAvgPrice()).isEqualTo(new BigDecimal("5670.00"));
		assertThat(dataResult2.getIgnoredCount()).isEqualTo(0);

		DataResult dataResult3 = results.get(2);
		assertThat(dataResult3.getMatchingId()).isEqualTo(3l);
		assertThat(dataResult3.getCurrency()).isEqualTo("PLN");
		assertThat(dataResult3.getTotalPrice()).isEqualTo(new BigDecimal("27720.00"));
		assertThat(dataResult3.getAvgPrice()).isEqualTo(new BigDecimal("2772.00"));
		assertThat(dataResult3.getIgnoredCount()).isEqualTo(2);

	}

}
