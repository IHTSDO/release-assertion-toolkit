package org.ihtsdo.release.fileqa.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;
import org.ihtsdo.release.fileqa.model.MessageType;
import org.ihtsdo.release.fileqa.model.Metadata;
import org.ihtsdo.release.fileqa.model.TestError;
import org.ihtsdo.release.fileqa.util.WriteExcel;

public class ColumnDataTests {

	private static ArrayList<TestError> errors = new ArrayList<TestError>();

	public static boolean execute(String currentReleaseDate, final Metadata qa,
			final File currFile, final Logger logger,
			final WriteExcel writeExcel) throws IOException {

		// clear the test errors
		errors.clear();

		boolean passed = false;
		boolean releaseDatePresent = false;

		if (logger.isDebugEnabled()) {
			logger.debug("Current Release File Name : " + currFile.getName());
		}

		int headerSize = 0;
		int lineCount = 0;
		String headerData = null;
		
		//RF1 files do not have effectiveTime column
		boolean effectiveTimeColumnExpected = false;

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(currFile.getAbsoluteFile()));

			// skip the header row
			while (lineCount < 1) {
				headerData = br.readLine();
				if (headerData != null)
					headerSize = headerData.length();					
				if (logger.isDebugEnabled()) {
					logger.debug("Header :" + headerData);
					logger.debug("Size :" + headerSize);
					logger.debug("Column data");
				}
				lineCount++;
			}
			
			
			// read the file
			String lineData = null;			
			while ((lineData = br.readLine()) != null) {
				lineCount++;
				int beginIndex = 0;
				char emptyStartChar =lineData.charAt(0);	
				if(emptyStartChar == '\t'){
					//first element is empty execute regex test handling as special loop					
					// test the column data using regex expressions
					if (qa.getColumn().get(beginIndex).getRegex() != null) {						
						//loop across the regex tests	
						for (int regexCount = 0; regexCount < qa.getColumn()
								.get(beginIndex).getRegex().size()
								; regexCount++) {
							boolean match = false;
							try {
								match = Character.toString(emptyStartChar).matches(qa.getColumn().get(
										beginIndex).getRegex().get(regexCount).getExpression());
							} catch (PatternSyntaxException e) {
								writeExcel.addRow(MessageType.FAILURE, qa
										.getColumn().get(beginIndex).getRegex()
										.get(regexCount).getTest()
										+ ",Current,Failed,Line No. "
										+ lineCount
										+ " : Column :"
										+ qa.getColumn().get(beginIndex).getHeader()
										+ " Data: "
										+ emptyStartChar + " :" + e.getMessage());
							}

							if (!match) {
								// look if this type of error happened before
								boolean found = findError(qa.getColumn().get(beginIndex).getRegex().get(regexCount).getTest(), 
										qa.getColumn().get(beginIndex).getHeader(), 
										qa.getColumn().get(beginIndex).getRegex().get(regexCount).getExpression());
								if (!found) {
									TestError error = new TestError();
									error.setTest(qa.getColumn().get(beginIndex).getRegex().get(regexCount).getTest());
									error.setColumnHeader(qa.getColumn().get(beginIndex).getHeader());
									error.setRegex(qa.getColumn().get(beginIndex).getRegex().get(regexCount).getExpression());
									error.setMessage(qa.getColumn().get(beginIndex).getRegex().get(regexCount).getFailureMessage());
									error.setLineCount(lineCount);
									error.setColumnData(Character.toString(emptyStartChar));
									error.setCount(1);
									errors.add(error);
								}
							}
						} // end looping thru regex tests
					} // end if regex not null					
				}
				
				
				
				
				boolean emptyEndCharFlag = lineData.endsWith("\t");						
				if(emptyEndCharFlag){
					char emptyEndChar ='\t';
					int lastIndex = qa.getColumn().size() - 1;
					//last element is empty execute regex test handling as special loop					
					// test the column data using regex expressions
					if (qa.getColumn().get(lastIndex).getRegex() != null) {						
						//loop across the regex tests	
						for (int regexCount = 0; regexCount < qa.getColumn().get(lastIndex).getRegex().size()
								; regexCount++) {
							boolean match = false;
							try {
								match = Character.toString(emptyEndChar).matches(qa.getColumn().get(
										lastIndex).getRegex().get(regexCount).getExpression());
							} catch (PatternSyntaxException e) {
								writeExcel.addRow(MessageType.FAILURE, qa
										.getColumn().get(lastIndex).getRegex()
										.get(regexCount).getTest()
										+ ",Current,Failed,Line No. "
										+ lineCount
										+ " : Column :"
										+ qa.getColumn().get(lastIndex).getHeader()
										+ " Data: "
										+ emptyEndChar + " :" + e.getMessage());
							}

							if (!match) {
								// look if this type of error happened before
								boolean found = findError(qa.getColumn().get(lastIndex).getRegex().get(regexCount).getTest(), 
										qa.getColumn().get(lastIndex).getHeader(), 
										qa.getColumn().get(lastIndex).getRegex().get(regexCount).getExpression());
								if (!found) {
									TestError error = new TestError();
									error.setTest(qa.getColumn().get(lastIndex).getRegex().get(regexCount).getTest());
									error.setColumnHeader(qa.getColumn().get(lastIndex).getHeader());
									error.setRegex(qa.getColumn().get(lastIndex).getRegex().get(regexCount).getExpression());
									error.setMessage(qa.getColumn().get(lastIndex).getRegex().get(regexCount).getFailureMessage());
									error.setLineCount(lineCount);
									error.setColumnData(Character.toString(emptyEndChar));
									error.setCount(1);
									errors.add(error);
								}
							}
						} // end looping thru regex tests
					} // end if regex not null					
				}				
				
				Scanner lineScanner = new Scanner(lineData);
				lineScanner.useDelimiter(qa.getFile().getDelimiter());
				
				int column = 0;
				
				if(emptyStartChar == '\t'){	
					column = 1; //First column is empty so start from next column
				}
					
				boolean rowEmpty = true;
				boolean lineLoop = true;
				
				// process the read row
				while (lineScanner.hasNext() && lineLoop) {					
					String columnData = lineScanner.next();
					boolean loop = true;					

					if ( column < qa.getColumn().size()) {
					// column or row trailing spaces test
					if (columnData.length() != columnData.trim().length()) {

						// look if this type of error happened before
						boolean found = findError("ColumnTrailingSpacesTest",
								qa.getColumn().get(column).getHeader(),
								"ColumnTrailingSpacesTest");
						if (!found) {
							TestError error = new TestError();

							error.setTest("ColumnTrailingSpacesTest");
							error.setColumnHeader(qa.getColumn().get(column)
									.getHeader());
							error.setRegex("ColumnTrailingSpacesTest");
							if (column != qa.getColumn().size())
								error.setMessage("Column has trailing spaces");
							else
								error.setMessage("Row has trailing spaces");
							error.setLineCount(lineCount);
							error.setColumnData(columnData);
							error.setCount(1);

							errors.add(error);
						}
					} else {
						// test the column data using regex expressions
						if (qa.getColumn().get(column).getRegex() != null) {
							//loop across the regex tests	
							for (int regexCount = 0; regexCount < qa.getColumn()
									.get(column).getRegex().size()
									&& loop; regexCount++) {
								boolean match = false;
								try {								
									match = columnData.matches(qa.getColumn().get(
											column).getRegex().get(regexCount)
											.getExpression());
								} catch (PatternSyntaxException e) {
									writeExcel.addRow(MessageType.FAILURE, qa
											.getColumn().get(column).getRegex()
											.get(regexCount).getTest()
											+ ",Current,Failed,Line No. "
											+ lineCount
											+ " : Column :"
											+ qa.getColumn().get(column)
													.getHeader()
											+ " Data: "
											+ columnData + " :" + e.getMessage());
								}
	
								if (!columnData.equals(""))
									rowEmpty = false;
	
								if (logger.isDebugEnabled()) {
									logger.debug("Column :"
											+ qa.getColumn().get(column)
													.getHeader()
											+ " Data :"
											+ columnData
											+ " :"
											+ qa.getColumn().get(column).getRegex()
													.get(regexCount)
													.getExpression());
								}
	
								if (match) {
									if (logger.isDebugEnabled()) {
										logger.debug("Message :"
												+ qa.getColumn().get(column)
														.getRegex().get(regexCount)
														.getSuccessMessage());
										passed = true;
									}
								} else {
	
									// look if this type of error happened before
									boolean found = findError(qa.getColumn().get(
											column).getRegex().get(regexCount)
											.getTest(), qa.getColumn().get(column)
											.getHeader(), qa.getColumn()
											.get(column).getRegex().get(regexCount)
											.getExpression());
	
									if (!found) {
										TestError error = new TestError();
	
										error.setTest(qa.getColumn().get(column)
												.getRegex().get(regexCount)
												.getTest());
										error.setColumnHeader(qa.getColumn().get(
												column).getHeader());
										error.setRegex(qa.getColumn().get(column)
												.getRegex().get(regexCount)
												.getExpression());
										error.setMessage(qa.getColumn().get(column)
												.getRegex().get(regexCount)
												.getFailureMessage());
										error.setLineCount(lineCount);
										error.setColumnData(columnData);
										error.setCount(1);
	
										errors.add(error);
									}
									loop = false;
									if (logger.isDebugEnabled()) {
										logger.debug("Message :"
												+ qa.getColumn().get(column)
														.getRegex().get(regexCount)
														.getFailureMessage());
									}
	
									/**
									 * FIXME writeExcel.addRow(MessageType.FAILURE,
									 * qa .getColumnsList().get(column)
									 * .getRegex().get(regexCount).getTest() +
									 * ",Current,Failed,Line No. " + lineCount +
									 * " : Column :" + qa.getColumn().get(column)
									 * .getHeader() + " : " +
									 * qa.getColumn().get(column)
									 * .getRegex().get(regexCount)
									 * .getFailureMessage() + " Data: " +
									 * columnData);
									 **/
								}
							} // end looping thru regex tests
						} // end if regex not null
											

						if (qa.getColumn().get(column).getHeader()
								.toLowerCase().equals("effectivetime")) {
							// metadata includes the effectiveTime column
							effectiveTimeColumnExpected = true;
							DateFormat df = new SimpleDateFormat("yyyyMMdd");

							// Get Date the passed release date
							Date d1 = null;
							try {
								d1 = df.parse(currentReleaseDate);
							} catch (ParseException e) {
								// look if this type of error happened before
								boolean found = findError("EffectiveTimeTest",
										qa.getColumn().get(column).getHeader(),
										"ParseExceptionCurrentReleaseDate");

								if (!found) {
									TestError error = new TestError();

									error.setTest("EffectiveTimeTest");
									error.setColumnHeader(qa.getColumn().get(
											column).getHeader());
									error.setRegex("ParseException");
									error.setMessage(e.getMessage());
									error.setLineCount(lineCount);
									error.setColumnData(currentReleaseDate);
									error.setCount(1);

									errors.add(error);
								}
								/**
								 * FIXME writeExcel.addRow(MessageType.FAILURE,
								 * "EffectiveTimeTest" +
								 * ",Current,Failed,Line No. " + lineCount +
								 * " : Column :" + qa.getColumn().get(column)
								 * .getHeader() + " : " + "Release date :" +
								 * currentReleaseDate + ", Column date: " +
								 * columnData + e.getMessage());
								 **/
								if (logger.isDebugEnabled()) {
									logger.debug("EffectiveTimeTest"
											+ ",Current,Failed,Line No. "
											+ lineCount
											+ " : Column :"
											+ qa.getColumn().get(column)
													.getHeader() + " : "
											+ "Release date :"
											+ currentReleaseDate
											+ ", Column date: " + columnData
											+ e.getMessage());
								}
							}
							// Get the column data
							Date d2 = null;
							try {
								d2 = df.parse(columnData);
							} catch (ParseException e1) {

								boolean found = findError("EffectiveTimeTest",
										qa.getColumn().get(column).getHeader(),
										"ParseExceptionColumnData");

								if (!found) {
									TestError error = new TestError();

									error.setTest("EffectiveTimeTest");
									error.setColumnHeader(qa.getColumn().get(
											column).getHeader());
									error.setRegex("ParseExceptionColumnData");
									error.setMessage(e1.getMessage());
									error.setLineCount(lineCount);
									error.setColumnData(columnData);
									error.setCount(1);

									errors.add(error);
								}
								/***
								 * FIXME writeExcel.addRow(MessageType.FAILURE,
								 * "EffectiveTimeTest" +
								 * ",Current,Failed,Line No. " + lineCount +
								 * " : Column :" + qa.getColumn().get(column)
								 * .getHeader() + " : " + "Release date :" +
								 * currentReleaseDate + ", Column date: " +
								 * columnData + e1.getMessage());
								 **/
								if (logger.isDebugEnabled()) {
									logger.debug("EffectiveTimeTest"
											+ ",Current,Failed,Line No. "
											+ lineCount
											+ " : Column :"
											+ qa.getColumn().get(column)
													.getHeader() + " : "
											+ "Release date :"
											+ currentReleaseDate
											+ ", Column date: " + columnData
											+ e1.getMessage());
								}
							}
							if (d1 != null && d2 != null) {
								releaseDatePresent = true;
							
								if (d2.after(d1)) {

									boolean found = findError(
											"EffectiveTimeTest", qa.getColumn()
													.get(column).getHeader(),
											"ColumnDateAfterCurrentReleaseDate");

									if (!found) {
										TestError error = new TestError();

										error.setTest("EffectiveTimeTest");
										error.setColumnHeader(qa.getColumn()
												.get(column).getHeader());
										error
												.setRegex("ColumnDateAfterCurrentReleaseDate");
										error
												.setMessage(" Column date is after Current Release date :"
														+ currentReleaseDate);
										error.setLineCount(lineCount);
										error.setColumnData(columnData);
										error.setCount(1);

										errors.add(error);
									}

									/***
									 * FIXME
									 * writeExcel.addRow(MessageType.FAILURE,
									 * "EffectiveTimeTest" +
									 * ",Current,Failed,Line No. " + lineCount +
									 * " : Column :" + qa.getColumn().get(
									 * column).getHeader() + " : " +
									 * " Column date: " + columnData +
									 * " is after Release date :" +
									 * currentReleaseDate);
									 ***/
									if (logger.isDebugEnabled()) {
										logger.debug("EffectiveTimeTest"
												+ ",Current,Failed,Line No. "
												+ lineCount
												+ " : Column :"
												+ qa.getColumn().get(column)
														.getHeader() + " : "
												+ " Column date: " + columnData
												+ " is after Release date :"
												+ currentReleaseDate);
									}
								} // end if d2>d1  
							} // end if dates d1 and d2 not null
						} // end if column "effectiveTime"
						column++;
					} // end looping thru columns
					} else { // delimiter is not correct
						boolean found = findError("ColumnSeparatorTest", "Header Mismatch",
								"ColumnSeparatorNotRight");

						if (!found) {
							TestError error = new TestError();

							error.setTest("ColumnSeparatorTest");
							error.setColumnHeader("Header Mismatch");
							error.setRegex("ColumnSeparatorNotRight");
							error.setMessage("Delimiter set to :"
									+ qa.getFile().getDelimiter());
							error.setLineCount(lineCount);
							error.setColumnData("");
							error.setCount(1);

							errors.add(error);
						}

						lineLoop = false;
					}
				}
				lineScanner.close();

				// row empty test
				if (rowEmpty) {
					boolean found = findError("RowEmptyTest", "Blank",
							"RowIsBlank");

					if (!found) {
						TestError error = new TestError();

						error.setTest("RowEmptyTest");
						error.setColumnHeader("Blank");
						error.setMessage("Row is empty");
						error.setLineCount(lineCount);
						error.setRegex("RowIsBlank");
						error.setCount(1);
						errors.add(error);
					}
					/***
					 * FIXME writeExcel.addRow(MessageType.FAILURE,
					 * "RowEmptyTest,Current,Failed,Line No. " + lineCount +
					 * " is empty");
					 **/
				}
			} // end while loop for reading files

			// file read done here
			// write all the errors

			for (int i = 0; i < errors.size(); i++) {
				TestError error = errors.get(i);
				if (error.getCount() > 1) {
					if (!error.getColumnHeader().equals("Blank"))
						writeExcel.addRow(MessageType.FAILURE, error.getTest()
								+ ",Current,Failed,Total errors :"
								+ error.getCount() + " Starting line no. :"
								+ error.getLineCount() + " Column :"
								+ error.getColumnHeader() + " : "
								+ error.getMessage());
					else
						writeExcel.addRow(MessageType.FAILURE, error.getTest()
								+ ",Current,Failed,Total errors :"
								+ error.getCount() + " Starting line no. :"
								+ error.getLineCount() + "  : "
								+ error.getMessage());
				} else
					writeExcel.addRow(MessageType.FAILURE, error.getTest()
							+ ",Current,Failed,Line No :"
							+ error.getLineCount() + " Column :"
							+ error.getColumnHeader() + " : " + " Data :"
							+ error.getColumnData() + " ; "
							+ error.getMessage());
			}

		} catch (IOException e) {
			TestError error = new TestError();

			error.setTest("IOException");
			error.setMessage(e.getMessage());
			error.setLineCount(lineCount);

			errors.add(error);

			/***
			 * FIXME writeExcel.addRow(MessageType.FAILURE,
			 * "FIleIO,Current,Failed,Line No. " + lineCount + " :" +
			 * e.getMessage());
			 **/
		} finally {
			// scanner.close();
			try {
				
				if (!releaseDatePresent && effectiveTimeColumnExpected) {
					writeExcel.addRow(MessageType.FAILURE,
							"ReleaseDateRowPresentTest,Current,Failed,Release date :"
									+ currentReleaseDate
									+ " no corresponding row present,"
									+ currFile.getAbsoluteFile());
				}
				br.close();
			} catch (IOException e) {
				writeExcel.addRow(MessageType.FAILURE,
						"FIleIO,Current,Failed,Line No. " + lineCount + " :"
								+ e.getMessage());
			}
		}
		return passed;
	}

	private static boolean findError(String testName, String header,
			String regex) {

		boolean found = false;

		for (int i = 0; i < errors.size() && !found; i++) {
			TestError testError = errors.get(i);

			if (testName.equals(testError.getTest())
					&& header.equals(testError.getColumnHeader())
					&& regex.equals(testError.getRegex())
					) {
				found = true;

				int count = testError.getCount();
				testError.setCount(++count);
				errors.set(i, testError);
			}
		}
		return found;
	}
}
