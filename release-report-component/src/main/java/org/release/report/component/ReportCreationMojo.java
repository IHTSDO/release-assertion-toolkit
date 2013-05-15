package org.release.report.component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.release.report.model.MessageType;
import org.release.report.util.WriteExcel;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import jxl.write.WriteException;
/**
 * @author Varsha Parekh
 * 
 * @goal report-builder
 * @requiresDependencyResolution compile
 */

public class ReportCreationMojo extends AbstractMojo {
	
	private static Logger logger = Logger.getLogger(ReportCreationMojo.class);


	/**
	 * Location of the build directory.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File targetDirectory;
			

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
	 * reportLocation
	 * 
	 * @parameter
	 * @required
	 */
	private String reportLocation;
	
	/**
	 * reportName
	 * 
	 * @parameter
	 * @required
	 */
	private String reportName;
	
	/**
	 * reportName
	 * 
	 * @parameter
	 * @required
	 */
	private String excelreportName;
	
	
	private Connection con;
	private WriteExcel writeExcel = null;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			createConnection();
			logger.info("Database Connection Created...");		
		
			String runId = getRunId();
			
			if(runId != null){
				
				BufferedWriter reportPrint = createReport(runId, reportLocation , reportName);
				logger.info("Report Created Successfully...");
				
				//copy report in particular place
				reportLocation = reportLocation.toString().replace('\\', '/');
			
				closeConnection();
				logger.info("Database Connection Closed...");
			}	
			
		
		}catch(SQLException sq){
			logger.info(sq.getMessage());		
		}catch (Exception e) {
			throw new MojoFailureException(e.getLocalizedMessage(), e);
		}
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
	
	
	;
	
	private String getRunId() {     
		int runId = 0 ;
		try {
			ResultSet rs = con.createStatement().executeQuery("select max(runid) from qa_run");

			while (rs.next()){
				runId = rs.getInt(1);
				logger.info("RunId : " + runId);			
			}
			rs.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
		return Integer.toString(runId);
	}
	
	
	private BufferedWriter createReport(String runId , String reportLocation, String reportName) throws FileNotFoundException, UnsupportedEncodingException , IOException {
			File parent = new File(reportLocation);
			if(!parent.exists()){
				parent.mkdir();
			}
			File report =new File(reportLocation,reportName);
		
		    //if file doesnt exists, then create it
		    if(!report.exists()){
		    	report.createNewFile();
		    }

			FileOutputStream fos = new FileOutputStream(report);
			OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF8");
			BufferedWriter reportWriter = new BufferedWriter(osw);
		
			
			
			
			writeExcel = new WriteExcel();
			writeExcel.setOutputFile(getExcelReportName());
			
			//create report header info
			
			try {
				logger.info("Opened Report File        :" + getReportName());
				writeExcel.write();
			} catch (WriteException e) {
				logger.info("Cannot create report file :" + getReportName() + " " + e.getMessage());
			} catch (IOException e) {
				logger.info("Cannot create report file :" + getReportName() + " " + e.getMessage());
			} catch (NullPointerException e) {			
				logger.info("Cannot create report file :" + getReportName() + " " + e.getMessage());
				System.exit(1);
			}
			
			//writeExcel.addHeaderRow("Assertionuuid" + "\t" + "Result" + "\t" + "Count" + "\t" + "Assertiontext");

			
			try {
				ResultSet rs = con.createStatement().executeQuery("select a.effectivetime," +
					" b.assertionuuid ," +
					" b.assertiontext , b.result , b.count " +
					" from qa_run a , qa_report b " +
					" where a.runid = b.runid " +
					" and a.runid = " + runId );
				
				reportWriter.append("Assertionuuid" + "\t" + "Result" + "\t" + "Count" + "\t" + "Assertiontext");
				reportWriter.append("\r\n");
				
				while (rs.next()){
					//String effectivetime = rs.getString(1);
					String assertionuuid = rs.getString(2);
					String assertiontext = rs.getString(3);
					String result = rs.getString(4);
					String count = rs.getString(5);
					
					if(result.equals("F")){
						result = "Fail";
						String row = assertionuuid +  "," + result + "," + count + "," + assertiontext;
						writeExcel.addRow(MessageType.Fail, row);						
					}else{
						result = "Pass";
						String row = assertionuuid +  "," + result + "," + count + "," + assertiontext;
						writeExcel.addRow(MessageType.Pass, row);						
					}				
					
					reportWriter.append(assertionuuid +  "\t" + result + "\t" + count + "\t" + assertiontext );
					reportWriter.append("\r\n");					
				}
			
				rs.close();
				reportWriter.close();
				
				//write an empty line for the report
				//writeExcel.addRow(MessageType.Pass, " , , ");
				writeExcel.close();
				
			} catch (SQLException e) {			
				e.printStackTrace();
			} catch (WriteException e) {			
				e.printStackTrace();
			}
			
		return reportWriter;
	}

	

	public String getReportName() {
		return reportName;
	}
	
	public String getExcelReportName() {
		return excelreportName;
	}
	

}
