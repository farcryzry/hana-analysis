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
	
	public String initParams() {
		String sql = "";
		
		for(AlgorithmParameter param : params) {
			Object [] values = new Object [] {param.getName(), null, null, null};
			Object defaultValue = param.getDefaultValue();
			
			if(defaultValue instanceof String) { 
				values[3] = defaultValue;
			} else if(defaultValue instanceof Double || defaultValue instanceof Float) { 
				values[2] = defaultValue;
			} else if(defaultValue instanceof Integer) {
				values[1] = defaultValue;
			}
			
			sql += SqlGenerator.insert(getParamTableName("PAL"), values);
		}
		
		return sql;		
	}
	
	public String updateParams(Map<String, Object> map) {
		String sql = "";
		
		for(AlgorithmParameter param : getParams()) {
			String paramName = param.getName();	
			
			if(map.containsKey(paramName)) {
				Object value = map.get(paramName);
				String conditionClause = "NAME = '" + paramName + "'";
				String setClause = "";
					if(value instanceof String) { 
						setClause = "STRINGARGS='" + value + "'";
					} else if(value instanceof Double || value instanceof Float) { 
						setClause = "DOUBLEARGS=" + value;
					} else if(value instanceof Integer) {
						setClause = "INTARGS=" + value;
					} else {
						setClause = "STRINGARGS='" + value + "'";
					}
					
				sql += SqlGenerator.update(getParamTableName("PAL"), setClause, conditionClause);
			}
		}
		
		return sql;
	}
	
	public abstract void setSignature(DataSource dataSource);

}
