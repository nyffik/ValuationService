package pl.mokaz.valuation.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;

import pl.mokaz.valuation.exception.ValuationException;
import pl.mokaz.valuation.service.inf.DataReader;

public class MatchingsFromCSVReader implements DataReader<Map<Long, Integer>> {

	private static final Logger logger = LoggerFactory.getLogger(CurrencyFromCSVReader.class);

	@Override
	public Map<Long, Integer> read(String path) {
		boolean isInvalidPath = validatePath(path);
		if (isInvalidPath) {
			return new HashMap<>();
		}
		try (CSVReader reader = new CSVReader(new FileReader(path), ',', '"', 1)) {

			List<String[]> readAll = reader.readAll();

			Map<Long, Integer> map = readAll.stream()
					.collect(Collectors.toMap(l -> Long.valueOf(l[0]), l -> Integer.valueOf(l[1])));
			return map;
		} catch (IOException e) {
			logger.error("Error during reading CVS File", e);
			throw new ValuationException("Error during reading CVS File", e);
		}
	}
}
