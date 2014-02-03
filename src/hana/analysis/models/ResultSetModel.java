package hana.analysis.models;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetModel {
	List<String> columns;
	List<List<Object>> values;
	int rowCount;

	public ResultSetModel(ResultSet rs) throws SQLException {

		if (rs == null)
			return;

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		columns = new ArrayList<String>();
		for (int i = 1; i <= columnsNumber; i++) {
			String columnName = rsmd.getColumnName(i).length() == 0 ? rsmd
					.getColumnLabel(i) : rsmd.getColumnName(i);
			columns.add(columnName);
		}

		values = new ArrayList<List<Object>>();

		rowCount = 0;

		while (rs.next()) {
			List<Object> row = new ArrayList<Object>();
			for (int i = 1; i <= columnsNumber; i++) {
				row.add(rs.getObject(i));
			}
			values.add(row);
			rowCount++;
		}
	}
}
