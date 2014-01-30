package hana.analysis.models;

import hana.analysis.db.Connector;

import java.sql.SQLException;
import java.util.*;

public class Analysis {
	private IAlgorithmAdapter adapter;

	public Analysis(Algorithm algorithm, String schemaName) {
		this.adapter = new AlgorithmAdapter(algorithm, schemaName);
	}

	public AnalysisResult action(boolean reGenerate, DataSource source, String classTable, List<List<Object>> classData,  List<String> trainningModelTables,
			Map<String, Object> params) {

		adapter.setSignature(source);
		String sqlBuild = reGenerate ?  adapter.build(source, classTable, classData) : "";
		String sqlRun = adapter.execute(source, classTable, trainningModelTables, params);

		Connector c = new Connector();
		try {
			if (reGenerate) {
				try {
					c.QueryWithoutResult(sqlBuild);
				} catch (Exception e) {
					e.printStackTrace();
					return new AnalysisResult(adapter.getAlgorithm(), null,
							e.getMessage());
				}
			}
			c.QueryWithoutResult(sqlRun);
			return new AnalysisResult(adapter.getAlgorithm(),
					adapter.getResultTables());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		Analysis analysis = new Analysis(new KmeansAlgorithm(), "PAL");

		LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>();
		columns.put("ID", "INTEGER");
		columns.put("LIFESPEND", "DOUBLE");
		columns.put("NEWSPEND", "DOUBLE");
		columns.put("INCOME", "DOUBLE");
		columns.put("LOYALTY", "DOUBLE");
		DataSource source = new DataSource("PAL", "CUSTOMERS", columns,
				"SELECT ID, LIFESPEND, NEWSPEND, INCOME, LOYALTY FROM PAL.CUSTOMERS");

		AnalysisResult result = analysis.action(true, source, null, null, null, null);
		System.out.println(result);

		Connector c = new Connector();
		for (String table : result.getTables()) {
			System.out.printf("<<<<<<<<<<%s>>>>>>>>\n", table);
			try {
				System.out.println(c.Query(String.format("select * from %s",
						table)));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
