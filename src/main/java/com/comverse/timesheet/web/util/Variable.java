package com.comverse.timesheet.web.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;

public class Variable {
	private String keys;
	private String values;
	private String types;
	public String getKeys() {
		return keys;
	}
	public void setKeys(String keys) {
		this.keys = keys;
	}
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	public Map<String, Object> getVariableMap() {
		Map<String, Object> vars = new HashMap<String, Object>();
		ConvertUtils.register(new DateConverter(), java.util.Date.class);
		if(StringUtils.isBlank(keys)) {
			return vars;
		}
		String [] arrayKeys = keys.split(",");
		String[] arrayValues = values.split(",");
		String [] arrayTypes = types.split(",");
		for (int i = 0; i < arrayKeys.length; i++) {
			String key = arrayKeys[i];
			String value = arrayValues[i];
			String type = arrayTypes[i];
			Class<?> c = Enum.valueOf(PropertyType.class, type).getValue(); 
			Object objectValue = ConvertUtils.convert(value, c);
			vars.put(key, objectValue);
		}
		return vars;
	}
}













