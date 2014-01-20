package hana.analysis.models;

public class AlgorithmParameter{
	
	private String name;
	private String description;
	private Object defaultValue;
	
	public AlgorithmParameter(String name, String description, Object defaultValue) {
		this.name = name;
		this.description = description;
		this.defaultValue = defaultValue;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public Object getDefaultValue() {
		return defaultValue;
	}
}
