package org.ihtsdo.release.fileqa.mojo;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.ihtsdo.release.fileqa.model.Column;
import org.ihtsdo.release.fileqa.model.MessageType;
import org.ihtsdo.release.fileqa.model.Metadata;
import org.ihtsdo.release.fileqa.tests.*;
import org.ihtsdo.release.fileqa.util.DOMUtil;
import org.ihtsdo.release.fileqa.util.DateUtils;
import org.ihtsdo.release.fileqa.util.JAXBUtil;
import org.ihtsdo.release.fileqa.util.WriteExcel;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;


/**
 * @author Varsha Parekh
 * 
 * @goal release-file-qa
 * @requiresDependencyResolution compile
 */

public class QAMojo extends AbstractMojo {

	private static Logger logger = Logger.getLogger(QAMojo.class.getName());
	private static Metadata fileTestMetadata = new Metadata();
	private static WriteExcel writeExcel = null;
	private static HashMap <String, String> fileMetadataMap = null;
	
	
	/**
	 * release date for the current release.
	 * 
	 * @parameter
	 * @required
	 */
	private String releaseDate;

	/**
	 * Date of the previous release.
	 * 
	 * @parameter
	 * @required
	 */
	private String previousReleaseDate;

	/**
	 * previous release working directory where the files  are copied
	 * to be compared against current
	 * 
	 * @parameter 
	 * @required
	 */
	private String previousWorkingDirectory;

	/**
	 * current release working directory where the files  are copied
	 * to be tested and eventually packaged
	 * 
	 * @parameter 
	 * @required
	 */
	private String currentWorkingDirectory;

	/**
	 * release date.
	 * 
	 * @parameter
	 * @required
	 */
	private String reportName;
	
	/**
	 * source directory is the root directory containing all the released files
	 *  
	 * @parameter 
	 * @required
	 */
	private String sourceDirectory;
	
	/**
	 * manifest file is an xml file listing all the files and directory structure for
	 * packaging the release.
	 * 
	 * @parameter 
	 * @required
	 */
	private String manifestFileName;

	/**
	 * metadataPath is the location of all the metadata files that specify the fileQA tests
	 * 
//	 * @parameter
//	 * @required
	 */
	private static String metadataPath = "target/metadata";

	/**
	 * Not used but 'run-file-qa' profile not happy without it.
	 *
	 * @parameter
	 */
	private String fileQAMetadataPath;

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

			// source directory must contain files from releases to be tested
			if (!testValidDirectory(sourceDirectory, true)) {
				throw new MojoExecutionException("Release files directory :" + sourceDirectory + " is invalid or empty, please provide a valid directory ");
			}
			

			// target working directory for current release MAY contain files but must be accessible for copying current files for testing
			if (!testValidDirectory(currentWorkingDirectory, false)) {
				throw new MojoExecutionException("Working directory :" + currentWorkingDirectory + " is invalid, please provide a valid directory ");
			}
	

			// target working directory for previous release MAY contain files but must be accessible for copying previous files for testing
			if (!testValidDirectory(currentWorkingDirectory, false)) {
				throw new MojoExecutionException("Working directory :" + previousWorkingDirectory + " is invalid, please provide a valid directory ");
			}

			// prepare test report
			writeExcel = new WriteExcel();
			writeExcel.setOutputFile(reportName);

			try {
				writeExcel.write();			
			} catch (Exception e) {
				logger.info("Cannot create report file :" + reportName + " " + e.getMessage());
				System.exit(1);
			}

			logger.info("");
			logger.info("Opened Report File        :" + reportName);			
			logger.info("");
			
			// now start the real testing of the release...
			try {
				processFolders();
			} catch (Exception e) {
				logger.error("Message : ", e);

			} finally {
				try {
					writeExcel.close();
				} catch (Exception e) {
					logger.error("Message : ", e);
				}
			}

			Date eDate = new Date();

			logger.info("FileQA Ended           :" + sdf.format(eDate));
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
	 *  processfolders() is where all the work is done to test the contents of the release folder
	 */
	private void processFolders() throws ParserConfigurationException, SAXException, IOException, Exception {
		
		// filter for testing text files only
		FilenameFilter textFileFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		};

		// get names of files to be released
		ArrayList<String> fileNames = DOMUtil.getElementsByType(manifestFileName, "file", "Name");
		
		// get filenames and corresponding matadata files from manifest
		if (fileMetadataMap == null ) {			
			// build a map of all the filenames and corresponding metadata files from the manifest
			fileMetadataMap = DOMUtil.getAttributePairsByType (manifestFileName, "file", "Name", "Metadata") ;			
		}
		
		//copy files from current directory to the target directory to be tested
		File sourceDir = new File(sourceDirectory);
		File targetDir = new File(currentWorkingDirectory);
		
		//in case it exists, delete it with all contents
		if (targetDir.isDirectory()) {
			FileUtils.deleteQuietly(targetDir);
		}
		logger.info("Copying current release files to target folder : " + currentWorkingDirectory );
		
		copyFilesFromSourceTree(fileNames, sourceDir, targetDir);
		
		//list all files in target directory
		String [] currFiles = targetDir.list(textFileFilter);
		if (currFiles == null || currFiles.length < 1) {
			logger.error("No files in current directory, no testing performed");
			return;
		}
		
		//look thru all the files from the manifest list
		for(int i=0; i < fileNames.size(); i++) {
			boolean fileFound = false;
			// test for all files present in current release as specified by manifest 
			String name = fileNames.get(i);
			
			for (int j = 0; j < currFiles.length; j++) {
				if (name.equals((String)currFiles[j])) {
					//set flag
					fileFound = true;
					//quit looping thru currfiles
					break;
				}
			} // end looping thru files in target directory
			
			//check if file exists
			if (!fileFound && fileNames.get(i).endsWith(".txt")) {
				// log missing file
				logger.error("File missing from target directory: " + fileNames.get(i));	
				writeExcel.addRow(MessageType.FAILURE, "FileTest,Current,Failed,File is missing in folder :" + sourceDirectory + File.pathSeparator + fileNames.get(i));
				
			} else {
			
				// convert name to the corresponding name from the previous release 
				//TODO: fix this- some previous files use a different date than the date of the previous release
				if (name.length() > 12) {
					name = name.substring(0, name.length() - 12) + previousReleaseDate + ".txt";
				}
				//fix filenames if  beta release
				name = stripXZ(name);
				fileNames.set(i, name);
				if (logger.isDebugEnabled()) {
					logger.debug("Corresponding previous file name : " + name);
				}
			}

				
		} // end looping thru current file Names from manifest
		

		// get previous file out of the previous directory structure
		sourceDir = new File(sourceDirectory);
		targetDir = new File(previousWorkingDirectory);
		
		if (targetDir.isDirectory()) {
			FileUtils.deleteQuietly(targetDir);
		}
		
		copyFilesFromSourceTree(fileNames, sourceDir, targetDir );
		
		String[] prevFiles = targetDir.list(textFileFilter);
		logger.info("Total number of matching previous files found : " + prevFiles.length);

		// write an empty line for the report
		writeExcel.addRow(MessageType.SUCCESS, " , , ");

		// check for files previously released but not found in current
		for (int i = 0; i < prevFiles.length; i++) {
			boolean foundCurrMatch = false;
			String currMatch = prevFiles[i].substring(0, prevFiles[i].length() - 12);
			
			for (int j = 0; j < currFiles.length && !foundCurrMatch; j++) {
				if ((currFiles[j].substring(0, currFiles[j].length() - 12)).contains(prevFiles[i].substring(0, prevFiles[i].length() - 12))) {
					currMatch = currFiles[j].substring(0, currFiles[j].length() - 12);
					foundCurrMatch = true;
				}
			}

			if (!foundCurrMatch) {
				writeExcel.addRow(MessageType.FAILURE, "FileTest,Current,Failed,File is missing in folder :" + sourceDirectory + File.pathSeparator + currMatch);
				writeExcel.addRow(MessageType.SUCCESS, "FileTest,Previous,Passed, ," + previousWorkingDirectory + File.separator + prevFiles[i]);
				writeExcel.addRow(MessageType.SUCCESS, " , , ");
			}
		} // end looping thru previous files
		
		String cS = null;
		
		//now start testing all the current files in the target directory
		for (int i = 0; i < currFiles.length; i++) { 	

			cS = currFiles[i];
			Date sDate = new Date();

			boolean foundPrevMatch = false;
			writeExcel.addHeaderRow(cS);

			int j;
			for (j = 0; j < prevFiles.length && !foundPrevMatch; j++) {
				
				// stripping the date portion and the extension
				// eg. "20101007.txt"			
				String pS = prevFiles[j].substring(0, prevFiles[j].length() - 12);

				if (cS.contains(pS)) {
					foundPrevMatch = true;	
					break;
				} // end if cs contains ps
				
			}// end looping thru previous files for match
							
			// look for a metadata file specified in the manifest for the release file
			if (getMetadata(cS, manifestFileName)) {
				if (logger.isDebugEnabled()) {
					dumpMetadata();
				}
				File currFile = new File(currentWorkingDirectory + File.separator + currFiles[i]);
				logger.info("Processing  ...        :" + currFile.getAbsoluteFile());
				
				if (foundPrevMatch) {
					if (logger.isDebugEnabled()) {
						logger.debug("Matching file found for current File :" + currFiles[i]);
						logger.debug("\tPrevious File :" + prevFiles[j]);
					}
					File prevFile = new File(previousWorkingDirectory + File.separator + prevFiles[j]);

					// start the rule tests
					if (logger.isDebugEnabled()) {
						logger.debug("");
						logger.debug(" ========== " + "Start: File Name Match Rule " + " ========== ");
					}
					FileNameTest.execute(fileTestMetadata, currFile, prevFile, logger, writeExcel);
					if (logger.isDebugEnabled()) {
						logger.debug(" ========== " + "End:   File Name Match Rule " + " ========== ");
					}

					if (logger.isDebugEnabled()) {
						logger.debug("");
						logger.debug(" ========== " + "Start: File Size Match Rule " + " ========== ");
					}

					FileSizeTest.execute(fileTestMetadata, currFile, prevFile, logger, writeExcel);

					if (logger.isDebugEnabled()) {
						logger.debug(" ========== " + "End:  File Size Match Rule " + " ========== ");
					}
				} // end foundPrevMatch 
				
				if (logger.isDebugEnabled()) {
					logger.debug("");
					logger.debug(" ========== " + "Start: Column Header Empty Rule " + " ========== ");
				}
				boolean passed = ColumnHeaderTest.execute(fileTestMetadata, currFile, logger, writeExcel, ColumnHeaderRuleEnum.EMPTY);

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
					ColumnHeaderTest.execute(fileTestMetadata, currFile, logger, writeExcel, ColumnHeaderRuleEnum.DELIMITER_CHECK);

					if (logger.isDebugEnabled()) {
						logger.debug(" ========== " + "End: Column Header Seperator Rule " + " ========== ");
					}

					if (logger.isDebugEnabled()) {
						logger.debug("");
						logger.debug(" ========== " + "Start: Column Header Count Rule " + " ========== ");
					}
					ColumnHeaderTest.execute(fileTestMetadata, currFile, logger, writeExcel, ColumnHeaderRuleEnum.COLUMN_COUNT_CHECK);
					if (logger.isDebugEnabled()) {
						logger.debug(" ========== " + "End: Column Header Count Rule " + " ========== ");
						logger.debug("");
					}

					if (logger.isDebugEnabled()) {
						logger.debug("");
						logger.debug(" ========== " + "Start: Column Header Present Rule " + " ========== ");
					}
					ColumnHeaderTest.execute(fileTestMetadata, currFile, logger, writeExcel, ColumnHeaderRuleEnum.PRESENT_RULE);

					if (logger.isDebugEnabled()) {
						logger.debug(" ========== " + "End: Column Header Present Rule " + " ========== ");
					}

					if (logger.isDebugEnabled()) {
						logger.debug("");
						logger.debug(" ========== " + "Start: Column Header Spell Check Rule " + " ========== ");
					}
					ColumnHeaderTest.execute(fileTestMetadata, currFile, logger, writeExcel, ColumnHeaderRuleEnum.SPELL_CHECK_RULE);
					if (logger.isDebugEnabled()) {
						logger.debug(" ========== " + "End: Column Header Spell Check Rule " + " ========== ");
						logger.debug("");
					}

				}
				if (fileTestMetadata.getColumn().size() > 0)
				{
					// only perform columnDataTests for metadata files that contain columns
					if (logger.isDebugEnabled()) {
						logger.debug("");
						logger.debug(" ========== " + "Start: Column Data Rules " + " ========== ");
					}
					ColumnDataTests.execute(releaseDate, fileTestMetadata, currFile, logger, writeExcel);
					if (logger.isDebugEnabled()) {
						logger.debug(" ========== " + "End: Column Data Rules " + " ========== ");
						logger.debug("");
					}
				}
				logger.info("Finished               :" + currFile.getAbsoluteFile());
				Date eDate = new Date();
				logger.info(DateUtils.elapsedTime("Elapsed                :", sDate, eDate));
			} else {
				// no metadata found
				writeExcel.addRow(MessageType.FAILURE, "Tests not performed, no metadata found for " + currentWorkingDirectory + File.pathSeparator + currFiles[i]);
			}

			if (!foundPrevMatch) {
				writeExcel.addRow(MessageType.SUCCESS, "FileTest,Current,Passed, ," + currentWorkingDirectory + File.separator + currFiles[i]);

				writeExcel.addRow(MessageType.FAILURE, "FileTest,Previous,Failed,File is missing in folder :" + previousWorkingDirectory + File.pathSeparator + cS );
			}

			// end all tests
			writeExcel.addRow(MessageType.SUCCESS, " , , ");
			
		} // end looping thru all current files for testing
	
	} // end proc
		
	/**
	 * This method flattens out the directory structure, copying all files in the source tree to a single directory
	 * 
	 * @param fileNames - list of files to be copied. if null, then ALL files are copied
	 * @param sourceTree - root directory where  the target files are located
	 * @param targetDir - directory where  the target files should be copied to
	 */
	private static void copyFilesFromSourceTree(ArrayList <String> fileNames, File sourceTree, File targetDir) {

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
			
			
			if (fileNames == null || fileNames.contains(sourceTree.getName())) {
				try {
					//check if file already in target directory
					File testFile = new File (sourceTree.getName());
					if (testFile.isFile())
					{
						logger.error("File " + testFile.getName() + " is a duplicate, there are two or more file with the same name in the release.");
						writeExcel.addRow(MessageType.FAILURE, "CopyFilesFromSourceTree,,Failed,File is a duplicate :" + testFile.getName());
					} else {
						//copy file to target Directory
						logger.info("Copying file: " + sourceTree.getName());
						org.apache.commons.io.FileUtils.copyFileToDirectory(sourceTree, targetDir, true);
					}
					
				} catch (IOException io ) 	{
					logger.error("Failed to copy source file " + sourceTree.getName());
					
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("File " + sourceTree.getName() + " in source dir, but not included in manifest");
				}
			}
		}
		
	}

	/**
	 * this method gets the metadata.xml file that describes the tests to be performed
	 * @param sourceFileName
	 * @return
	 * @throws IOException
	 */
	private static boolean getMetadata(String sourceFileName, String manifestFileName) throws IOException {

		boolean success = false;
		String metaDataFileName;
		
		try {
			
			//  find correct metadata file by reading attribute from manifest 
			metaDataFileName = fileMetadataMap.get(sourceFileName);
		} catch (Exception e) {
			logger.error("Problem getting metadata File Name " + e.getMessage());
			return false;
		}
		// use helper method to look in the folder and all subfolders to find the correct metadata file
		File metadataDirectory = new File (metadataPath);
		String metadataFilePath = getFileFromSourceFolder(metadataDirectory, metaDataFileName);
		
		logger.info("");
		
		if (metadataFilePath == null || metadataFilePath.length() < 1) {
			logger.error("No metadata file found for source file " + sourceFileName);
			return false;
		}
		logger.info("Loading  MetaData File :" + metadataFilePath + " for file name " + sourceFileName);
		

		if (fileTestMetadata != null) {
				fileTestMetadata.init();
		}

		fileTestMetadata = JAXBUtil.getMetadata(metadataFilePath, writeExcel);

		// check if there was an error and the metadata xml
		// was not unmarshalled properly
		if (fileTestMetadata != null ) {
			
			success = true;
			
			if ( fileTestMetadata.getColumn() != null) {

				// sort the list of columns based on their positions
				ArrayList<Column> columns = fileTestMetadata.getColumn();
				Collections.sort(columns);
				fileTestMetadata.setColumn(columns);
			} else {
				// no columns in metadata, test using an empty set of columns
				logger.info("Not processing any columns for metadata file found at : " + metadataFilePath);
				fileTestMetadata.setColumn(new ArrayList <Column> () );
			}
		} else {
			logger.error("Not processing metadata , no file or invalid file found for : " + sourceFileName);
		}

		return success;
	} // end proc getMetadata
	

	private static void dumpMetadata() {

		org.ihtsdo.release.fileqa.model.File file = fileTestMetadata.getFile();
		ArrayList<Column> columns = fileTestMetadata.getColumn();

		if (file != null) {
			logger.debug("METADATA Loaded into the File object");
			logger.debug(file.getDescription());
			logger.debug(file.getDelimiter());
			logger.debug(file.getEncoding());
			logger.debug(file.getRegex());
		} else
			logger.debug("File Object is null");

		columns = fileTestMetadata.getColumn();

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
	
	/**
	 * Used to fixup filenames from beta release for comparison with previous release filenames
	 * 
	 * @param fileName
	 * @return
	 */
	private static String stripXZ (String fileName) {
		
		if (fileName.startsWith("x") || fileName.startsWith("z")) {
			return fileName.substring(1); 
		} else {
			return fileName;
		}
	}
	
	private static String getFileFromSourceFolder(File searchDirectory, String fileName) {
		
		String matchFileName = "";
		
		if (searchDirectory.isDirectory())  {
			File[] files = searchDirectory.listFiles();
			int i = 0;
						
			if (logger.isDebugEnabled()) {
				logger.debug("Searching directory "
						+ searchDirectory.getAbsolutePath());
			}
			while (matchFileName.equals("") &&  i < files.length) {
				matchFileName = getFileFromSourceFolder(files[i], fileName);				
				i++;
			}
		} else {
			// not a directory, it's a file, check for match
			if (logger.isDebugEnabled()) {
				logger.debug("File compared: "
						+ searchDirectory.getAbsolutePath());
			}
			if (searchDirectory.getName().contains(fileName)) {
				//found  match
				matchFileName = searchDirectory.getAbsolutePath();
				if (logger.isDebugEnabled()) {
					logger.debug("found matching file : " + searchDirectory);
				}	
			}
		}
		return matchFileName;		

	} // end proc getFileFromSourceFolder()
}
