package pl.mokaz.valuation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import pl.mokaz.valuation.exception.ValuationException;
import pl.mokaz.valuation.model.DataValue;
import pl.mokaz.valuation.service.CurrencyFromCSVReader;
import pl.mokaz.valuation.service.DataValueFromCSVReader;
import pl.mokaz.valuation.service.MatchingsFromCSVReader;
import pl.mokaz.valuation.service.inf.DataReader;

public class ReadCvsTest {

	private static final String MATCHING_CSV = "src/test/resources/matchings.csv";
	private static final String CURRENCY_CSV = "src/test/resources/currencies.csv";
	private static final String DATA_PATH = "src/test/resources/data.csv";
	private static final String DATA_PATH_INVALID = "src/test/resources/data1.csv";

	@Test
	public void shouldReadDataFromCSV() {
		DataReader<List<DataValue>> reader = new DataValueFromCSVReader();
		List<DataValue> data = reader.read(DATA_PATH);
		assertThat(data).hasSize(9);

		DataValue firtsData = data.get(0);
		assertThat(firtsData.getId()).isEqualTo(1l);
		assertThat(firtsData.getCurrency()).isEqualTo("GBP");
		assertThat(firtsData.getQuantity()).isEqualTo(2);
		assertThat(firtsData.getPrice()).isEqualTo(new BigDecimal("1000"));
		assertThat(firtsData.getMatchingId()).isEqualTo(3);

		DataValue lastData = data.get(8);
		assertThat(lastData.getId()).isEqualTo(9l);
		assertThat(lastData.getCurrency()).isEqualTo("GBP");
		assertThat(lastData.getQuantity()).isEqualTo(3);
		assertThat(lastData.getPrice()).isEqualTo(new BigDecimal("1400"));
		assertThat(lastData.getMatchingId()).isEqualTo(1);
	}

	@Test
	public void shouldReturnEmptyListWhenDataPathIsNull() {
		DataReader<List<DataValue>> reader = new DataValueFromCSVReader();

		List<DataValue> datas = reader.read(null);
		assertThat(datas).isEmpty();
	}

	@Test
	public void shouldReturnEmptyListWhenDataPathIEmpty() {
		DataReader<List<DataValue>> reader = new DataValueFromCSVReader();

		List<DataValue> datas = reader.read("");
		assertThat(datas).isEmpty();
	}

	@Test
	public void shouldThrowValuationExceptionWhenDataPathNotExist() {
		DataReader<List<DataValue>> reader = new DataValueFromCSVReader();

		assertThatThrownBy(() -> reader.read(DATA_PATH_INVALID)).isInstanceOf(ValuationException.class)
				.hasMessageContaining("Error during reading CVS File");
	}

	@Test
	public void shouldReadCurrencyFileFromCVS() {
		DataReader<Map<String, BigDecimal>> reader = new CurrencyFromCSVReader();
		Map<String, BigDecimal> currencies = reader.read(CURRENCY_CSV);

		assertThat(currencies).hasSize(3).contains(entry("GBP", new BigDecimal("2.4")),
				entry("EU", new BigDecimal("2.1")), entry("PLN", new BigDecimal("1")));
	}

	@Test
	public void shouldReturnEmptyMapWhenCurrencyPathIsNull() {
		DataReader<Map<String, BigDecimal>> reader = new CurrencyFromCSVReader();

		Map<String, BigDecimal> datas = reader.read(null);
		assertThat(datas).isEmpty();
	}

	@Test
	public void shouldReturnEmptyMapWhenCurrencyPathIEmpty() {
		DataReader<Map<String, BigDecimal>> reader = new CurrencyFromCSVReader();

		Map<String, BigDecimal> datas = reader.read("");
		assertThat(datas).isEmpty();
	}

	@Test
	public void shouldThrowValuationExceptionWhenCurrencyPathNotExist() {
		DataReader<Map<String, BigDecimal>> reader = new CurrencyFromCSVReader();

		assertThatThrownBy(() -> reader.read(DATA_PATH_INVALID)).isInstanceOf(ValuationException.class)
				.hasMessageContaining("Error during reading CVS File");
	}

	@Test
	public void shouldReadMatchingFileFromCVS() {
		DataReader<Map<Long, Integer>> reader = new MatchingsFromCSVReader();
		Map<Long, Integer> currencies = reader.read(MATCHING_CSV);

		assertThat(currencies).hasSize(3).contains(entry(1l, 2), entry(2l, 2), entry(3l, 3));
	}

	@Test
	public void shouldReturnEmptyMapWhenMatchingsPathIsNull() {
		DataReader<Map<Long, Integer>> reader = new MatchingsFromCSVReader();

		Map<Long, Integer> datas = reader.read(null);
		assertThat(datas).isEmpty();
	}

	@Test
	public void shouldReturnEmptyMapWhenMatchingsPathIEmpty() {
		DataReader<Map<Long, Integer>> reader = new MatchingsFromCSVReader();

		Map<Long, Integer> datas = reader.read("");
		assertThat(datas).isEmpty();
	}

	@Test
	public void shouldThrowValuationExceptionWhenMatchingsPathNotExist() {
		DataReader<Map<Long, Integer>> reader = new MatchingsFromCSVReader();

		assertThatThrownBy(() -> reader.read(DATA_PATH_INVALID)).isInstanceOf(ValuationException.class)
				.hasMessageContaining("Error during reading CVS File");
	}

}
