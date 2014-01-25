package hana.analysis.models;

import java.util.*;

public class AnalysisResult {
	private Algorithm algorithm;
	private List<String> resultTables;
	private String message;
	
	public AnalysisResult(Algorithm algorithm, List<String> tables, String message) {
		this.algorithm = algorithm;
		this.resultTables = tables;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public AnalysisResult(Algorithm algorithm, List<String> tables) {
		this(algorithm, tables, "");
	}
	
	public Algorithm getAlgorithm() {
		return algorithm;
	}
	
	public List<String> getTables() {
		return resultTables;
	}
	
	public String ToSring() {
		return resultTables != null ? this.resultTables.toString(): message;
	}

}
