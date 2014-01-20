package hana.analysis.models;

import java.util.*;

public class AnalysisResult {
	
	private List<String> resultTables;
	
	public AnalysisResult(List<String> tables) {
		this.resultTables = tables;
	}
	
	public List<String> getTables() {
		return resultTables;
	}
	
	public String ToSring() {
		return this.resultTables.toString();
	}

}
