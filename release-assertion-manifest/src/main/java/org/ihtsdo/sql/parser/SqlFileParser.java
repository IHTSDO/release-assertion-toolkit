package org.ihtsdo.sql.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.ihtsdo.sql.StatementExecutor;
import org.ihtsdo.sql.processors.RunTableProcessor;
import org.ihtsdo.xml.elements.ExecProperties;
import org.ihtsdo.xml.elements.Property;

public class SqlFileParser {
	String currentReleaseDate;
	private HashMap<String, String> variableMap = new HashMap<String, String>();
	private String runId = "-1";
	private String dbName;
	
	private final String useStatement = "use ";
	
	public SqlFileParser(String databaseName) {
		this.dbName = databaseName;
	}
	
	public SqlFileParser(File execProperties, String databaseName) throws JAXBException {
		this.dbName = databaseName;
		
		JAXBContext jaxbContext = JAXBContext.newInstance(ExecProperties.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		ExecProperties properties = (ExecProperties) jaxbUnmarshaller.unmarshal(execProperties);
		
		for (Property prop : properties.getProperty()) {
			addToMap(prop.getVariable(), prop.getValue());
		}
	}
	
	public String parse(File sqlFile) throws IOException {
		String line = null;
		StringBuffer str = new StringBuffer();
		str.append(useStatement + dbName + ";");
		
		// Put into single file
		BufferedReader reader = new BufferedReader(new FileReader(sqlFile));
		
		int lineCount = 0;
		while ((line = reader.readLine()) != null) {
			str.append(line);
			str.append("\r\n");
			lineCount++;
		}
		
		if (lineCount > 0) {
			return parse(str.toString());
		} 
		
		return null;
	}
	
	public String parse(String statement) {
		// Prepare statement
		if (statement.length() > 0) {
			return replaceVariables(statement);
		}
		
		return null;
	}
	
	public String[] identifyStatements(String statementString) {
		String[] preProcessStatements = statementString.trim().split(";");
		String[] statements = new String[preProcessStatements.length];
		
		for (int i = 0; i < preProcessStatements.length; i++) {
			String s = preProcessStatements[i].trim();
			statements[i] = s + ";";
		}
		
		return statements;
	}

	private String replaceVariables(String statement) {
		for (String variable : variableMap.keySet()) {
			String value = variableMap.get(variable);

			statement = statement.replaceAll("<" + variable + ">", value);
		}

		return statement;
	}

	public void initializeRunId(StatementExecutor executor, String sqlDirectory) throws Exception {
		String prepend = useStatement + dbName + ";";

		RunTableProcessor processor = new RunTableProcessor(sqlDirectory, prepend);	
		
		runId  = processor.getRunId(this, executor);
		String executionDate = processor.getExecutionDate();
		
		updateVariableMap("runId", runId);
		updateVariableMap("executionDate", executionDate);
	}

	private void addToMap(String variable, String value) {
		variableMap.put(variable.toUpperCase(), value.toLowerCase());		
	}


	private void updateVariableMap(String variable, String value) throws Exception {
		if (variableMap.containsKey(variable)) {
			throw new Exception("Shouldn't assign duplicate variable date");
		}
		
		addToMap(variable, value);
	}

	public void updateVariables(String variable, String value) {
		addToMap(variable, value);
	}

	public String getRunId() {
		return runId;
	}
}
