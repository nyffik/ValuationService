package pl.mokaz.valuation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import pl.mokaz.valuation.model.DataValue;
import pl.mokaz.valuation.service.inf.ConversionService;

public class ConversionTest {

	private Map<String, BigDecimal> ratio = new HashMap<>();
	private Map<Long, Integer> matchings = new HashMap<>();
	private ConversionService<DataValue> conversionService;

	@Before
	public void setUp() {
		ratio.put("GBP", BigDecimal.valueOf(2.4));
		ratio.put("EU", BigDecimal.valueOf(2.1));
		ratio.put("PLN", BigDecimal.valueOf(1));

		matchings.put(1l, 2);
		matchings.put(2l, 2);
		matchings.put(3l, 3);
		conversionService = new DataValueConversionService(ratio, "PLN");

	}

	@Test
	public void shouldConvertPricesToPLNFromKnownCurrencies() {

		DataValue dataValueGBP = new DataValue();
		dataValueGBP.setId(1l);
		dataValueGBP.setCurrency("GBP");
		dataValueGBP.setPrice(new BigDecimal("1000"));
		dataValueGBP.setQuantity(2);

		DataValue convert = conversionService.convert(dataValueGBP);
		assertThat(convert.getId()).isEqualTo(1l);
		assertThat(convert.getCurrency()).isEqualTo("PLN");
		assertThat(convert.getPrice()).isEqualTo(new BigDecimal("2400.00"));
		assertThat(convert.totalPrice()).isEqualTo(new BigDecimal("4800.00"));

		DataValue dataValueEU = new DataValue();
		dataValueEU.setId(2l);
		dataValueEU.setCurrency("EU");
		dataValueEU.setPrice(new BigDecimal("1000"));
		dataValueEU.setQuantity(2);

		convert = conversionService.convert(dataValueEU);
		assertThat(convert.getId()).isEqualTo(2l);
		assertThat(convert.getCurrency()).isEqualTo("PLN");
		assertThat(convert.getPrice()).isEqualTo(new BigDecimal("2100.00"));
		assertThat(convert.totalPrice()).isEqualTo(new BigDecimal("4200.00"));

		DataValue dataValuePLN = new DataValue();
		dataValuePLN.setId(3l);
		dataValuePLN.setCurrency("PLN");
		dataValuePLN.setPrice(new BigDecimal("1000"));
		dataValuePLN.setQuantity(2);

		convert = conversionService.convert(dataValuePLN);
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

		assertThatThrownBy(() -> conversionService.convert(dataValue)).isInstanceOf(NullPointerException.class);

	}

	@Test
	public void shouldThrowNPEWhenCurrencyIsNull() {

		DataValue dataValue = new DataValue();
		dataValue.setPrice(new BigDecimal("1000"));
		assertThatThrownBy(() -> conversionService.convert(dataValue)).isInstanceOf(NullPointerException.class);
	}

	@Test
	public void shouldThrowNPEWhenCurrencyIsEmpty() {

		DataValue dataValue = new DataValue();
		dataValue.setPrice(new BigDecimal("1000"));
		dataValue.setCurrency("");

		assertThatThrownBy(() -> conversionService.convert(dataValue)).isInstanceOf(NullPointerException.class);
	}

	@Test
	public void shouldThrowNPEWhenPriceIsNull() {

		DataValue dataValue = new DataValue();
		dataValue.setCurrency("GBP");

		assertThatThrownBy(() -> conversionService.convert(dataValue)).isInstanceOf(NullPointerException.class);
	}

	@Test
	public void shouldThrowNPEWhenDataIsNull() {

		assertThatThrownBy(() -> conversionService.convert(null)).isInstanceOf(NullPointerException.class);
	}

}
