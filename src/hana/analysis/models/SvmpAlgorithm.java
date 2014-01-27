package hana.analysis.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class SvmpAlgorithm extends Algorithm {

	public SvmpAlgorithm() {
		this.name = "SVMPREDICT";
		this.procedureName = "PAL_SVP";

		this.params = new ArrayList<AlgorithmParameter>();
		this.params.add(new AlgorithmParameter("THREAD_NUMBER",
				"Number of threads.", 2));
		this.params
				.add(new AlgorithmParameter(
						"KERNEL_TYPE",
						"Kernel type: ¡ñ 0: LINEAR KERNEL ¡ñ 1: POLY KERNEL ¡ñ 2: RBF KERNEL ¡ñ 3: SIGMOID KERNEL Default value: 2",
						2));

		this.signature = new AlgorithmSignature("PAL.PAL_SVP_SIGNATURE",
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
		this.signature.setDataSourceType(new TableType("PAL", "PAL_T_SVP_DATA", dataSource.getColumns()));

		LinkedHashMap<String, String> modelColumns1 = new LinkedHashMap<String, String>();
		modelColumns1.put("NAME", "VARCHAR(50)");
		modelColumns1.put("VALUE", "DOUBLE");
		TableType modelTableType1 = new TableType("PAL", "PAL_T_SV_MODEL1", modelColumns1);
		this.signature.addModelTableType(modelTableType1);
		
		LinkedHashMap<String, String> modelColumns2 = new LinkedHashMap<String, String>();
		modelColumns2.put("ID", "INTEGER");
		modelColumns2.put("ALPHA", "DOUBLE");
		modelColumns2.put("POLICY", "INTEGER");
		modelColumns2.put("AGE", "INTEGER");
		modelColumns2.put("AMOUNT", "INTEGER");
		modelColumns2.put("OCCUPATION", "INTEGER");
		TableType modelTableType2 = new TableType("PAL", "PAL_T_SV_MODEL2", modelColumns2);
		this.signature.addModelTableType(modelTableType2);
		
		LinkedHashMap<String, String> resultColumns = new LinkedHashMap<String, String>();
		resultColumns.put("ID", "INTEGER");
		resultColumns.put("FRAUD", "VARCHAR(10)");
		TableType resultType = new TableType("PAL", "PAL_T_NBP_PREDICT", resultColumns);

		this.signature.addResultTableType(resultType);
	}
}

