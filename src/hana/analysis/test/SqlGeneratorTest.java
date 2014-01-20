package hana.analysis.test;

import static org.junit.Assert.*;

import java.util.*;

import hana.analysis.models.SqlGenerator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SqlGeneratorTest {

	SqlGenerator generator;
	LinkedHashMap<String, String> columns;
	List<Object> values;
	
	@Before
	public void setUp() throws Exception {
		generator = new SqlGenerator();
		columns = new LinkedHashMap<String, String>();
		values = new ArrayList<Object>();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateTableType() {
		String typeName = "PAL_T_KM_DATA";
		
		columns.put("ID", "INTEGER");
		columns.put("LIFESPEND", "DOUBLE");
		columns.put("NEWSPEND", "DOUBLE");
		columns.put("INCOME", "DOUBLE");
		columns.put("LOYALTY", "DOUBLE");
		
		String result = SqlGenerator.createTableType(typeName, columns);
		String expected = "CREATE TYPE PAL_T_KM_DATA AS TABLE (ID INTEGER, LIFESPEND DOUBLE, NEWSPEND DOUBLE, INCOME DOUBLE, LOYALTY DOUBLE);";
		
		assertEquals(expected,result);
	}
	
	@Test
	public void testMapToString() {
		
		columns.put("ID", "INTEGER");
		columns.put("LIFESPEND", "DOUBLE");
		columns.put("NEWSPEND", "DOUBLE");
		columns.put("INCOME", "DOUBLE");
		columns.put("LOYALTY", "DOUBLE");
		
		String result = SqlGenerator.mapToString(columns);
		
		assertEquals("ID INTEGER, LIFESPEND DOUBLE, NEWSPEND DOUBLE, INCOME DOUBLE, LOYALTY DOUBLE",result);
	}
	
	@Test
	public void testInsert() {
		String tableName = "PAL_KM_SIGNATURE";
		
		values.add(1);
		values.add("PAL_T_KM_DATA");
		values.add("in");
		
		String result = SqlGenerator.insert(tableName, values);
		
		assertEquals("INSERT INTO PAL_KM_SIGNATURE VALUES (1, 'PAL_T_KM_DATA', 'in');",result);
	}
	
	@Test
	public void testCallProcedure() {
		String procedureName = "SYSTEM.AFL_WRAPPER_ERASER";
		
		values.add("PAL_KM");
		
		String result = SqlGenerator.callProcedure(procedureName, values);
		
		assertEquals("CALL SYSTEM.AFL_WRAPPER_ERASER ('PAL_KM');",result);
		
		procedureName = "SYSTEM.AFL_WRAPPER_GENERATOR";
		values.clear();
		values.add("PAL_KM");
		values.add("AFLPAL");
		values.add("KMEANS");
		values.add("PAL_KM_SIGNATURE");
		
		result = SqlGenerator.callProcedure(procedureName, values);
		assertEquals("CALL SYSTEM.AFL_WRAPPER_GENERATOR ('PAL_KM', 'AFLPAL', 'KMEANS', 'PAL_KM_SIGNATURE');",result);
	}

}
