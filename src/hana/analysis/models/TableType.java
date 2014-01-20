package hana.analysis.models;

import java.util.LinkedHashMap;

public class TableType implements ISqlGenerator {
	protected String schemaName;
	protected String typeName;
	protected LinkedHashMap<String, String> columns;
	
	public TableType(String schemaName, String typeName, LinkedHashMap<String, String> columns) {
		this.schemaName = schemaName;
		this.typeName = typeName;
		this.columns = columns;
	}

	public String getTypeName() {
		return getSchemaName() + "." + typeName;
	}
	
	public String getSchemaName() {
		return schemaName;
	}

	public LinkedHashMap<String, String> getColumns() {
		return columns;
	}

	@Override
	public String create() {
		return SqlGenerator.createTableType(getTypeName(), columns);
	}

	@Override
	public String drop() {
		return SqlGenerator.drop("TYPE", getTypeName());
	}
	
	public String createTableByType(String tableName) {
		return SqlGenerator.drop("TABLE", tableName) + 
				SqlGenerator.createTableByType(tableName, getTypeName());
	}
}
