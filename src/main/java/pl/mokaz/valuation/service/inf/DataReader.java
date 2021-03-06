package pl.mokaz.valuation.service.inf;

import java.util.Objects;

@FunctionalInterface
public interface DataReader<T> {

	T read();

	default boolean validatePath(String path) {
		return Objects.isNull(path) || path.isEmpty();
	}
}
