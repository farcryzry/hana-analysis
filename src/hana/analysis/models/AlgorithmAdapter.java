package hana.analysis.models;

import java.util.*;

public class AlgorithmAdapter implements IAlgorithmAdapter {
	private Algorithm algorithm;
	private List<String> resultTables;
	
	public AlgorithmAdapter(Algorithm algorithm) {
		this.algorithm = algorithm;
		this.resultTables = new ArrayList<String>();
	}
	
	public List<String> getResultTables() {
		return resultTables;
	}
	
	public AlgorithmSignature getSignature() {
		return this.algorithm.getSignature();
	}
	
	public String build(DataSource source) {
		
		algorithm.setSignature(source);
		
		StringBuilder sb = new StringBuilder();
		
		//-- PAL setup
		
		sb.append(SqlGenerator.setSchema("PAL"));
		
		List<TableType> allTypes = algorithm.getSignature().getAllTypes();
		for(TableType type : allTypes) {
			sb.append(type.drop());
			sb.append(type.create());
		}
		
		sb.append(getSignature().drop());
		sb.append(getSignature().create());
		
		List<Object> lstProcedureParam = new ArrayList<Object>();
		lstProcedureParam.add(algorithm.getProcedureName());
		sb.append(SqlGenerator.callProcedure("SYSTEM.AFL_WRAPPER_ERASER", lstProcedureParam));
		
		lstProcedureParam.clear();
		lstProcedureParam.add(algorithm.getProcedureName());
		lstProcedureParam.add(algorithm.getLibraryName());
		lstProcedureParam.add(algorithm.getName());
		lstProcedureParam.add(getSignature().getName());
		sb.append(SqlGenerator.callProcedure("SYSTEM.AFL_WRAPPER_GENERATOR", lstProcedureParam));
		
		//-- App setup
		
		sb.append(SqlGenerator.setSchema("PAL"));
		
		sb.append(SqlGenerator.drop("VIEW", source.getViewName()));
		sb.append(SqlGenerator.createView(source.getViewName(), source.getViewDefination()));
		sb.append(getSignature().getParamTableType().createTableByType(algorithm.getParamTableName()));
		
		int idx = 1;
		for(TableType type : getSignature().getResultTableTypes()) {
			sb.append(type.createTableByType(algorithm.getName() + "RESULT" + idx));
			idx ++;
		}
		
		sb.append(algorithm.initParams());
		
		System.out.println(sb);
		
		return sb.toString();
	}
	
	public String execute(DataSource source, Map<String, Object> params) {
		StringBuilder sb = new StringBuilder();
		
		//-- app runtime
		
		sb.append(SqlGenerator.setSchema("PAL"));
		
		if(params != null) {
			sb.append(algorithm.updateParams(params));
		}
		
		sb.append(getSignature().truncate());
	
		
		List<Object> lstProcedureParam = new ArrayList<Object>();
		lstProcedureParam.add(source.getViewName());
		lstProcedureParam.add(algorithm.getParamTableName());
		
		resultTables.clear();
		int idx = 1;
		for(@SuppressWarnings("unused") TableType type : getSignature().getResultTableTypes()) {
			String tableName = algorithm.getName() + "RESULT" + idx;
			lstProcedureParam.add(tableName);
			resultTables.add(tableName);
			idx ++;
		}
		
		sb.append(SqlGenerator.callProcedure("_SYS_AFL.PAL_KM", lstProcedureParam, true));
		
		System.out.println(sb);
		
		return sb.toString();
	}
}
