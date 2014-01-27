package hana.analysis.models;

import java.util.*;

public class AlgorithmSignature implements ISqlGenerator {
	private String name;
	private String algorithmName;
	private TableType dataSourceType;
	private TableType modelTableType;
	private TableType paramTableType;
	private List<TableType> resultTableTypes;
	LinkedHashMap<String, String> columns;

	public AlgorithmSignature(String name, String algorithmName) {
		this.name = name;
		this.algorithmName = algorithmName;

		columns = new LinkedHashMap<String, String>();
		columns.put("ID", "INTEGER");
		columns.put("TYPENAME", "VARCHAR(100)");
		columns.put("DIRECTION", "VARCHAR(100)");
	}

	public String getName() {
		return this.name;
	}

	public TableType getDataSourceType() {
		return dataSourceType;
	}

	public void setDataSourceType(TableType dataSourceType) {
		this.dataSourceType = dataSourceType;
	}
	
	public TableType getModelTableType() {
		return modelTableType;
	}

	public void setModelTableType(TableType modelTableType) {
		this.modelTableType = modelTableType;
	}

	public TableType getParamTableType() {
		return paramTableType;
	}

	public void setParamTableType(TableType paramTableType) {
		this.paramTableType = paramTableType;
	}

	public List<TableType> getResultTableTypes() {
		return resultTableTypes;
	}

	public void addResultTableType(TableType resultTableType) {
		if (resultTableTypes == null)
			resultTableTypes = new ArrayList<TableType>();
		resultTableTypes.add(resultTableType);
	}

	public List<TableType> getAllTypes() {

		List<TableType> lst = new ArrayList<TableType>();
		lst.add(dataSourceType);
		lst.add(paramTableType);
		//lst.add(modelTableType);
		lst.addAll(resultTableTypes);

		return lst;
	}

	@Override
	public String create() {
		String sql = "";
		sql += SqlGenerator.createTable(name, columns);
		
		int index = 0;

		sql += SqlGenerator.insert(name, new Object[] { ++ index,
				dataSourceType.getTypeName(), "in" });
		sql += SqlGenerator.insert(name, new Object[] { ++ index,
				paramTableType.getTypeName(), "in" });
		
		if(modelTableType != null) {
			sql += SqlGenerator.insert(name, new Object[] { ++ index,
					modelTableType.getTypeName(), "in" });
		}
		
		index ++;
		
		for (int i = 0; i < resultTableTypes.size(); i++) {
			sql += SqlGenerator.insert(name, new Object[] { index + i,
					resultTableTypes.get(i).getTypeName(), "out" });
		}

		return sql;
	}

	@Override
	public String drop() {
		return SqlGenerator.drop("TABLE", name);
	}

	public String truncate(String schemaName) {
		String sql = "";
		int idx = 1;
		for (@SuppressWarnings("unused") TableType type : resultTableTypes) {
			sql += SqlGenerator.truncateTable((schemaName + "." + algorithmName + "RESULT" + idx)) + "\n";
			idx ++;
		}

		return sql;
	}
}
