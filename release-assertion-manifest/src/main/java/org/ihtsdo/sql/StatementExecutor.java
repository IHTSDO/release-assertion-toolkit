package org.ihtsdo.sql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.ihtsdo.sql.parser.SqlFileParser;
import org.ihtsdo.xml.elements.Script;

public class StatementExecutor {

	private Connection con = null;
	private SqlFileParser sqlParser;
	private String currentScriptContent;
	private String executedSqlDirectory;
	private Script currentScript;
	private String useStatement;
	private final String prependUseStatement = "use ";
	private ResultSet results = null;

	public StatementExecutor(Connection con, String dbName, String executedSqlDirectory) {
		this.con = con;
		this.sqlParser = new SqlFileParser();
		this.executedSqlDirectory = executedSqlDirectory;
		
		initUseStatement(dbName);
	}

	public StatementExecutor(Connection con, SqlFileParser parser, String sqlDirectory, String dbName, String executedSqlDirectory) throws Exception {
		this.con = con;
		this.sqlParser = parser;
		this.executedSqlDirectory = executedSqlDirectory;
		
		initUseStatement(dbName);
		sqlParser.initializeRunId(this, sqlDirectory);
	}

	public boolean execute(Script script, File sqlFile) throws SQLException, IOException {
		return execute(script, sqlFile, null);
	}
	
	public boolean execute(Script script, File sqlFile, String queryTimeOut) throws SQLException, IOException {
		sqlParser.updateVariables("assertionText", script.getText());
		sqlParser.updateVariables("assertionUuid", script.getUuid());

		currentScriptContent = sqlParser.parse(sqlFile);
		currentScript = script;
		boolean successfulExec;
		
		if(queryTimeOut != null){
			successfulExec = execute(currentScriptContent, queryTimeOut);
		}else {
			successfulExec = execute(currentScriptContent);
		}
		archiveExecutedFiles();
		
		return successfulExec;
	}

	public boolean execute(File script) throws SQLException, IOException {
		return execute(script, null);
	}
	
	
	public boolean execute(File script, String queryTimeOut) throws SQLException, IOException {
		if (!script.exists()) {
			return false;
		}

		currentScript = new Script();
		currentScript.setCategory("special");
		currentScript.setSqlFile(script.getName());

		currentScriptContent = sqlParser.parse(script);
		boolean successfulExec;
		
		// Checking queryTimeOut
		if (queryTimeOut != null) {		
			successfulExec = execute(currentScriptContent, queryTimeOut);
		}else{
			successfulExec = execute(currentScriptContent);
		}
		archiveExecutedFiles();
		
		return successfulExec;
	}

	public ResultSet execute(String[] statements, String scriptName) throws SQLException, IOException {
		return execute(statements, scriptName, null);
	}


	public ResultSet execute(String[] statements, String scriptName, String queryTimeOut) throws SQLException, IOException {
		// Assumes Pre-Parsed
		StringBuffer currentScriptStr = new StringBuffer();
		currentScript = new Script();
		currentScript.setCategory("special");
		currentScript.setSqlFile(scriptName);
		boolean successfulExec = false;

		if (statements != null && statements.length > 0) {
			for (int i = 0; i < statements.length; i++) {
				
				// Checking queryTimeOut
				if (queryTimeOut != null) {		
					successfulExec = execute(statements[i], queryTimeOut);
				} else {
					successfulExec = execute(statements[i]);
				}
				
				currentScriptStr.append(statements[i]);
				currentScriptStr.append("\r\n");
				
				// If unsuccessful execution on any of the statements in the array, stop instantly
				if (successfulExec == false) {
					break;					
				}
			}

			currentScriptContent = currentScript.toString();
			archiveExecutedFiles();
		}
		
		if (successfulExec) {		
			return getResultSet();
		} else {
			return null;
		}
	}

	private boolean execute(String statement) throws SQLException {
		return execute(statement, null);
	}

	private boolean execute(String statement, String queryTimeOut) throws SQLException {
		// Assumes Pre-Parsed
		if (statement != null && statement.length() > 0) {
			Statement st = con.createStatement();
			
			// Setting queryTimeOut
			if (queryTimeOut != null) {			
				st.setQueryTimeout(Integer.parseInt(queryTimeOut));
			}
			
			st.execute(useStatement + statement);
			results  = st.getResultSet();
			st.close();
			
			return true;
		}
		
		return false;
	}
	
	public void archiveExecutedFiles() throws IOException {
		if (currentScript != null) {
			File targetCategoryDir = new File(executedSqlDirectory + File.separator + currentScript.getCategory());
			if (!targetCategoryDir.exists()) {
				targetCategoryDir.mkdirs();
			}
	
			File executedFile = new File(executedSqlDirectory + File.separator + currentScript.getCategory() + File.separator + currentScript.getSqlFile());
			BufferedWriter writer = new BufferedWriter(new FileWriter(executedFile));
	
			writer.append(useStatement + currentScriptContent);
			writer.newLine();
	
			writer.flush();
			writer.close();
		}
	}

	public ResultSet getResultSet() {
		ResultSet retVal = results;
		results = null;
		
		return retVal;
	}
	
	private void initUseStatement(String dbName) {
		StringBuffer str = new StringBuffer();
		str.append(prependUseStatement + dbName + ";");
		str.append("\r\n");
		str.append("\r\n");
		
		useStatement = str.toString();
	}
}

