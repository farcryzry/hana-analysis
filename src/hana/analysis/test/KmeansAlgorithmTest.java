package hana.analysis.test;

import static org.junit.Assert.*;

import java.util.*;

import hana.analysis.models.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KmeansAlgorithmTest {
	
	KmeansAlgorithm algorithm;
	DataSource dataSource;

	@Before
	public void setUp() throws Exception {
		algorithm = new KmeansAlgorithm();
		
		LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>();
		columns.put("ID", "INTEGER");
		columns.put("LIFESPEND", "DOUBLE");
		columns.put("NEWSPEND", "DOUBLE");
		columns.put("INCOME", "DOUBLE");
		columns.put("LOYALTY","DOUBLE");

		dataSource = new DataSource("PAL", "CUSTOMERS", columns);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGetLibraryName() {
		assertEquals("AFLPAL", algorithm.getLibraryName());
	}
	
	@Test
	public void testGetName() {
		assertEquals("KMEANS", algorithm.getName());
	}
	
	@Test
	public void testGetParams() {
		List<AlgorithmParameter> params = algorithm.getParams();
		assertEquals(7, params.size());
		assertEquals("THREAD_NUMBER", params.get(0).getName());
		assertEquals("EXIT_THRESHOLD", params.get(6).getName());
	}
	
	@Test
	public void testGetSignature() {
		AlgorithmSignature signature = algorithm.getSignature();
		assertEquals("PAL_T_KM_PARAMS", signature.getParamTableType().getTypeName());
		assertEquals("VARCHAR(100)", signature.getParamTableType().getColumns().get("STRINGARGS"));
	}
	
	@Test
	public void testSetSignature() {
		AlgorithmSignature signature = algorithm.getSignature();
		algorithm.setSignature(dataSource);
		
		assertEquals("PAL_T_KM_DATA", signature.getDataSourceType().getTypeName());
		assertEquals("INTEGER", signature.getDataSourceType().getColumns().get("ID"));
		
		assertEquals(2, signature.getResultTableTypes().size());
		assertEquals("PAL_T_KM_RESULTS", signature.getResultTableTypes().get(0).getTypeName());
		assertEquals(3, signature.getResultTableTypes().get(0).getColumns().size());
		assertEquals("PAL_T_KM_CENTERS", signature.getResultTableTypes().get(1).getTypeName());
		assertEquals(dataSource.getColumns().size(), signature.getResultTableTypes().get(1).getColumns().size());
	}

}
