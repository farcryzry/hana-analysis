package hana.analysis.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class KnnAlgorithm extends Algorithm {

	public KnnAlgorithm() {
		this.name = "KNN";
		this.procedureName = "PAL_KNN";
		
		this.params = new ArrayList<AlgorithmParameter>();
		this.params.add(new AlgorithmParameter("THREAD_NUMBER", "Number of threads", 4));
		this.params.add(new AlgorithmParameter("ATTRIBUTE_NUM", "Number of attributes. Default value: 1", 2));
		this.params.add(new AlgorithmParameter("VOTING_TYPE", "Voting type: ¡ñ 0 = majority voting ¡ñ 1 (default) = distance-weighted voting", 0));
		this.params.add(new AlgorithmParameter("K_NEAREST_NEIGHBOURS", "Number of nearest neighbors (k).", 3));
		this.params.add(new AlgorithmParameter("METHOD", "Searching method. ¡ñ 0 (default) = Brute force searching ¡ñ 1 = KD-tree searching", 0));
		
		this.signature = new AlgorithmSignature("PAL.PAL_KNN_SIGNATURE", this.name);
		
		LinkedHashMap<String, String> ParamColumns = new LinkedHashMap<String, String>();
		ParamColumns.put("NAME", "VARCHAR(60)");
		ParamColumns.put("INTARGS", "INTEGER");
		ParamColumns.put("DOUBLEARGS", "DOUBLE");
		ParamColumns.put("STRINGARGS", "VARCHAR(100)");
		this.signature.setParamTableType(new TableType("PAL", "PAL_T_KNN_PARAMS", ParamColumns));
	}
	
	@Override
	public void setSignature(DataSource dataSource) {
		this.signature.setDataSourceType(new TableType("PAL", "PAL_T_KNN_DATA", dataSource.getColumns()));
		
		LinkedHashMap<String, String> classColumns = new LinkedHashMap<String, String>();
		classColumns.put("CUSTOMER_ID", "INTEGER");
		classColumns.put("LIFESPEND", "INTEGER");
		classColumns.put("NEWSPEND", "INTEGER");
		TableType classTableType = new TableType("PAL", "PAL_T_KNN_CLASS", classColumns);
		this.signature.setClassTableType(classTableType);
		
		
		LinkedHashMap<String, String> resultColumns = new LinkedHashMap<String, String>();
		resultColumns.put("CUSTOMER_ID", "INTEGER");
		resultColumns.put("GENDER", "INTEGER");
		TableType resultType = new TableType("PAL", "PAL_T_KNN_PREDICT", resultColumns);

		this.signature.addResultTableType(resultType);
	}
}
