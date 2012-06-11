package org.ihtsdo.release.fileqa.mojo;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import jxl.write.WriteException;

import org.apache.log4j.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.ihtsdo.release.fileqa.action.QA;
import org.ihtsdo.release.fileqa.model.Column;
import org.ihtsdo.release.fileqa.model.MessageType;
import org.ihtsdo.release.fileqa.model.Metadata;
import org.ihtsdo.release.fileqa.model.Props;
import org.ihtsdo.release.fileqa.tests.ColumnDataTests;
import org.ihtsdo.release.fileqa.tests.ColumnHeaderRuleEnum;
import org.ihtsdo.release.fileqa.tests.ColumnHeaderTest;
import org.ihtsdo.release.fileqa.tests.FileNameTest;
import org.ihtsdo.release.fileqa.tests.FileSizeTest;
import org.ihtsdo.release.fileqa.util.DOMUtil;
import org.ihtsdo.release.fileqa.util.DateUtils;
import org.ihtsdo.release.fileqa.util.JAXBUtil;
import org.ihtsdo.release.fileqa.util.WriteExcel;
import org.xml.sax.SAXException;


/**
 * @author Varsha Parekh
 * 
 * @goal release-file-qa
 * @requiresDependencyResolution compile
 */

public class QAMojo extends AbstractMojo {

	private static Logger logger = Logger.getLogger(QAMojo.class.getName());
	private static Metadata metadata;
	private static WriteExcel writeExcel = null;

	/**
	 * release date.
	 * 
	 * @parameter
	 * @required
	 */
	private String releaseDate;

	/**
	 * release date.
	 * 
	 * @parameter
	 */
	private String releaseName;

	/**
	 * release date.
	 * 
	 * @parameter
	 * @required
	 */
	private String prevDir;

	/**
	 * release date.
	 * 
	 * @parameter
	 * @required
	 */
	private String currDir;

	/**
	 * release date.
	 * 
	 * @parameter
	 * @required
	 */
	private String reportName;
	
	/**
	 * source directory is the root directory containing all the released files
	 * This will be different for RF1 and RF2.
	 * 
	 * @parameter expression="${project.source.directory}"
	 * @required
	 */
	private String sourceDirectory;
	
	/**
	 * manifest file is an xml file listing all the files and ditrectory structure for
	 * packaging the release.
	 * 
	 * @parameter expression="${project.source.manifestFileName}"
	 * @required
	 */
	private String manifestFileName;

	/**
	 * Location of the build directory.
	 * 
	 * @parameter expression="${project.build.targetDirectory}"
	 * @required
	 */
	private String targetDirectory;

	public void execute() throws MojoExecutionException, MojoFailureException {

		try {

			Date sDate = new Date();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");

			logger.info("FileQA Started  :" + sdf.format(sDate));
			logger.info("");

			if (!DateUtils.isValidDateStr(releaseDate, "yyyyMMdd")) {
				logger.info("Release date :" + releaseDate + " is invalid, please provide a valid release date with format YYYYMMDD");
				throw new MojoExecutionException("Release date :" + releaseDate + " is invalid, please provide a valid realse date YYYYMMDD");
			}

			// test the various directories for use in performing file QA

			// previous files directory must contain files from a previous release
			if (!testValidDirectory(prevDir, true)) {
				throw new MojoExecutionException("Previous files directory :" + prevDir + " is invalid or empty, please provide a valid directory ");
			}
			
			// current files directory must contain files from the current release
			if (!testValidDirectory(currDir, true)) {
				throw new MojoExecutionException("Current files directory :" + currDir + " is invalid or empty, please provide a valid directory ");
			}
	

			// target directory MAY contain files but must be accessible for copying current files for testing
			if (!testValidDirectory(targetDirectory, false)) {
				throw new MojoExecutionException("Target directory :" + targetDirectory + " is invalid, please provide a valid directory ");
			}

//			QA.execute(props, prevDir, currDir);
			writeExcel = new WriteExcel();
			writeExcel.setOutputFile(reportName);

			try {
				writeExcel.write();
			} catch (WriteException e) {
				logger.info("Cannot create report file :" + reportName + " " + e.getMessage());
			} catch (IOException e) {
				logger.info("Cannot create report file :" + reportName + " " + e.getMessage());
				System.exit(1);
			} catch (NullPointerException e) {
				logger.info("Cannot create report file :" + reportName + " " + e.getMessage());
				System.exit(1);
			}

			logger.info("");
			logger.info("Opened Report File        :" + reportName);
			
			logger.info("");


			Date eDate = new Date();

			logger.info("");
			logger.info("FileQA Started         :" + sdf.format(sDate));
			if (logger.isDebugEnabled()) {
				logger.debug("FileQA Started  :" + sdf.format(sDate));
				logger.debug("");
			}
			
			// now start the real testing of the release...
			try {
				processFolders();
			} catch (Exception e) {
				logger.error("Message : ", e);

			} finally {
				try {
					writeExcel.close();
				} catch (WriteException e) {
					logger.error("Message : ", e);
				} catch (IOException e) {
					logger.error("Message : ", e);
				}
			}


			logger.info("FileQA Ended           :" + sdf.format(eDate));
			if (logger.isDebugEnabled()) {
				logger.debug("FileQA Ended    :" + sdf.format(eDate));
				logger.debug("");
			}
			logger.info("");

			logger.info(DateUtils.elapsedTime("Total elapsed          :", sDate, eDate));

		} catch (Exception e) {
			logger.error("Message :", e);
			throw new MojoExecutionException(e.getMessage());
		}
	} // end of the execute() method
	
	
	/**
	 * 
	 * @param directory
	 * @param nonEmpty - test for non-empty
	 * @return true if directory is valid
	 */
	Boolean testValidDirectory(String directory, Boolean nonEmpty)
	{
		Boolean valid = true;
		File testDir = null;
		try {
			testDir = new File(directory);
			if (!testDir.isDirectory()) {
				if (nonEmpty) {
					//there are supposed to be files here!!
					logger.info("Folder :" + directory + " is not a directory, please provide a valid directory ");
					valid = false;
				} else {
					// can be an empty directory, so let's make one
					testDir.mkdir();
				}

			}
		} catch (NullPointerException e) {
			logger.info("Cannot open folder :" + directory + " " + e.getMessage());
			valid = false;
		}

		if (nonEmpty) {
			String sourceFiles[] = testDir.list();
			if (sourceFiles.length <= 0) {
				logger.info("Folder :" + directory
						+ " is empty, please provide a valid directory ");
				valid = false;
			}
		}
		
		return valid;
	}
	
	/**
	 *  processfolders is where all the work is done to test the contents of the release folder
	 */
	private void processFolders() throws ParserConfigurationException, SAXException, IOException, Exception {

		ArrayList fileNames = DOMUtil.getElementsByType(manifestFileName, "file", "Name");

		//copy files from current directory to the target directory to be tested
		File sourceDir = new File(currDir);
		File targetDir = new File(targetDirectory);
		copyFilesFromSourceTree(fileNames, sourceDir, targetDir);

		// test current files against previous
		File prevyDir = new File(prevDir);
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		};
		
		String[] currFiles = targetDir.list(filter);
		String[] prevFiles = prevyDir.list(filter);
		String cS = null;

		// write an empty line for the report
		writeExcel.addRow(MessageType.SUCCESS, " , , ");

		// check for files previously released but not found in current
		for (int i = 0; i < prevFiles.length; i++) {
			boolean foundCurrMatch = false;
			String currMatch = prevFiles[i].substring(0, prevFiles[i].length() - 12);
			for (int j = 0; j < currFiles.length && !foundCurrMatch; j++) {
				if ((prevFiles[i].substring(0, prevFiles[i].length() - 12)).contains(currFiles[j].substring(0, currFiles[j].length() - 12))) {
					currMatch = currFiles[j].substring(0, currFiles[j].length() - 12);
					foundCurrMatch = true;
				}
			}

			if (!foundCurrMatch) {
				writeExcel.addRow(MessageType.FAILURE, "FileTest,Current,Failed,File is missing in folder :" + currDir + currMatch + "YYYYMMDD.txt");
				writeExcel.addRow(MessageType.SUCCESS, "FileTest,Previous,Passed, ," + prevDir + prevFiles[i]);
				writeExcel.addRow(MessageType.SUCCESS, " , , ");
			}
		}
		
		for (int i = 0; i < currFiles.length; i++) { 
				

			boolean foundPrevMatch = false;
			writeExcel.addHeaderRow(currFiles[i]);

			for (int j = 0; j < prevFiles.length && !foundPrevMatch; j++) {
				if (logger.isDebugEnabled()) {
					logger.debug("Current File :" + currFiles[i]);
					logger.debug("Previous File :" + prevFiles[j]);
				}

				// stripping the date portion and the extension
				// eg. _20101007.txt
				cS = currFiles[i].substring(0, currFiles[i].length() - 12);
				String pS = prevFiles[j].substring(0, prevFiles[j].length() - 12);

				if (cS.contains(pS)) {

					Date sDate = new Date();
					foundPrevMatch = true;

					// look for a metadata file matching the file name
					// excluding the date and the extension
					// eg. exclude 20101007.txt
					if (getMetadata("/metadata/" + cS + "Metadata.xml")) {
						if (logger.isDebugEnabled())
							dumpMetadata();

						File currFile = new File(targetDir + File.separator + currFiles[i]);
						File prevFile = new File(prevDir + File.separator + prevFiles[j]);

						logger.info("Processing  ...        :" + currFile.getAbsoluteFile());

						// start the rule tests

						if (logger.isDebugEnabled()) {
							logger.debug("");
							logger.debug(" ========== " + "Start: File Name Match Rule " + " ========== ");
						}
						FileNameTest.execute(metadata, currFile, prevFile, logger, writeExcel);
						if (logger.isDebugEnabled()) {
							logger.debug(" ========== " + "End:   File Name Match Rule " + " ========== ");
						}

						if (logger.isDebugEnabled()) {
							logger.debug("");
							logger.debug(" ========== " + "Start: File Size Match Rule " + " ========== ");
						}

						FileSizeTest.execute(metadata, currFile, prevFile, logger, writeExcel);

						if (logger.isDebugEnabled()) {
							logger.debug(" ========== " + "End:  File Size Match Rule " + " ========== ");
						}

						if (logger.isDebugEnabled()) {
							logger.debug("");
							logger.debug(" ========== " + "Start: Column Header Empty Rule " + " ========== ");
						}
						boolean passed = ColumnHeaderTest.execute(metadata, currFile, logger, writeExcel, ColumnHeaderRuleEnum.EMPTY);

						if (logger.isDebugEnabled()) {
							logger.debug(" ========== " + "End: Column Header EMPTY Rule " + " ========== ");
						}

						// we only do other Column Header Tests
						// if the Column Header is NOT empty
						if (passed) {

							if (logger.isDebugEnabled()) {
								logger.debug("");
								logger.debug(" ========== " + "Start: Column Header Seperator Rule " + " ========== ");
							}
							ColumnHeaderTest.execute(metadata, currFile, logger, writeExcel, ColumnHeaderRuleEnum.DELIMITER_CHECK);

							if (logger.isDebugEnabled()) {
								logger.debug(" ========== " + "End: Column Header Seperator Rule " + " ========== ");
							}

							if (logger.isDebugEnabled()) {
								logger.debug("");
								logger.debug(" ========== " + "Start: Column Header Count Rule " + " ========== ");
							}
							ColumnHeaderTest.execute(metadata, currFile, logger, writeExcel, ColumnHeaderRuleEnum.COLUMN_COUNT_CHECK);
							if (logger.isDebugEnabled()) {
								logger.debug(" ========== " + "End: Column Header Count Rule " + " ========== ");
								logger.debug("");
							}

							if (logger.isDebugEnabled()) {
								logger.debug("");
								logger.debug(" ========== " + "Start: Column Header Present Rule " + " ========== ");
							}
							ColumnHeaderTest.execute(metadata, currFile, logger, writeExcel, ColumnHeaderRuleEnum.PRESENT_RULE);

							if (logger.isDebugEnabled()) {
								logger.debug(" ========== " + "End: Column Header Present Rule " + " ========== ");
							}

							if (logger.isDebugEnabled()) {
								logger.debug("");
								logger.debug(" ========== " + "Start: Column Header Spell Check Rule " + " ========== ");
							}
							ColumnHeaderTest.execute(metadata, currFile, logger, writeExcel, ColumnHeaderRuleEnum.SPELL_CHECK_RULE);
							if (logger.isDebugEnabled()) {
								logger.debug(" ========== " + "End: Column Header Spell Check Rule " + " ========== ");
								logger.debug("");
							}

						}

						if (logger.isDebugEnabled()) {
							logger.debug("");
							logger.debug(" ========== " + "Start: Column Data Rules " + " ========== ");
						}
						ColumnDataTests.execute(releaseDate, metadata, currFile, logger, writeExcel);
						if (logger.isDebugEnabled()) {
							logger.debug(" ========== " + "End: Column Data Rules " + " ========== ");
							logger.debug("");
						}
						logger.info("Finished               :" + currFile.getAbsoluteFile());
						Date eDate = new Date();
						logger.info(DateUtils.elapsedTime("Elapsed                :", sDate, eDate));
						if (logger.isDebugEnabled())
							logger.debug("Finished          :" + currFile.getAbsoluteFile());
					}
				}
			}
			if (!foundPrevMatch) {
				writeExcel.addRow(MessageType.SUCCESS, "FileTest,Current,Passed, ," + targetDir + currFiles[i]);

				writeExcel.addRow(MessageType.FAILURE, "FileTest,Previous,Failed,File is missing in folder :" + prevDir + cS + "YYYYMMDD.txt");
			}
			// end all tests
			writeExcel.addRow(MessageType.SUCCESS, " , , ");
		}
	}
		

	private static void copyFilesFromSourceTree(ArrayList fileNames, File sourceTree,
			File targetDir) {

		if (sourceTree.isDirectory()) {
			logger.info("Found directory: " + sourceTree.getName());
			File[] contentFiles = sourceTree.listFiles();
			
			for (int i = 0; i < contentFiles.length; i++) {
				
				//recursive call to get contents
				copyFilesFromSourceTree(fileNames, contentFiles[i], targetDir);
			}
		} else {
			// must be a file.
			logger.debug("Found file: " + sourceTree.getName());
			
			if (fileNames.contains(sourceTree.getName())) {
				try {
					//copy file to target Directory
					logger.info("Copying file: " + sourceTree.getName());
					org.apache.commons.io.FileUtils.copyFileToDirectory(sourceTree, targetDir, true);
				} catch (IOException io ) 	{
					logger.error("Failed to copy source file " + sourceTree.getName());
				}
			} else {
				logger.info("File " + sourceTree.getName() + " in source dir, but not included in manifest");
			}
		}
		
	}

	private static boolean getMetadata(String metaDataFile) throws IOException {

		boolean success = false;

		if (metadata != null)
			metadata.init();

		logger.info("");
		logger.info("Loading  MetaData File :" + metaDataFile);
		

		metadata = JAXBUtil.getMetadata(metaDataFile, writeExcel);

		// check if there was an error and the metadata xml
		// was not marshalled properly
		if (metadata != null) {
			success = true;
			// sort the list of columns based on thier position
			ArrayList<Column> columns = metadata.getColumn();
			Collections.sort(columns);
			metadata.setColumn(columns);
		} else
			logger.info("Not processing file not found");

		return success;
	}

	private static void dumpMetadata() {

		org.ihtsdo.release.fileqa.model.File file = metadata.getFile();
		ArrayList<Column> columns = metadata.getColumn();

		if (file != null) {
			logger.debug("METADATA Loaded into the File object");
			logger.debug(file.getDescription());
			logger.debug(file.getDelimiter());
			logger.debug(file.getEncoding());
			logger.debug(file.getRegex());
		} else
			logger.debug("File Object is null");

		columns = metadata.getColumn();

		if (columns != null) {
			logger.debug("Column object");
			for (int i = 0; i < columns.size(); i++) {

				Column column = columns.get(i);
				logger.debug("Header :" + column.getHeader());
				logger.debug("Regex :" + column.getRegex());

			}
		} else
			logger.debug("Column(s) Object is null");

		logger.debug("");
	}
}
