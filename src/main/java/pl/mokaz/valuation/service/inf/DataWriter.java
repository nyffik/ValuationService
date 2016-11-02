package pl.mokaz.valuation.service.inf;

import java.util.List;

public interface DataWriter<T> {
	void write(String path, List<T> results);
}