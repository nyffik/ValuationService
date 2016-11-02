package pl.mokaz.valuation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static pl.mokaz.valuation.service.TestPath.RESULT_FILE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.opencsv.CSVWriter;

import pl.mokaz.valuation.model.DataResult;
import pl.mokaz.valuation.service.inf.DataWriter;

@RunWith(MockitoJUnitRunner.class)
public class WriteCSVTest {

	private Path path;
	@Mock
	private ValuationService valuationService;

	@Before
	public void setUp() throws Exception {
		path = Paths.get(RESULT_FILE.getPath());

		if (Files.exists(path)) {
			Files.delete(path);
		}
	}

	@Test
	public void shouldWriteDataToCSV() {
		assertThat(Files.notExists(path)).isTrue();

		when(valuationService.calculate()).thenReturn(
				Arrays.asList(new DataResult(1l, new BigDecimal("12285.00"), new BigDecimal("3071.25"), "PLN", 1),
						new DataResult(2l, new BigDecimal("28350.00"), new BigDecimal("5670.00"), "PLN", 0),
						new DataResult(3l, new BigDecimal("27720.00"), new BigDecimal("2772.00"), "PLN", 2)));

		DataWriter<DataResult> writer = new DataResultCSVWriter();

		List<DataResult> datas = valuationService.calculate();

		writer.write(RESULT_FILE.getPath(), datas);

		assertThat(Files.exists(path)).isTrue();

		List<List<String>> readRecords = readRecords();

		assertThat(readRecords).hasSize(4)
				.contains(
						Arrays.asList("matching_id", "total_price", "avg_price", "currency", "ignored_products_count"))
				.contains(Arrays.asList("1", "12285.00", "3071.25", "PLN", "1"))
				.contains(Arrays.asList("2", "28350.00", "5670.00", "PLN", "0"))
				.contains(Arrays.asList("3", "27720.00", "2772.00", "PLN", "2"));
	}

	private List<List<String>> readRecords() {
		try (BufferedReader reader = new BufferedReader(Files.newBufferedReader(path, Charset.forName("UTF-8")))) {
			return reader.lines().map(l -> Arrays.asList(l.split(String.valueOf(CSVWriter.DEFAULT_SEPARATOR))))

					.collect(Collectors.toList());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

}
