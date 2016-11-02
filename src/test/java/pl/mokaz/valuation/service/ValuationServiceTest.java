package pl.mokaz.valuation.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import pl.mokaz.valuation.model.DataResult;

public class ValuationServiceTest {
	private static final String MATCHING_CSV = "src/test/resources/matchings.csv";
	private static final String CURRENCY_CSV = "src/test/resources/currencies.csv";
	private static final String DATA_PATH = "src/test/resources/data.csv";

	@Test
	public void shouldReturnData() {
		ValuationService valuationService = new ValuationService(new DataValueFromCSVReader(DATA_PATH),
				new CurrencyFromCSVReader(CURRENCY_CSV), new MatchingsFromCSVReader(MATCHING_CSV), "PLN");

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

		CVSWriter cvsWriter = new CVSWriter();
		cvsWriter.write("src/test/resources/top_products.csv", results);

	}

}
