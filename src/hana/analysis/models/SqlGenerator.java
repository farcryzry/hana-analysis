package hana.analysis.models;

import java.util.*;

public class SqlGenerator {

	private static List<String> lstObject = new ArrayList<String>();

	public static void clearObjectList() {
		lstObject.clear();
	}

	public static void addToObjectList(String objectName) {
		if (!lstObject.contains(objectName)) {
			lstObject.add(objectName);
		}
	}

	public static String drop(String type, String name) {
		addToObjectList(name);
		return String.format("DROP %s %s;", type, name);
	}

	public static String setSchema(String name) {
		return String.format("SET SCHEMA %s;", name);
	}

	public static String createTableType(String typeName,
			LinkedHashMap<String, String> columns) {
		addToObjectList(typeName);
		String sql = String.format("CREATE TYPE %s AS TABLE (%s);", typeName,
				mapToString(columns));
		return sql;
	}

	public static String createTable(String tableName,
			LinkedHashMap<String, String> columns) {
		addToObjectList(tableName);
		String sql = String.format("CREATE COLUMN TABLE %s (%s);", tableName,
				mapToString(columns));
		return sql;
	}

	public static String createTableByType(String tableName, String type) {
		addToObjectList(tableName);
		String sql = String.format("CREATE COLUMN TABLE %s LIKE %s;",
				tableName, type);
		return sql;
	}

	public static String createViewByTable(String viewName, String tableName) {
		return createView(viewName,
				String.format("SELECT * FROM %S", tableName));
	}

	public static String createView(String viewName, IView view) {
		return createView(viewName, view.getViewDefination());
	}

	public static String createView(String viewName, String viewDefination) {
		addToObjectList(viewName);
		String sql = String.format("CREATE VIEW %s AS %S;", viewName,
				viewDefination);
		return sql;
	}

	public static String insert(String tableName, List<Object> values) {
		addToObjectList(tableName);
		String sql = String.format("INSERT INTO %s VALUES (%s);", tableName,
				rowListToString(values));
		return sql;
	}

	public static String insert(String tableName, Object[] values) {
		return insert(tableName, Arrays.asList(values));
	}

	public static String callProcedure(String procedureName, Object[] values) {
		return callProcedure(procedureName, Arrays.asList(values));
	}

	public static String callProcedure(String procedureName, List<Object> values) {
		return callProcedure(procedureName, values, true, false);
	}

	public static String callProcedure(String procedureName,
			List<Object> values, boolean needQuote, boolean withOverview) {
		String sql = String.format("CALL %s (%s) %s;", procedureName,
				listToString(values, needQuote), withOverview ? "WITH OVERVIEW"
						: "");
		return sql;
	}

	public static String update(String tableName, String setClause,
			String conditionClause) {

		String sql = String.format("UPDATE %s SET %s WHERE %s;", tableName,
				setClause, conditionClause);
		return sql;
	}

	public static String truncateTable(String tableName) {

		String sql = String.format("TRUNCATE TABLE %s;", tableName);
		return sql;
	}

	public static String mapToString(Map<String, String> map) {

		StringBuilder sbColumn = new StringBuilder();

		for (String key : map.keySet()) {
			sbColumn.append(key + " " + map.get(key) + ", ");
		}

		int ending = sbColumn.lastIndexOf(",");
		String str = sbColumn.substring(0, ending);

		return str;
	}

	public static String rowListToString(List<Object> list) {
		StringBuilder sbValue = new StringBuilder();

		for (Object value : list) {
			if (value instanceof String) {
				sbValue.append("'" + value + "'" + ", ");
			} else {
				sbValue.append(value + ", ");
			}
		}

		int ending = sbValue.lastIndexOf(",");
		String str = sbValue.substring(0, ending);

		return str;
	}

	public static String listToString(List<Object> list, boolean needQuoteParam) {

		StringBuilder sbValue = new StringBuilder();

		for (Object value : list) {
			boolean needQuote = needQuoteParam && !lstObject.contains(value);
			if (value instanceof String && needQuote) {
				sbValue.append("'" + value + "'" + ", ");
			} else {
				sbValue.append(value + ", ");
			}
		}

		int ending = sbValue.lastIndexOf(",");
		String str = sbValue.substring(0, ending);

		return str;
	}

	public static String listToString(List<Object> list) {
		return listToString(list, false);
	}

}
