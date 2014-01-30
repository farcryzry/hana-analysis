package hana.analysis.models;

import java.util.*;

public class AlgorithmAdapter implements IAlgorithmAdapter {
	private Algorithm algorithm;
	private List<String> resultTables;
	private String schemaName;

	public AlgorithmAdapter(Algorithm algorithm, String schemaName) {
		this.algorithm = algorithm;
		this.resultTables = new ArrayList<String>();
		this.schemaName = schemaName;
	}

	public List<String> getResultTables() {
		return resultTables;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public AlgorithmSignature getSignature() {
		return this.algorithm.getSignature();
	}

	public void setSignature(DataSource source) {
		algorithm.setSignature(source);
	}

	public String build(DataSource source, String classTable,
			List<List<Object>> classData) {

		StringBuilder sb = new StringBuilder();

		// -- PAL setup

		sb.append(SqlGenerator.setSchema(schemaName));

		List<TableType> allTypes = algorithm.getSignature().getAllTypes();
		for (TableType type : allTypes) {
			sb.append(type.drop());
			sb.append(type.create());
		}

		sb.append(getSignature().drop());
		sb.append(getSignature().create());

		List<Object> lstProcedureParam = new ArrayList<Object>();
		lstProcedureParam.add(algorithm.getProcedureName());
		sb.append(SqlGenerator.callProcedure("SYSTEM.AFL_WRAPPER_ERASER",
				lstProcedureParam));

		lstProcedureParam.clear();
		lstProcedureParam.add(algorithm.getProcedureName());
		lstProcedureParam.add(algorithm.getLibraryName());
		lstProcedureParam.add(algorithm.getName());
		lstProcedureParam.add(getSignature().getName());
		sb.append(SqlGenerator.callProcedure("SYSTEM.AFL_WRAPPER_GENERATOR",
				lstProcedureParam));

		// -- App setup

		sb.append(SqlGenerator.setSchema(schemaName));

		if (source.containsView()) {
			sb.append(SqlGenerator.drop("VIEW", source.getViewName()));
			sb.append(SqlGenerator.createView(source.getViewName(),
					source.getViewDefination()));
		} else {
			sb.append(SqlGenerator.drop("TABLE", source.getTableName()));
			sb.append(SqlGenerator.createTableByType(source.getTableName(),
					getSignature().getDataSourceType().getTypeName()));
		}
		sb.append(getSignature().getParamTableType().createTableByType(
				algorithm.getParamTableName(this.schemaName)));

		if (!source.containsView()) {
			sb.append(SqlGenerator.truncateTable(source.getTableName()));
			List<List<Object>> tableData = source.getData();
			for (List<Object> row : tableData) {
				sb.append(SqlGenerator.insert(source.getTableName(), row));
			}
		}
		
		if (classTable != null && classTable.length() >0 && classData != null) {
			sb.append(SqlGenerator.truncateTable(classTable));
			for (List<Object> row : classData) {
				sb.append(SqlGenerator.insert(classTable, row));
			}
		}


		int idx = 1;
		for (TableType type : getSignature().getResultTableTypes()) {
			sb.append(type.createTableByType(type.getSchemaName() + "."
					+ algorithm.getName() + "RESULT" + idx));
			idx++;
		}

		sb.append(algorithm.initParams());

		System.out.println(sb);

		return sb.toString();
	}

	public String execute(DataSource source, String classTable,
			List<String> trainningModelTables, Map<String, Object> params) {
		StringBuilder sb = new StringBuilder();

		// -- app runtime

		sb.append(SqlGenerator.setSchema(schemaName));

		if (params != null) {
			sb.append(algorithm.updateParams(params));
		}

		sb.append(getSignature().truncate(schemaName));

		List<Object> lstProcedureParam = new ArrayList<Object>();
		if (source.containsView()) {
			lstProcedureParam.add(source.getViewName());
		} else {
			lstProcedureParam.add(source.getTableName());
		}

		if (classTable != null && classTable.length() > 0) {
			SqlGenerator.addToObjectList(classTable);
			lstProcedureParam.add(classTable);
		}

		lstProcedureParam.add(algorithm.getParamTableName(this.schemaName));

		if (trainningModelTables != null) {
			for (String trainningModelTable : trainningModelTables) {
				if (trainningModelTable != null
						&& trainningModelTable.length() > 0) {
					SqlGenerator.addToObjectList(trainningModelTable);
					lstProcedureParam.add(trainningModelTable);
				}
			}
		}

		resultTables.clear();
		int idx = 1;
		for (TableType type : getSignature().getResultTableTypes()) {
			String tableName = type.schemaName + "." + algorithm.getName()
					+ "RESULT" + idx;
			lstProcedureParam.add(tableName);
			resultTables.add(tableName);
			idx++;
		}

		sb.append(SqlGenerator.callProcedure(
				"_SYS_AFL." + algorithm.getProcedureName(), lstProcedureParam,
				false, true));

		System.out.println(sb);

		return sb.toString();
	}
}
