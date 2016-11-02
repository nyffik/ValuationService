package pl.mokaz.valuation.service;

public enum TestPath {
	MATCHING_CSV("src/test/resources/matchings.csv"), CURRENCY_CSV("src/test/resources/currencies.csv"),
	DATA_PATH("src/test/resources/data.csv"), DATA_PATH_INVALID("src/test/resources/data1.csv"),
	RESULT_FILE("src/test/resources/top_products.csv")

	;
	private final String path;

	private TestPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
