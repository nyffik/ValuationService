package pl.mokaz.valuation.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.bean.BeanToCsv;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import pl.mokaz.valuation.model.DataResult;

public class CVSWriter {

	public void write(String path, List<DataResult> results) {
		try (CSVWriter writer = new CSVWriter(new FileWriter(path), CSVWriter.DEFAULT_SEPARATOR,
				CSVWriter.NO_QUOTE_CHARACTER)) {

			HeaderColumnNameMappingStrategy<DataResult> strategy = new HeaderColumnNameMappingStrategy<>();
			strategy.setType(DataResult.class);
			// strategy.d
			String[] columns = new String[] { "matchingId", "totalPrice", "avgPrice", "currency", "ignoredCount" };
			// strategy.setColumnMapping(columns);

			BeanToCsv<DataResult> beanToCsv = new BeanToCsv<>();

			beanToCsv.write(strategy, writer, results);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * writer.writeAll(results); // feed in your array (or convert your data
		 * to an array) String[] entries = "first#second#third".split("#");
		 * writer.writeNext(entries); writer.close();
		 */
	}

}
