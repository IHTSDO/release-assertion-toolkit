package org.ihtsdo.sql;

import java.io.File;
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
	private String archiveContent;
	
	public StatementExecutor(Connection con, SqlFileParser parser, String sqlDirectory) throws Exception {
		this.sqlDirectory = sqlDirectory;
		this.con = con;
		this.sqlParser = parser;
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
		
		archiveContent = sqlParser.parse(sqlFile);

		return execute(archiveContent);
	}

	public String getArchiveContent() {
		return archiveContent;
	}

	
	public ResultSet execute(String[] statements) throws SQLException {
		// Assumes Pre-Parsed
		Statement st = con.createStatement();
		if (statements != null && statements.length > 0) {
			for (int i = 0; i < statements.length; i++) {
				st.execute(statements[i]);
			}
		
			return st.getResultSet();
		}
		
		return null;
	}

	public boolean execute(String statement) throws SQLException {
		// Assumes Pre-Parsed
		if (statement != null && statement.length() > 0) {
			Statement st = con.createStatement();
			st.execute(statement);
			st.close();
			
			return true;
		}
		
		return false;
	}

}

