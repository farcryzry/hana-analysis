package hana.analysis.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class NbAlgorithm extends Algorithm {

	public NbAlgorithm() {
		this.name = "NBCTRAIN";
		this.procedureName = "PAL_NB";

		this.params = new ArrayList<AlgorithmParameter>();
		this.params.add(new AlgorithmParameter("THREAD_NUMBER",
				"Number of threads.", 2));
		this.params
				.add(new AlgorithmParameter(
						"IS_SPLIT_MODEL",
						"Indicates whether to split the string of the model. 0: does not split the model, Other value: splits the model. The maximum length of each unit is 5000.",
						0));
		this.params.add(new AlgorithmParameter("LAPLACE",
				"Enables or disables Laplace smoothing.", 0.01));

		this.signature = new AlgorithmSignature("PAL.PAL_NB_SIGNATURE",
				this.name);

		LinkedHashMap<String, String> ParamColumns = new LinkedHashMap<String, String>();
		ParamColumns.put("NAME", "VARCHAR(60)");
		ParamColumns.put("INTARGS", "INTEGER");
		ParamColumns.put("DOUBLEARGS", "DOUBLE");
		ParamColumns.put("STRINGARGS", "VARCHAR(100)");
		this.signature.setParamTableType(new TableType("PAL",
				"PAL_T_NB_PARAMS", ParamColumns));
	}

	@Override
	public void setSignature(DataSource dataSource) {
		this.signature.setDataSourceType(new TableType("PAL", "PAL_T_NB_DATA", dataSource.getColumns()));

		LinkedHashMap<String, String> resultColumns = new LinkedHashMap<String, String>();
		resultColumns.put("ID", "INTEGER");
		resultColumns.put("JSONMODEL", "VARCHAR(5000)");
		TableType resultType = new TableType("PAL", "PAL_T_NB_MODEL", resultColumns);

		this.signature.addResultTableType(resultType);
	}
}
