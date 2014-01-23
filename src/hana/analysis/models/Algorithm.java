package hana.analysis.models;

import java.util.*;

public abstract class Algorithm {
	
	private String libraryName = "AFLPAL";
	
	protected String name;
	protected List<AlgorithmParameter> params;
	protected AlgorithmSignature signature;
	protected String procedureName;
	
	public String getLibraryName() {
		return libraryName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getParamTableName(String schemaName) {
		return schemaName + "." + name + "_PARAM";
	}
	
	public String getProcedureName() {
		return procedureName;
	}
	
	public List<AlgorithmParameter> getParams() {
		return params;
	}
	
	public AlgorithmSignature getSignature() {
		return signature;
	}
	
	public abstract void setSignature(DataSource dataSource);
	
	public abstract String updateParams(Map<String, Object> map);

	public abstract String initParams();
	
}
