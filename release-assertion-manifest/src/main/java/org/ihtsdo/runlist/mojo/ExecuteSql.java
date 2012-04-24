package org.ihtsdo.runlist.mojo;

import java.io.File;
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
	    	RunListProcessor processor = new RunListProcessor(sqlDirectory, runlistFile);
	    	
	    	if (processor.containsScripts()) {
	    		initializeProcess();
				
				while (processor.hasNext()) {
					try {
						File scriptFile = getScriptFile(processor, logger);
						
						if (scriptFile != null) {
							long startTime = logger.initializeScript(currentScript);
		
				    		if (executor.execute(currentScript, scriptFile)) {
					    		logger.finalizeScript(startTime);
			        		} else {
			        			logger.logError("Error executing script: " + currentScript.getSqlFile() + " (UUID: " + currentScript.getUuid().toString() + ")");
			        		}
						}
		    	    } catch (Exception e ) {
		    			executor.archiveExecutedFiles();
		    	    	
		    	    	String errorMessage = "For file: " + currentScript.getSqlFile() + " have error: " + e.getMessage();
		    	    	
		    	    	if (breakOnFailure) {
		    	    		throw new MojoExecutionException(errorMessage);
		    	    	} else {
		    	    		logger.logError("Jave error in executing script: " + currentScript.getSqlFile() + " (UUID: " + currentScript.getUuid().toString() + ") with JavaErrorMsg: " + errorMessage);
		    	    	}
					}
	        	}

	        	closeConnection();
	        }
	    } catch (Exception e) {
	    	logger.logError("Error processing RunList with error message:" + e.getMessage());
        	throw new MojoExecutionException(e.getMessage());
	    }
	    
	    logger.finalizeProcess(testingStartDate);
   }


	private File getScriptFile(RunListProcessor processor, ExecutionLogger logger) {
		currentScript = processor.getNextScript();

		if (currentScript.getCategory().length() == 0 || currentScript.getSqlFile().length() == 0) {
			logger.logError("Script definition for \"" + currentScript.getUuid() + "\" is invalid");
			return null;
		}

		File f = processor.getScriptFile(currentScript, logger);

		if (!f.exists()) {
			logger.logError("Script \"" + f.getAbsolutePath() + "\" doesn't exist (UUID: " + currentScript.getUuid().toString() + ")");
			return null;
		}
		
		return f;
	}

	private void initializeProcess() throws Exception {
		File targetSqlDirectory = new File(executedSqlDirectory);
		targetSqlDirectory.mkdir();

		createConnection(url, username, password);

		sqlParser = new SqlFileParser(execProperties);
		executor = new StatementExecutor(con, sqlParser, sqlDirectory, databaseName, executedSqlDirectory);
		
		ExecutionLogger.initializeRun(sqlParser);
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
