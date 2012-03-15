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

@SuppressWarnings("restriction")
public class SqlFileParser {
	String currentReleaseDate;
	String[] archiveContent = null;
	private HashMap<String, String> variableMap = new HashMap<String, String>();
	private String runId = "-1";
	
	public SqlFileParser(File execProperties) throws JAXBException {
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
		
		// Put into single file
		BufferedReader reader = new BufferedReader(new FileReader(sqlFile));
		
		int lineCount = 0;
		while ((line = reader.readLine()) != null) {
			str.append(line);
			lineCount++;
		}
		
		if (lineCount > 0) {
			String statement = parse(str.toString());
			
			// Prepare archived file
			reader.close();
			reader = new BufferedReader(new FileReader(sqlFile));
			prepareArchiveContent(reader, lineCount);
			
			return statement;
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

	private void prepareArchiveContent(BufferedReader reader, int totalLines) 
		throws IOException 
	{
		int lineCount = 0;
		String line = null;
		archiveContent = new String[totalLines];
		
		while ((line = reader.readLine()) != null) {
			archiveContent[lineCount++] = replaceVariables(line);
		}
		
		reader.close();
	}
	
	public String[] getArchiveContent() {
		return archiveContent;
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
		RunTableProcessor processor = new RunTableProcessor(sqlDirectory);	
		
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