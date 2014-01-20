package hana.analysis.models;

import java.util.*;

public class KmeansAlgorithm extends Algorithm {
	
	public KmeansAlgorithm() {
		this.name = "KMEANS";
		this.procedureName = "PAL_KM";
		
		this.params = new ArrayList<AlgorithmParameter>();
		this.params.add(new AlgorithmParameter("THREAD_NUMBER", "", 2));
		this.params.add(new AlgorithmParameter("GROUP_NUMBER", "", 3));
		this.params.add(new AlgorithmParameter("INIT_TYPE", "", 1));
		this.params.add(new AlgorithmParameter("DISTANCE_LEVEL", "", 2));
		this.params.add(new AlgorithmParameter("MAX_ITERATION", "", 100));
		this.params.add(new AlgorithmParameter("NORMALIZATION", "", 0));
		this.params.add(new AlgorithmParameter("EXIT_THRESHOLD", "", 0.0001));
		
		this.signature = new AlgorithmSignature("PAL.PAL_KM_SIGNATURE", this.name);
		
		LinkedHashMap<String, String> ParamColumns = new LinkedHashMap<String, String>();
		ParamColumns.put("NAME", "VARCHAR(60)");
		ParamColumns.put("INTARGS", "INTEGER");
		ParamColumns.put("DOUBLEARGS", "DOUBLE");
		ParamColumns.put("STRINGARGS", "VARCHAR(100)");
		this.signature.setParamTableType(new TableType("PAL", "PAL_T_KM_PARAMS", ParamColumns));
	}
	
	@Override
	public void setSignature(DataSource dataSource) {
		this.signature.setDataSourceType(new TableType("PAL", "PAL_T_KM_DATA", dataSource.getColumns()));
		
		LinkedHashMap<String, String> resultColumns = new LinkedHashMap<String, String>();
		resultColumns.put("ID", "INTEGER");
		resultColumns.put("CENTER_ID", "INTEGER");
		resultColumns.put("DISTANCE", "DOUBLE");
		TableType resultType = new TableType("PAL", "PAL_T_KM_RESULTS", resultColumns);
		
		LinkedHashMap<String, String> centerColumns = new LinkedHashMap<String, String>();
		centerColumns.put("CENTER_ID", "INTEGER");
		for(String key : this.signature.getDataSourceType().getColumns().keySet()){
			if(!key.equals("ID")) {
				centerColumns.put(key, this.signature.getDataSourceType().getColumns().get(key));
			}
		}
		TableType centerType = new TableType("PAL", "PAL_T_KM_CENTERS", centerColumns);

		this.signature.addResultTableType(resultType);
		this.signature.addResultTableType(centerType);
	}
	
	@Override
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
			
			sql += SqlGenerator.insert(getParamTableName(), values);
		}
		
		return sql;		
	}

	@Override
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
					
				sql += SqlGenerator.update(getParamTableName(), setClause, conditionClause);
			}
		}
		
		return sql;
	}
	
}
