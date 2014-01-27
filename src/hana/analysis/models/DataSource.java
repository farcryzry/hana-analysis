package hana.analysis.models;

import java.util.*;

public class DataSource extends TableType implements IView {
	private String viewDefination;
	List<List<Object>> data;

	public DataSource(String schemaName, String tableName,
			LinkedHashMap<String, String> columns) {
		this(schemaName, tableName, columns, null);
	}

	public DataSource(String schemaName, String tableName,
			LinkedHashMap<String, String> columns, String viewDefination) {
		super(schemaName, tableName, columns);
		this.viewDefination = viewDefination;
	}

	public DataSource(String schemaName, String tableName,
			LinkedHashMap<String, String> columns, String viewDefination,
			List<List<Object>> data) {
		this(schemaName, tableName, columns, viewDefination);
		this.data = data;
	}

	public String getTableName() {
		return super.getTypeName();
	}

	public List<List<Object>> getData() {
		return data;
	}

	public String getViewName() {
		return this.getSchemaName() + ".V_" + typeName;
	}

	public LinkedHashMap<String, String> getColumns() {
		return columns;
	}

	public boolean containsView() {
		return viewDefination != null && viewDefination.length() != 0;
	}

	public String getViewDefination() {
		return containsView() ? viewDefination : String.format(
				"SELECT * FROM %S", getTableName());
	}
}