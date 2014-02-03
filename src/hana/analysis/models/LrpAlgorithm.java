package hana.analysis.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class LrpAlgorithm extends Algorithm {

	public LrpAlgorithm() {
		this.name = "FORECASTWITHLOGISTICR";
		this.procedureName = "PAL_RGP";

		this.params = new ArrayList<AlgorithmParameter>();
		this.params.add(new AlgorithmParameter("THREAD_NUMBER", "Number of threads.", 2));

		this.signature = new AlgorithmSignature("PAL.PAL_RGP_SIGNATURE", this.name);

		LinkedHashMap<String, String> ParamColumns = new LinkedHashMap<String, String>();
		ParamColumns.put("NAME", "VARCHAR(60)");
		ParamColumns.put("INTARGS", "INTEGER");
		ParamColumns.put("DOUBLEARGS", "DOUBLE");
		ParamColumns.put("STRINGARGS", "VARCHAR(100)");
		this.signature.setParamTableType(new TableType("PAL", "PAL_T_RG_PARAMS", ParamColumns));
	}

	@Override
	public void setSignature(DataSource dataSource) {
		this.signature.setDataSourceType(new TableType("PAL", "PAL_T_RGP_DATA", dataSource.getColumns()));

		LinkedHashMap<String, String> modelColumns = new LinkedHashMap<String, String>();
		modelColumns.put("ID", "INTEGER");
		modelColumns.put("JSONMODEL", "VARCHAR(5000)");	
		TableType modelTableType = new TableType("PAL", "PAL_T_RG_COEFF", modelColumns);
		this.signature.addModelTableType(modelTableType);
		
		LinkedHashMap<String, String> resultColumns = new LinkedHashMap<String, String>();
		resultColumns.put("ID", "INTEGER");
		resultColumns.put("FITTED", "DOUBLE");
		resultColumns.put("GENDER", "INTEGER");
		TableType resultType = new TableType("PAL", "PAL_T_RGP_FITTED", resultColumns);

		this.signature.addResultTableType(resultType);
	}

}

