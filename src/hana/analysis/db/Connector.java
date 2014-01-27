package hana.analysis.db;

import java.sql.*;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

//import net.sf.json.*;

/**
 * @author Jacob
 * 
 */
public class Connector {

	String userName = "PAL_DEV";
	String password = "Pal001122";
	String dbms = "sap";
	String serverName = "uvo1pubge222eztww2r.vm.cld.sr";
	String portNumber = "30015";
	String dbName = "";

	public Connection getConnection() throws SQLException,
			ClassNotFoundException {

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);
		connectionProps.put("allowMultiQuery", true);

		String connStr = "jdbc:" + this.dbms + "://" + this.serverName + ":"
				+ this.portNumber + "/";

		Class.forName("com.sap.db.jdbc.Driver");
		conn = DriverManager.getConnection(connStr, connectionProps);

		System.out.println("[Connected to database]");
		return conn;
	}

	// public List<HashMap<String, Object>> Query(String sql) throws
	// SQLException {
	public String Query(String sql) throws SQLException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);

			
			return convertResultSetToString(rs);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();
		}
		return null;
	}

	public String convertResultSetToString(ResultSet rs) throws SQLException, JsonProcessingException {
		/*
		StringBuilder sb = new StringBuilder();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		while (rs.next()) {
			for (int i = 1; i <= columnsNumber; i++) {
				if (i > 1)
					sb.append(",  ");
				String columnValue = rs.getString(i);
				sb.append(columnValue + " " + rsmd.getColumnName(i));
			}
			sb.append('\n');
		}
		System.out.println(sb.toString());
		return sb.toString();
		*/
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Object o = resultSetToHashMap(rs);
		String json = ow.writeValueAsString(o);
		return json;
	}

	public void QueryWithoutResult(String sql) throws SQLException,
			ClassNotFoundException {
		
		if(sql.isEmpty()) return;
		
		Connection con = null;
		Statement stmt = null;
		String[] sqls = sql.split(";");

		con = getConnection();
		stmt = con.createStatement();

		for (int i = 0; i < sqls.length; i++) {
			System.out.print(sqls[i]);
			try {
				con = getConnection();
				stmt = con.createStatement();
				stmt.execute(sqls[i]);
				System.out.println("...done");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (stmt != null)
			stmt.close();
		if (con != null)
			con.close();
	}

	/*
	 * public JSONArray QueryAsJSON(String sql) throws Exception { return
	 * convertToJSON(Query(sql)); }
	 */

	private List<HashMap<String, Object>> resultSetToHashMap(ResultSet rs)
			throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		while (rs.next()) {
			HashMap<String, Object> row = new HashMap<String, Object>();
			for (int i = 1; i <= columns; i++) {
				row.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(row);
		}
		return list;
	}

	/*
	public static JSONArray convertToJSON(ResultSet resultSet) throws Exception {
		JSONArray jsonArray = new JSONArray();
		while (resultSet.next()) {
			int total_rows = resultSet.getMetaData().getColumnCount();
			JSONObject obj = new JSONObject();
			for (int i = 0; i < total_rows; i++) {
				obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
						.toLowerCase(), resultSet.getObject(i + 1));
			}
			jsonArray.add(obj);
		}
		return jsonArray;
	}
	

	public static JSONArray convertToJSON(List<HashMap<String, Object>> list)
			throws Exception {

		JSONArray jsonArray = new JSONArray();

		for (HashMap<String, Object> map : list) {
			jsonArray.add(map);
		}

		return jsonArray;
	}
	*/

	public static void main(String args[]) {
		Connector c = new Connector();
		try {
			c.QueryWithoutResult("SET SCHEMA PAL;DROP TYPE PAL.PAL_T_KM_DATA;CREATE TYPE PAL.PAL_T_KM_DATA AS TABLE (ID INTEGER, LIFESPEND DOUBLE, NEWSPEND DOUBLE, INCOME DOUBLE, LOYALTY DOUBLE);DROP TYPE PAL.PAL_T_KM_PARAMS;CREATE TYPE PAL.PAL_T_KM_PARAMS AS TABLE (NAME VARCHAR(60), INTARGS INTEGER, DOUBLEARGS DOUBLE, STRINGARGS VARCHAR(100));DROP TYPE PAL.PAL_T_KM_RESULTS;CREATE TYPE PAL.PAL_T_KM_RESULTS AS TABLE (ID INTEGER, CENTER_ID INTEGER, DISTANCE DOUBLE);DROP TYPE PAL.PAL_T_KM_CENTERS;CREATE TYPE PAL.PAL_T_KM_CENTERS AS TABLE (CENTER_ID INTEGER, LIFESPEND DOUBLE, NEWSPEND DOUBLE, INCOME DOUBLE, LOYALTY DOUBLE);DROP TABLE PAL.PAL_KM_SIGNATURE;CREATE COLUMN TABLE PAL.PAL_KM_SIGNATURE (ID INTEGER, TYPENAME VARCHAR(100), DIRECTION VARCHAR(100));INSERT INTO PAL.PAL_KM_SIGNATURE VALUES (1, 'PAL.PAL_T_KM_DATA', 'in');INSERT INTO PAL.PAL_KM_SIGNATURE VALUES (2, 'PAL.PAL_T_KM_PARAMS', 'in');INSERT INTO PAL.PAL_KM_SIGNATURE VALUES (3, 'PAL.PAL_T_KM_RESULTS', 'out');INSERT INTO PAL.PAL_KM_SIGNATURE VALUES (4, 'PAL.PAL_T_KM_CENTERS', 'out');CALL SYSTEM.AFL_WRAPPER_ERASER('PAL_KM');CALL SYSTEM.AFL_WRAPPER_GENERATOR ('PAL_KM', 'AFLPAL', 'KMEANS', PAL.PAL_KM_SIGNATURE) ;SET SCHEMA PAL;DROP VIEW PAL.V_CUSTOMERS;CREATE VIEW PAL.V_CUSTOMERS AS SELECT ID, LIFESPEND, NEWSPEND, INCOME, LOYALTY FROM PAL.CUSTOMERS;DROP TABLE PAL.KMEANS_PARAM;CREATE COLUMN TABLE PAL.KMEANS_PARAM LIKE PAL.PAL_T_KM_PARAMS;DROP TABLE KMEANSRESULT1;CREATE COLUMN TABLE PAL.KMEANSRESULT1 LIKE PAL.PAL_T_KM_RESULTS;DROP TABLE PAL.KMEANSRESULT2;CREATE COLUMN TABLE PAL.KMEANSRESULT2 LIKE PAL.PAL_T_KM_CENTERS;INSERT INTO PAL.KMEANS_PARAM VALUES ('THREAD_NUMBER', 2, null, null);INSERT INTO PAL.KMEANS_PARAM VALUES ('GROUP_NUMBER', 3, null, null);INSERT INTO PAL.KMEANS_PARAM VALUES ('INIT_TYPE', 1, null, null);INSERT INTO PAL.KMEANS_PARAM VALUES ('DISTANCE_LEVEL', 2, null, null);INSERT INTO PAL.KMEANS_PARAM VALUES ('MAX_ITERATION', 100, null, null);INSERT INTO PAL.KMEANS_PARAM VALUES ('NORMALIZATION', 0, null, null);INSERT INTO PAL.KMEANS_PARAM VALUES ('EXIT_THRESHOLD', null, 1.0E-4, null);SET SCHEMA PAL;TRUNCATE TABLE PAL.KMEANSRESULT1;TRUNCATE TABLE PAL.KMEANSRESULT2;CALL _SYS_AFL.PAL_KM (PAL.V_CUSTOMERS, PAL.KMEANS_PARAM, PAL.KMEANSRESULT1, PAL.KMEANSRESULT2) WITH OVERVIEW;");
			c.QueryWithoutResult("set schema PAL");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
