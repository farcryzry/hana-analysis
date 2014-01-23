package hana.analysis.models;

import java.util.*;

public class AnalysisResult {
	
	private List<String> resultTables;
	private String message;
	
	public AnalysisResult(List<String> tables, String message) {
		this.resultTables = tables;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public AnalysisResult(List<String> tables) {
		this(tables, "");
	}
	
	public List<String> getTables() {
		return resultTables;
	}
	
	public String ToSring() {
		return resultTables != null ? this.resultTables.toString(): message;
	}

}
