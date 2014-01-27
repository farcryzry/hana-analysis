package hana.analysis.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class SvmAlgorithm extends Algorithm {

	public SvmAlgorithm() {
		this.name = "SVMTRAIN";
		this.procedureName = "PAL_SV";

		this.params = new ArrayList<AlgorithmParameter>();
		this.params.add(new AlgorithmParameter("THREAD_NUMBER",
				"Number of threads.", 2));
		this.params
				.add(new AlgorithmParameter(
						"KERNEL_TYPE",
						"Kernel type: ¡ñ 0: LINEAR KERNEL ¡ñ 1: POLY KERNEL ¡ñ 2: RBF KERNEL ¡ñ 3: SIGMOID KERNEL Default value: 2",
						2));
		this.params.add(new AlgorithmParameter("TYPE",
				"SVM type: ¡ñ 1: SVC (for classification) ¡ñ 2: SVR (for regression) ¡ñ 3: Support Vector Ranking (for ranking)", 
				1));
		this.params.add(new AlgorithmParameter("POLY_DEGREE",
				"Coefficient for the PLOY KERNEL type. Value range: ¡Ý 1 Default value: 3", 
				3));
		this.params.add(new AlgorithmParameter("RBF_GAMMA",
				"Coefficient for the RBF KERNEL type. Value range: > 0 Default value: 1.0", 
				1.0));
		this.params.add(new AlgorithmParameter("COEF_LIN",
				"Coefficient for the POLY/SIGMOID KERNEL type. Default value: 1.0", 
				1.0));
		this.params.add(new AlgorithmParameter("COEF_CONST",
				"Coefficient for the POLY/SIGMOID KERNEL type. Default value: 1.0", 
				1.0));
		this.params.add(new AlgorithmParameter("SVM_C",
				"Trade-off between training error and margin. Value range: > 0 Default value: 1.0", 
				1.0));
		this.params.add(new AlgorithmParameter("REGRESSION_EPS",
				"Epsilon width of tube for regression. Value range: > 0 Default value: 1.0", 
				1.0));
		this.params.add(new AlgorithmParameter("CROSS_VALIDATION",
				"Specifies whether to use cross validation. ¡ñ 0 (default): does not use cross validation ¡ñ 1: uses cross validation. Other parameters will be ignored.", 
				0));
		this.params.add(new AlgorithmParameter("NR_FOLD",
				"Specifies how many portions the training data will be divided into in cross validation. Value range: ¡Ý 2 Default value: 3", 
				3));

		this.signature = new AlgorithmSignature("PAL.PAL_SV_SIGNATURE",
				this.name);

		LinkedHashMap<String, String> ParamColumns = new LinkedHashMap<String, String>();
		ParamColumns.put("NAME", "VARCHAR(60)");
		ParamColumns.put("INTARGS", "INTEGER");
		ParamColumns.put("DOUBLEARGS", "DOUBLE");
		ParamColumns.put("STRINGARGS", "VARCHAR(100)");
		this.signature.setParamTableType(new TableType("PAL",
				"PAL_T_SV_PARAMS", ParamColumns));
	}

	@Override
	public void setSignature(DataSource dataSource) {
		this.signature.setDataSourceType(new TableType("PAL", "PAL_T_SV_DATA", dataSource.getColumns()));

		LinkedHashMap<String, String> resultColumns1 = new LinkedHashMap<String, String>();
		resultColumns1.put("NAME", "VARCHAR(50)");
		resultColumns1.put("VALUE", "DOUBLE");
		TableType resultType1 = new TableType("PAL", "PAL_T_SV_MODEL1", resultColumns1);
		
		LinkedHashMap<String, String> resultColumns2 = new LinkedHashMap<String, String>();
		resultColumns2.put("ID", "INTEGER");
		resultColumns2.put("ALPHA", "DOUBLE");
		resultColumns2.put("POLICY", "INTEGER");
		resultColumns2.put("AGE", "INTEGER");
		resultColumns2.put("AMOUNT", "INTEGER");
		resultColumns2.put("OCCUPATION", "INTEGER");
		TableType resultType2 = new TableType("PAL", "PAL_T_SV_MODEL2", resultColumns2);

		this.signature.addResultTableType(resultType1);
		this.signature.addResultTableType(resultType2);
	}
}
