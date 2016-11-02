package pl.mokaz.valuation.service.inf;

@FunctionalInterface
public interface ConversionService<T> {
	T convert(T elemenent);
}
