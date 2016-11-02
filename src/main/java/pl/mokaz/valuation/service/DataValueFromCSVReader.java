package pl.mokaz.valuation.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import pl.mokaz.valuation.exception.ValuationException;
import pl.mokaz.valuation.model.DataValue;
import pl.mokaz.valuation.service.inf.DataReader;

public class DataValueFromCSVReader implements DataReader<List<DataValue>> {

	private static final Logger logger = LoggerFactory.getLogger(DataValueFromCSVReader.class);
	private final String path;

	public DataValueFromCSVReader(String path) {
		super();
		this.path = path;
	}

	@Override
	public List<DataValue> read() {

		boolean isInvalidPath = validatePath(path);
		if (isInvalidPath) {
			return Collections.emptyList();
		}

		List<DataValue> datas;
		HeaderColumnNameMappingStrategy<DataValue> strategy = new HeaderColumnNameMappingStrategy<>();
		strategy.setType(DataValue.class);
		CsvToBean<DataValue> csvToBean = new CsvToBean<>();
		try (CSVReader reader = new CSVReader(new FileReader(path))) {
			datas = csvToBean.parse(strategy, reader);
			return datas;
		} catch (IOException e) {
			logger.error("Error during reading CVS File", e);
			throw new ValuationException("Error during reading CVS File", e);
		}
	}

}
