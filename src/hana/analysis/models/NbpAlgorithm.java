package hana.analysis.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class NbpAlgorithm extends Algorithm {

	public NbpAlgorithm() {
		this.name = "NBCPREDICT";
		this.procedureName = "PAL_NBP";

		this.params = new ArrayList<AlgorithmParameter>();
		this.params.add(new AlgorithmParameter("THREAD_NUMBER", "Number of threads.", 2));

		this.signature = new AlgorithmSignature("PAL.PAL_NBP_SIGNATURE", this.name);

		LinkedHashMap<String, String> ParamColumns = new LinkedHashMap<String, String>();
		ParamColumns.put("NAME", "VARCHAR(60)");
		ParamColumns.put("INTARGS", "INTEGER");
		ParamColumns.put("DOUBLEARGS", "DOUBLE");
		ParamColumns.put("STRINGARGS", "VARCHAR(100)");
		this.signature.setParamTableType(new TableType("PAL", "PAL_T_NB_PARAMS", ParamColumns));
	}

	@Override
	public void setSignature(DataSource dataSource) {
		this.signature.setDataSourceType(new TableType("PAL", "PAL_T_NBP_DATA", dataSource.getColumns()));

		LinkedHashMap<String, String> modelColumns = new LinkedHashMap<String, String>();
		modelColumns.put("ID", "INTEGER");
		modelColumns.put("JSONMODEL", "VARCHAR(5000)");
		TableType modelTableType = new TableType("PAL", "PAL_T_NB_MODEL", modelColumns);
		this.signature.setModelTableType(modelTableType);
		
		LinkedHashMap<String, String> resultColumns = new LinkedHashMap<String, String>();
		resultColumns.put("ID", "INTEGER");
		resultColumns.put("FRAUD", "VARCHAR(10)");
		TableType resultType = new TableType("PAL", "PAL_T_NBP_PREDICT", resultColumns);

		this.signature.addResultTableType(resultType);
	}

}
