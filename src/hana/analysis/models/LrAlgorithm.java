package hana.analysis.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class LrAlgorithm extends Algorithm {

	public LrAlgorithm() {
		this.name = "LOGISTICREGRESSION";
		this.procedureName = "PAL_RG";

		this.params = new ArrayList<AlgorithmParameter>();
		this.params.add(new AlgorithmParameter("THREAD_NUMBER",
				"Number of threads.", 4));
		this.params
				.add(new AlgorithmParameter(
						"MAX_ITERATION",
						"Indicates whether to split the string of the model. 0: does not split the model, Other value: splits the model. The maximum length of each unit is 5000.",
						100));
		this.params.add(new AlgorithmParameter("EXIT_THRESHOLD",
				"Threshold (actual value) for exiting the iterations. Default value: 0.000001", 0.00001));
		this.params.add(new AlgorithmParameter("VARIABLE_NUM",
				"(Optional) Specifies the number of independent variables (Xi). Default value: all Xi columns in DataTab", 1));
		this.params.add(new AlgorithmParameter("METHOD",
				"¡ñ 0 (default and recommended): uses the Newton iteration method. ¡ñ 1: uses the gradient-decent method. ¡ñ 2: uses the L1 regularized Newton iteration method. ¡ñ 3: uses the BFGS method.", 0));
		this.params.add(new AlgorithmParameter("PMML_EXPORT",
				"0 (default): does not export logistic regression model in PMML.¡ñ 1: exports logistic regression model in PMML in single row.¡ñ 2: exports logistic regression model in PMML in several rows, and the minimum length of each row is 5000 characters.", 2));

		this.signature = new AlgorithmSignature("PAL.PAL_RG_SIGNATURE",
				this.name);

		LinkedHashMap<String, String> ParamColumns = new LinkedHashMap<String, String>();
		ParamColumns.put("NAME", "VARCHAR(60)");
		ParamColumns.put("INTARGS", "INTEGER");
		ParamColumns.put("DOUBLEARGS", "DOUBLE");
		ParamColumns.put("STRINGARGS", "VARCHAR(100)");
		this.signature.setParamTableType(new TableType("PAL",
				"PAL_T_RG_PARAMS", ParamColumns));
	}

	@Override
	public void setSignature(DataSource dataSource) {
		this.signature.setDataSourceType(new TableType("PAL", "PAL_T_RG_DATA",
				dataSource.getColumns()));

		LinkedHashMap<String, String> resultColumns1 = new LinkedHashMap<String, String>();
		resultColumns1.put("ID", "INTEGER");
		resultColumns1.put("AI", "DOUBLE");
		TableType resultType1 = new TableType("PAL", "PAL_T_RG_COEFF",
				resultColumns1);

		this.signature.addResultTableType(resultType1);
		
		LinkedHashMap<String, String> resultColumns2 = new LinkedHashMap<String, String>();
		resultColumns2.put("ID", "INTEGER");
		resultColumns2.put("PMML", "VARCHAR(5000)");
		TableType resultType2 = new TableType("PAL", "PAL_T_RG_PMML",
				resultColumns2);

		this.signature.addResultTableType(resultType2);
	}
}
