package hana.analysis.models;

import java.util.*;

public class AlgorithmAdapter implements IAlgorithmAdapter {
	private Algorithm algorithm;
	private List<String> resultTables;
	private String schemaName;
	
	public AlgorithmAdapter(Algorithm algorithm, String schemaName) {
		this.algorithm = algorithm;
		this.resultTables = new ArrayList<String>();
		this.schemaName = schemaName;
	}
	
	public List<String> getResultTables() {
		return resultTables;
	}
	
	public Algorithm getAlgorithm() {
		return algorithm;
	}
	
	public AlgorithmSignature getSignature() {
		return this.algorithm.getSignature();
	}
	
	public String build(DataSource source) {
		
		algorithm.setSignature(source);
		
		StringBuilder sb = new StringBuilder();
		
		//-- PAL setup
		
		sb.append(SqlGenerator.setSchema(schemaName));
		
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
		
		sb.append(SqlGenerator.setSchema(schemaName));
		
		sb.append(SqlGenerator.drop("VIEW", source.getViewName()));
		sb.append(SqlGenerator.createView(source.getViewName(), source.getViewDefination()));
		sb.append(getSignature().getParamTableType().createTableByType(algorithm.getParamTableName(this.schemaName)));
		
		int idx = 1;
		for(TableType type : getSignature().getResultTableTypes()) {
			sb.append(type.createTableByType(type.getSchemaName() + "." + algorithm.getName() + "RESULT" + idx));
			idx ++;
		}
		
		sb.append(algorithm.initParams());
		
		System.out.println(sb);
		
		return sb.toString();
	}
	
	public String execute(DataSource source, Map<String, Object> params) {
		StringBuilder sb = new StringBuilder();
		
		//-- app runtime
		
		sb.append(SqlGenerator.setSchema(schemaName));
		
		if(params != null) {
			sb.append(algorithm.updateParams(params));
		}
		
		sb.append(getSignature().truncate(schemaName));
	
		
		List<Object> lstProcedureParam = new ArrayList<Object>();
		lstProcedureParam.add(source.getViewName());
		lstProcedureParam.add(algorithm.getParamTableName(this.schemaName));
		
		resultTables.clear();
		int idx = 1;
		for(TableType type : getSignature().getResultTableTypes()) {
			String tableName = type.schemaName + "." + algorithm.getName() + "RESULT" + idx;
			lstProcedureParam.add(tableName);
			resultTables.add(tableName);
			idx ++;
		}
		
		sb.append(SqlGenerator.callProcedure("_SYS_AFL." + algorithm.getProcedureName(), lstProcedureParam, true));
		
		System.out.println(sb);
		
		return sb.toString();
	}
}
