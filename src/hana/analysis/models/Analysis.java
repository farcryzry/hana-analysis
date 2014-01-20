package hana.analysis.models;

import hana.analysis.db.Connector;

import java.sql.SQLException;
import java.util.*;

public class Analysis {
	private IAlgorithmAdapter adapter;
	
	public Analysis(Algorithm algorithm) {
		this.adapter = new AlgorithmAdapter(algorithm);
	}
	
	public AnalysisResult action(DataSource source) {

		String sqlBuild = adapter.build(source);
		String sqlRun = adapter.execute(source, null);
		
		Connector c = new Connector();
		try {
			c.QueryWithoutResult(sqlBuild);
			c.QueryWithoutResult(sqlRun);
			return new AnalysisResult(adapter.getResultTables());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		Analysis analysis = new Analysis(new KmeansAlgorithm());
		
		LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>();
		columns.put("ID", "INTEGER");
		columns.put("LIFESPEND", "DOUBLE");
		columns.put("NEWSPEND", "DOUBLE");
		columns.put("INCOME", "DOUBLE");
		columns.put("LOYALTY","DOUBLE");
		DataSource source = new DataSource("PAL", "CUSTOMERS", columns, "SELECT ID, LIFESPEND, NEWSPEND, INCOME, LOYALTY FROM CUSTOMERS");
		
		AnalysisResult result = analysis.action(source);
		System.out.println(result);
		
		Connector c = new Connector();
		for( String table : result.getTables()) {
			System.out.printf("<<<<<<<<<<%s>>>>>>>>\n", table);
			try {
				System.out.println(c.Query(String.format("select * from PAL.%s",table)));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}