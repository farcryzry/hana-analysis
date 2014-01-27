package hana.analysis.models;

import java.util.*;

public interface IAlgorithmAdapter {
	String build(DataSource source);
	String execute(DataSource source, List<String> trainningModelTable, Map<String, Object> params);
	List<String> getResultTables();
	Algorithm getAlgorithm();
	void setSignature(DataSource source);
}
