package hana.analysis.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MlrAlgorithm extends Algorithm {

	public MlrAlgorithm() {
		this.name = "LRREGRESSION";
		this.procedureName = "PAL_MLRG";

		this.params = new ArrayList<AlgorithmParameter>();
		this.params.add(new AlgorithmParameter("THREAD_NUMBER",
				"Number of threads.", 2));
		this.params
				.add(new AlgorithmParameter(
						"VARIABLE_NUM",
						"(Optional) Specifies the number of independent variables (Xi). ¡ñ Default value: all Xi columns in trainingDataTab ¡ñ Customized value: first n Xi in trainingDataTab ",
						3));
		this.params.add(new AlgorithmParameter("PMML_EXPORT",
				"0 (default): does not export multiple linear regression model in PMML. ¡ñ 1: exports multiple linear regression model in PMML in single row. ¡ñ 2: exports multiple linear regression model in PMML in several rows, and the minimum length of each row is 5000 characters.", 2));
		this.signature = new AlgorithmSignature("PAL.PAL_MLRG_SIGNATURE",
				this.name);

		LinkedHashMap<String, String> ParamColumns = new LinkedHashMap<String, String>();
		ParamColumns.put("NAME", "VARCHAR(60)");
		ParamColumns.put("INTARGS", "INTEGER");
		ParamColumns.put("DOUBLEARGS", "DOUBLE");
		ParamColumns.put("STRINGARGS", "VARCHAR(100)");
		this.signature.setParamTableType(new TableType("PAL",
				"PAL_T_MLRG_PARAMS", ParamColumns));
	}

	@Override
	public void setSignature(DataSource dataSource) {
		this.signature.setDataSourceType(new TableType("PAL", "PAL_T_MLRG_DATA",
				dataSource.getColumns()));

		LinkedHashMap<String, String> resultColumns1 = new LinkedHashMap<String, String>();
		resultColumns1.put("ID", "INTEGER");
		resultColumns1.put("AI", "DOUBLE");
		TableType resultType1 = new TableType("PAL", "PAL_T_MLRG_COEFF",
				resultColumns1);
		this.signature.addResultTableType(resultType1);
		
		LinkedHashMap<String, String> resultColumns2 = new LinkedHashMap<String, String>();
		resultColumns2.put("ID", "INTEGER");
		resultColumns2.put("FITTED", "DOUBLE");
		TableType resultType2 = new TableType("PAL", "PAL_T_MLRG_FITTED",
				resultColumns2);

		this.signature.addResultTableType(resultType2);
		
		LinkedHashMap<String, String> resultColumns3 = new LinkedHashMap<String, String>();
		resultColumns3.put("NAME", "VARCHAR(50)");
		resultColumns3.put("VALUE", "DOUBLE");
		TableType resultType3 = new TableType("PAL", "PAL_T_MLRG_SIGNIFICANCE",
				resultColumns3);

		this.signature.addResultTableType(resultType3);
		
		LinkedHashMap<String, String> resultColumns4 = new LinkedHashMap<String, String>();
		resultColumns4.put("ID", "INTEGER");
		resultColumns4.put("PMML", "VARCHAR(5000)");
		TableType resultType4 = new TableType("PAL", "PAL_T_MLRG_PMML",
				resultColumns4);
		this.signature.addResultTableType(resultType4);
	}
}

