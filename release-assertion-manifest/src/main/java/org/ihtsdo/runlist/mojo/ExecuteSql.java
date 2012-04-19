package org.ihtsdo.runlist.mojo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.ihtsdo.runlist.RunListProcessor;
import org.ihtsdo.sql.StatementExecutor;
import org.ihtsdo.sql.parser.SqlFileParser;
import org.ihtsdo.xml.elements.Script;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 *
 * @goal execute-sql
 */
public class ExecuteSql extends AbstractMojo
{
    /**
     * Location of the runList.xml file
     * @parameter runlistFilePath = null
     * @required
     */
    private File runlistFile;

    /**
     * Location of the runList.xml file
     * @parameter runlistFilePath = null
     * @required
     */
    private File execProperties;

    /**
     * Location of the sql files
     * @parameter sqlDir = null
     * @required
     */
    private String sqlDirectory;

    /**
     * Location of the sql files
     * @parameter sqlDir = null
     * @required
     */
    private String executedSqlDirectory;

    /**
	 * dbConnection JDBC URL
	 * 
	 * @parameter
	 * @required
	 */
	private String url;

	/**
	 * dbConnection JDBC username
	 * 
	 * @parameter
	 * @required
	 */
	private String username;

	/**
	 * dbConnection JDBC password
	 * 
	 * @parameter
	 * @required
	 */
	private String password;

	/**
	 * dbConnection JDBC password
	 * 
	 * @parameter
	 * @required
	 */
	private boolean breakOnFailure;

	/**
	 * dbConnection JDBC password
	 * 
	 * @parameter
	 * @required
	 */
	private String databaseName;

	private Connection con = null;
    private SqlFileParser sqlParser = null;
	private StatementExecutor executor = null;
    private Script currentScript = new Script();
    
	public void execute() throws MojoExecutionException
    {
		ExecutionLogger logger = new ExecutionLogger();
		Date testingStartDate = logger.initializeProcess();
		
		try {
	    	RunListProcessor processor = new RunListProcessor(runlistFile);
	    	
	    	if (processor.containsScripts()) {
	    		initializeProcess();
				
				while (processor.hasNext()) {
					try {
						currentScript = processor.nextSqlFileName();
						long startTime = logger.initializeScript(currentScript);
	
			    		if (executor.execute(currentScript)) {
		        			archiveExecutedFiles(executor.getArchiveContent());
		        		}
			    		
			    		logger.finalizeScript(startTime);
		    	    } catch (Exception e ) {
		    	    	String errorMessage = "For file: " + currentScript.getSqlFile() + " have error: " + e.getMessage();
		    	    	
		    	    	archiveExecutedFiles(executor.getArchiveContent());
		    	    	
		    	    	if (breakOnFailure) {
		    	    		throw new MojoExecutionException(errorMessage);
		    	    	} else {
		    	    		logger.logError(errorMessage);
		    	    	}
					}
	        	}

	        	closeConnection();
	        }
	    } catch (Exception e) {
	    	logger.logError(e.getMessage());
        	throw new MojoExecutionException(e.getMessage());
	    }
	    
	    logger.finalizeProcess(testingStartDate);
   }

	private void initializeProcess() throws Exception {
		File targetSqlDirectory = new File(executedSqlDirectory);
		targetSqlDirectory.mkdir();

		createConnection(url, username, password);

		sqlParser = new SqlFileParser(execProperties, databaseName);
		executor = new StatementExecutor(con, sqlParser, sqlDirectory);
		
		ExecutionLogger.initializeRun(sqlParser);
	}
	
	private void archiveExecutedFiles(String statement) throws IOException {
		File targetCategoryDir = new File(executedSqlDirectory + File.separator + currentScript.getCategory());
		if (!targetCategoryDir.exists()) {
			targetCategoryDir.mkdir();
		}
		
		File executedFile = new File(executedSqlDirectory + File.separator + currentScript.getCategory() + File.separator + currentScript.getSqlFile());
		BufferedWriter writer = new BufferedWriter(new FileWriter(executedFile));
		
		writer.append(statement);
		writer.newLine();
		
		writer.flush();
		writer.close();
	}

	private void createConnection(String url, String username, String password) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		url = url + "?allowMultiQueries=true";
		
		con = DriverManager.getConnection(url,username,password);
	}
	
	public void closeConnection() throws SQLException {
		con.close();		
	}

}
