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
	private String sqlDirectory;
	private String currentScriptContent;
	private String executedSqlDirectory;
	private Script currentScript;
	private String useStatement;
	private final String prependUseStatement = "use ";

	public StatementExecutor(Connection con, String dbName, String executedSqlDirectory) {
		this.con = con;
		this.sqlParser = new SqlFileParser();
		this.executedSqlDirectory = executedSqlDirectory;
		
		initUseStatement(dbName);
	}

	public StatementExecutor(Connection con, SqlFileParser parser, String sqlDirectory, String dbName, String executedSqlDirectory) throws Exception {
		this.sqlDirectory = sqlDirectory;
		this.con = con;
		this.sqlParser = parser;
		this.executedSqlDirectory = executedSqlDirectory;
		
		initUseStatement(dbName);
		sqlParser.initializeRunId(this, sqlDirectory);
	}
	 
	public boolean execute(Script script) throws SQLException, IOException {
		if (script.getCategory().length() == 0 || script.getSqlFile().length() == 0) {
			return false;
		}
		
		File sqlFile = new File(sqlDirectory + File.separator + script.getCategory() + File.separator + script.getSqlFile());
		
		if (!sqlFile.exists()) {
			return false;
		}

		sqlParser.updateVariables("assertionText", script.getText());
		sqlParser.updateVariables("assertionUuid", script.getUuid());

		currentScriptContent = sqlParser.parse(sqlFile);
		currentScript = script;

		boolean successfulExec = execute(currentScriptContent);
		archiveExecutedFiles();
		
		return successfulExec;
	}

	public boolean execute(File script) throws SQLException, IOException {
		if (!script.exists()) {
			return false;
		}

		currentScript = new Script();
		currentScript.setCategory("special");
		currentScript.setSqlFile(script.getName());

		currentScriptContent = sqlParser.parse(script);
		
		boolean successfulExec = execute(currentScriptContent);
		archiveExecutedFiles();
		
		return successfulExec;
	}

	public ResultSet execute(String[] statements, String scriptName) throws SQLException, IOException {
		// Assumes Pre-Parsed
		currentScript = new Script();
		currentScript.setCategory("special");
		currentScript.setSqlFile(scriptName);

		StringBuffer currentScript = new StringBuffer();
		Statement st = con.createStatement();
		if (statements != null && statements.length > 0) {
			for (int i = 0; i < statements.length; i++) {
				st.execute(statements[i]);
				currentScript.append(statements[i]);
				currentScript.append("\r\n");
			}

			currentScriptContent = currentScript.toString();
			archiveExecutedFiles();

			return st.getResultSet();
		}
		
		return null;
	}

	private boolean execute(String statement) throws SQLException {
		// Assumes Pre-Parsed
		if (statement != null && statement.length() > 0) {
			Statement st = con.createStatement();
			st.execute(useStatement + statement);
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

	private void initUseStatement(String dbName) {
		StringBuffer str = new StringBuffer();
		str.append(prependUseStatement + dbName + ";");
		str.append("\r\n");
		str.append("\r\n");
		
		useStatement = str.toString();
	}
}

