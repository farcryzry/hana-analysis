package hana.analysis.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MlrpAlgorithm extends Algorithm {

	public MlrpAlgorithm() {
		this.name = "FORECASTWITHLR";
		this.procedureName = "PAL_MLRGP";

		this.params = new ArrayList<AlgorithmParameter>();
		this.params.add(new AlgorithmParameter("THREAD_NUMBER", "Number of threads.", 2));

		this.signature = new AlgorithmSignature("PAL.PAL_MLRG_SIGNATURE", this.name);

		LinkedHashMap<String, String> ParamColumns = new LinkedHashMap<String, String>();
		ParamColumns.put("NAME", "VARCHAR(60)");
		ParamColumns.put("INTARGS", "INTEGER");
		ParamColumns.put("DOUBLEARGS", "DOUBLE");
		ParamColumns.put("STRINGARGS", "VARCHAR(100)");
		this.signature.setParamTableType(new TableType("PAL", "PAL_T_MLRG_PARAMS", ParamColumns));
	}

	@Override
	public void setSignature(DataSource dataSource) {
		this.signature.setDataSourceType(new TableType("PAL", "PAL_T_MLRGP_DATA", dataSource.getColumns()));

		LinkedHashMap<String, String> classColumns = new LinkedHashMap<String, String>();
		classColumns.put("ID", "INTEGER");
		classColumns.put("AI", "DOUBLE");	
		TableType classTableType = new TableType("PAL", "PAL_T_MLRG_COEFF", classColumns);
		this.signature.setClassTableType(classTableType);
		
		LinkedHashMap<String, String> resultColumns = new LinkedHashMap<String, String>();
		resultColumns.put("ID", "INTEGER");
		resultColumns.put("FITTED", "DOUBLE");
		TableType resultType = new TableType("PAL", "PAL_T_MLRGP_FITTED", resultColumns);

		this.signature.addResultTableType(resultType);
	}

}

