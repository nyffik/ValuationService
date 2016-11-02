package pl.mokaz.valuation.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVWriter;

import pl.mokaz.valuation.exception.ValuationException;
import pl.mokaz.valuation.model.DataResult;
import pl.mokaz.valuation.service.inf.DataWriter;

public class DataResultCSVWriter implements DataWriter<DataResult> {

	private static final Logger logger = LoggerFactory.getLogger(CurrencyFromCSVReader.class);

	@Override
	public void write(String path, List<DataResult> results) {
		try (CSVWriter writer = new CSVWriter(new FileWriter(path), CSVWriter.DEFAULT_SEPARATOR,
				CSVWriter.NO_QUOTE_CHARACTER)) {
			String[] columns = new String[] { "matching_id", "total_price", "avg_price", "currency",
					"ignored_products_count" };

			writer.writeNext(columns);

			List<String[]> collect = results.stream().map(this::toStringArray).collect(Collectors.toList());

			writer.writeAll(collect);
		} catch (IOException e) {
			logger.error("Error during writing CVS File", e);
			throw new ValuationException("Error during writing CVS File", e);
		}
	}

	private String[] toStringArray(DataResult result) {
		return new String[] { String.valueOf(result.getMatchingId()), String.valueOf(result.getTotalPrice()),
				String.valueOf(result.getAvgPrice()), result.getCurrency(), String.valueOf(result.getIgnoredCount()) };
	}

}
