package org.release.database.setup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

/**
 * @author Varsha Parekh
 * 
 * @goal import-releasefile-to-database
 * @requiresDependencyResolution compile
 */

public class ImportFileToDBMojo extends AbstractMojo {
	
	private static Logger logger = Logger.getLogger(ImportFileToDBMojo.class);


	/**
	 * Location of the build directory.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File targetDirectory;
	
	
	private Connection con;
	
	
	/**
	 * Files
	 * 
	 * @parameter
	 * @required
	 */
	private ArrayList<LoadFileParameter> importConfig;

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
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			String importSuccess;
			
			createConnection();
			logger.info("Database Connection Created...");
			
			logger.info("File Import Started ...");
			
			for (int f = 0; f < importConfig.size(); f++) {
				File loadReleaseFileName = importConfig.get(f).loadReleaseFileName;
				String loadDBTableName = importConfig.get(f).loadDBTableName;
				
				logger.info(loadReleaseFileName + " & " + loadDBTableName);
				
				loadFileToDatabase(loadReleaseFileName , loadDBTableName);
				int fileCount = checkFileCount(loadReleaseFileName);
				importSuccess = checkImportCount(loadDBTableName , fileCount);
				
				if(importSuccess.equals("true")){					
					logger.info("File is imported successfully into table.");
				}else{
					logger.error("File is not imported successfully into table " + loadDBTableName);
				}
			}
			
			logger.info("File Import Finished...");
			
			closeConnection();
			logger.info("Database Connection Closed...");
			
		}catch(SQLException sq){ 
			System.out.println(sq.getMessage());
			logger.info(sq.getMessage());
		}catch(FileNotFoundException fn){
			System.out.println(fn.getMessage());
			logger.info(fn.getMessage());
		}catch (Exception e) {
			throw new MojoFailureException(e.getLocalizedMessage(), e);
		}
	}
	
	private void loadFileToDatabase(File filename , String tablename) throws SQLException, FileNotFoundException  {
		String fileName = filename.toString().replace('\\', '/');
							
		Statement statement = (com.mysql.jdbc.Statement)con.createStatement();
		statement.execute("SET UNIQUE_CHECKS=0; ");
		
		String disableKey ="ALTER TABLE " + tablename + " DISABLE KEYS";
		statement.execute(disableKey);
		
		String truncateTable ="TRUNCATE TABLE " + tablename ;
		con.createStatement().execute(truncateTable);			
		
		// Define the query we are going to execute
		String statementText = "LOAD DATA LOCAL INFILE '" + fileName + "' " +
		"INTO TABLE " + tablename +  " FIELDS TERMINATED BY '\\t' LINES TERMINATED BY '\\r\\n' IGNORE 1 LINES " ;
		
		statement.execute(statementText);
		String enableKey ="ALTER TABLE " + tablename + " ENABLE KEYS";
		statement.execute(enableKey);
		statement.execute("SET UNIQUE_CHECKS=1; ");
		statement.close();
		
	}
	
	
	private void createConnection() throws ClassNotFoundException, SQLException {
		// create database connection 
		Class.forName("com.mysql.jdbc.Driver");
		
		if(con == null)
			con = (com.mysql.jdbc.Connection)DriverManager.getConnection(url,username,password);

	}
	
	private void closeConnection() throws SQLException {
		con.close();		
	}
	
	
	private String checkImportCount(String tablename , int fileCount) {     
		String importSuccess = "false";
		try {
			ResultSet rs = con.createStatement().executeQuery("select count(*) from " + tablename);

			while (rs.next()){
				int tableRowCount = rs.getInt(1);
				logger.info("Table Rows : " + tableRowCount + " & File Rows: " +fileCount);
				
				if(tableRowCount == (fileCount-1)){
					importSuccess="true";
				}				
			}
			rs.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
		return importSuccess;
	}
	
	
	private int checkFileCount(File loadReleaseFileName) {     
			
		LineNumberReader reader =null; 
    	try { 
           reader = new LineNumberReader( new FileReader(loadReleaseFileName)); 
           while (( reader.readLine()) != null); 
                 return reader.getLineNumber(); 
        } catch (IOException ex) { 
                 return -1; 
        } finally{  
             if(reader !=null)
			 try {
				reader.close();
			 } catch (IOException e) {
				e.printStackTrace();
			} 
        }
	}

}
