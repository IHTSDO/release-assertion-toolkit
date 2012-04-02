package org.ihtsdo.sql.processors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ihtsdo.sql.StatementExecutor;
import org.ihtsdo.sql.parser.SqlFileParser;

public class RunTableProcessor {
    final private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	final private String runTableSqlFileName = "initialize-run-id-table.sql";
	private File runTableSql;
	private String date;
	private String useStatement;
    
	public RunTableProcessor(String sqlDirectory, String useStatement) {
		this.useStatement = useStatement;
		Date d = new Date();
		date = "'" + dateFormatter.format(d) + "'";
		
		runTableSql = new File(sqlDirectory + File.separator + "special" + File.separator + runTableSqlFileName);
	}
	
	public String getRunId(SqlFileParser parser, StatementExecutor executor) throws IOException, SQLException {
		String initRunIdStatement = getStatement();
		
		String[] statements = parser.identifyStatements(initRunIdStatement);
		ResultSet results = executor.execute(statements);
		String s = null;
		
		if (results == null || !results.next()) {
			throw new IOException("Unable to calculate the run Id");
		} else {
			s = results.getString("max(runid)");
			results.close();
			return s;
		}
	}

	private String getStatement() throws IOException {
		String line = null;
		StringBuffer str = new StringBuffer();
		str.append(useStatement);

		BufferedReader reader = new BufferedReader(new FileReader(runTableSql));
		while ((line = reader.readLine()) != null) {
			str.append(line);
		}
		
		String statement = str.toString().toLowerCase().replace("<sysdate>", date);
		return statement;
	}

	public String getExecutionDate() {
		return date;
	}

}
