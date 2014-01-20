package hana.analysis.models;

import java.util.*;

public class DataSource extends TableType implements IView {
	private String viewDefination;
	
	public DataSource(String schemaName, String tableName, LinkedHashMap<String, String> columns) {
		this(schemaName, tableName, columns, null);
	}
	
	public DataSource(String schemaName, String tableName, LinkedHashMap<String, String> columns, String viewDefination) {
		super(schemaName, tableName, columns);
		this.viewDefination = viewDefination;
	}

	public String getTableName() {
		return super.getTypeName();
	}
	
	public String getViewName() {
		return this.getSchemaName() +  ".V_" + typeName;
	}

	public LinkedHashMap<String, String> getColumns() {
		return columns;
	}
	
	public String getViewDefination() {
		return viewDefination != null ? viewDefination : String.format("SELECT * FROM %S", getTableName());
	}
}