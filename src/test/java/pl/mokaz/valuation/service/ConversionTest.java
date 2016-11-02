package pl.mokaz.valuation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import pl.mokaz.valuation.model.DataValue;
import pl.mokaz.valuation.service.inf.DataReader;

public class ConversionTest {
	private static final String DATA_PATH = "src/test/resources/data.csv";
	private Map<String, BigDecimal> ratio = new HashMap<>();
	private Map<Long, Integer> matchings = new HashMap<>();
	AggregationService aggregationService;

	@Before
	public void setUp() {
		ratio.put("GBP", BigDecimal.valueOf(2.4));
		ratio.put("EU", BigDecimal.valueOf(2.1));
		ratio.put("PLN", BigDecimal.valueOf(1));

		matchings.put(1l, 2);
		matchings.put(2l, 2);
		matchings.put(3l, 3);

		aggregationService = new AggregationService(ratio, "PLN", matchings);
	}

	@Test
	public void shouldConvertPricesToPLNFromKnownCurrencies() {

		DataValue dataValueGBP = new DataValue();
		dataValueGBP.setId(1l);
		dataValueGBP.setCurrency("GBP");
		dataValueGBP.setPrice(new BigDecimal("1000"));
		dataValueGBP.setQuantity(2);

		DataValue convert = aggregationService.convert(dataValueGBP);
		assertThat(convert.getId()).isEqualTo(1l);
		assertThat(convert.getCurrency()).isEqualTo("PLN");
		assertThat(convert.getPrice()).isEqualTo(new BigDecimal("2400.00"));
		assertThat(convert.totalPrice()).isEqualTo(new BigDecimal("4800.00"));

		DataValue dataValueEU = new DataValue();
		dataValueEU.setId(2l);
		dataValueEU.setCurrency("EU");
		dataValueEU.setPrice(new BigDecimal("1000"));
		dataValueEU.setQuantity(2);

		convert = aggregationService.convert(dataValueEU);
		assertThat(convert.getId()).isEqualTo(2l);
		assertThat(convert.getCurrency()).isEqualTo("PLN");
		assertThat(convert.getPrice()).isEqualTo(new BigDecimal("2100.00"));
		assertThat(convert.totalPrice()).isEqualTo(new BigDecimal("4200.00"));

		DataValue dataValuePLN = new DataValue();
		dataValuePLN.setId(3l);
		dataValuePLN.setCurrency("PLN");
		dataValuePLN.setPrice(new BigDecimal("1000"));
		dataValuePLN.setQuantity(2);

		convert = aggregationService.convert(dataValuePLN);
		assertThat(convert.getId()).isEqualTo(3l);
		assertThat(convert.getCurrency()).isEqualTo("PLN");
		assertThat(convert.getPrice()).isEqualTo(new BigDecimal("1000.00"));
		assertThat(convert.totalPrice()).isEqualTo(new BigDecimal("2000.00"));
	}

	@Test
	public void shouldThrowNPEWhenUnknownCurrency() {

		DataValue dataValue = new DataValue();
		dataValue.setPrice(new BigDecimal("1000"));
		dataValue.setCurrency("USD");

		assertThatThrownBy(() -> aggregationService.convert(dataValue)).isInstanceOf(NullPointerException.class);

	}

	@Test
	public void shouldThrowNPEWhenCurrencyIsNull() {

		DataValue dataValue = new DataValue();
		dataValue.setPrice(new BigDecimal("1000"));
		assertThatThrownBy(() -> aggregationService.convert(dataValue)).isInstanceOf(NullPointerException.class);
	}

	@Test
	public void shouldThrowNPEWhenCurrencyIsEmpty() {

		DataValue dataValue = new DataValue();
		dataValue.setPrice(new BigDecimal("1000"));
		dataValue.setCurrency("");

		assertThatThrownBy(() -> aggregationService.convert(dataValue)).isInstanceOf(NullPointerException.class);
	}

	@Test
	public void shouldThrowNPEWhenPriceIsEmpty() {

		DataValue dataValue = new DataValue();
		dataValue.setCurrency("GBP");

		assertThatThrownBy(() -> aggregationService.convert(dataValue)).isInstanceOf(NullPointerException.class);
	}

	@Test
	public void shouldThrowNPEWhenDataIsNull() {

		assertThatThrownBy(() -> aggregationService.convert(null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	public void shouldSortDataValueByTotalPriceDecr() {

		DataValue dataValue = new DataValue();
		dataValue.setId(1l);
		dataValue.setPrice(new BigDecimal("1000"));
		dataValue.setQuantity(2);

		DataValue dataValue2 = new DataValue();
		dataValue2.setId(2l);
		dataValue2.setPrice(new BigDecimal("1500"));
		dataValue2.setQuantity(1);

		DataValue dataValue3 = new DataValue();
		dataValue3.setId(3l);
		dataValue3.setPrice(new BigDecimal("500"));
		dataValue3.setQuantity(5);

		List<DataValue> list = Arrays.asList(dataValue, dataValue2, dataValue3);

		Collections.sort(list, aggregationService.sort().reversed());

		assertThat(list).hasSize(3);
		DataValue first = list.get(0);
		assertThat(first.getId()).isEqualTo(3l);
		assertThat(first.totalPrice()).isEqualTo(new BigDecimal("2500.00"));
		DataValue second = list.get(1);
		assertThat(second.getId()).isEqualTo(1l);
		assertThat(second.totalPrice()).isEqualTo(new BigDecimal("2000.00"));
		DataValue third = list.get(2);
		assertThat(third.getId()).isEqualTo(2l);
		assertThat(third.totalPrice()).isEqualTo(new BigDecimal("1500.00"));
	}

	@Test
	public void shouldLimitGroupedDataToCountFromFile() {
		Map<Long, List<DataValue>> groupDatas = aggregationService.groupDatas(readData());
		assertThat(groupDatas).hasSize(3).containsKeys(1l, 2l, 3l);
		assertThat(groupDatas.get(1l)).hasSize(3).first().hasFieldOrPropertyWithValue("id", 9l);
		assertThat(groupDatas.get(2l)).hasSize(2).first().hasFieldOrPropertyWithValue("id", 6l);
		assertThat(groupDatas.get(3l)).hasSize(4);

		Map<Long, List<DataValue>> limitData = aggregationService.limitData(groupDatas);
		assertThat(limitData).hasSize(3).containsKeys(1l, 2l, 3l);
		assertThat(limitData.get(1l)).hasSize(2);
		assertThat(limitData.get(2l)).hasSize(2);
		assertThat(limitData.get(3l)).hasSize(3);

	}

	@Test
	public void shouldGroupDatasByMatchingId() {
		List<DataValue> data = readData();

		Map<Long, List<DataValue>> groupDatas = aggregationService.groupDatas(data);

		assertThat(groupDatas).hasSize(3).containsKeys(1l, 2l, 3l);
		assertThat(groupDatas.get(1l)).hasSize(3).first().hasFieldOrPropertyWithValue("id", 9l);
		assertThat(groupDatas.get(2l)).hasSize(2).first().hasFieldOrPropertyWithValue("id", 6l);
		assertThat(groupDatas.get(3l)).hasSize(4).first().hasFieldOrPropertyWithValue("id", 5l);
	}

	private List<DataValue> readData() {
		DataReader<List<DataValue>> reader = new DataValueFromCSVReader(DATA_PATH);
		List<DataValue> data = reader.read();
		return data;
	}
}
