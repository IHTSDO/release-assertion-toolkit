package org.ihtsdo.release.fileqa.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import org.apache.log4j.Logger;
import org.ihtsdo.release.fileqa.model.MessageType;
import org.ihtsdo.release.fileqa.model.Metadata;
import org.ihtsdo.release.fileqa.util.WriteExcel;


public class FileSizeTest {
	public static long getFileSize(File file, Metadata qa, final Logger logger,
			final WriteExcel writeExcel) {

		long fileSize = file.length();
		if (logger.isDebugEnabled()) {
			logger.debug("File :" + file.getName());
			logger.debug("Size :" + fileSize);
		}

		return fileSize;
	}
	
	public static byte[] createChecksum(String filename) throws Exception {
	       InputStream fis =  new FileInputStream(filename);
	       byte[] buffer = new byte[1024];
	       MessageDigest complete = MessageDigest.getInstance("MD5");
	       int numRead;

	       do {
	           numRead = fis.read(buffer);
	           if (numRead > 0) {
	               complete.update(buffer, 0, numRead);
	           }
	       } while (numRead != -1);

	       fis.close();
	       return complete.digest();
	   }

	
	
	   // a byte array to a HEX string
	   public static String getMD5Checksum(String filename) throws Exception {
	       byte[] b = createChecksum(filename);
	       String result = "";

	       for (int i=0; i < b.length; i++) {
	           result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
	       }
	       return result;
	   }

	public static boolean execute(final Metadata qa, final File currFile,
			File prevFile, final Logger logger, final WriteExcel writeExcel)
			throws IOException , Exception{

		boolean passed = false;

		if (logger.isDebugEnabled()) {
			logger.debug("Executing File Size Rule");
			logger.debug("Current Release File Name : " + currFile.getName());
			logger.debug("Previous Release File Name: " + prevFile.getName());
		}

		long prevFileSize = getFileSize(prevFile, qa, logger, writeExcel);
		long currFileSize = getFileSize(currFile, qa, logger, writeExcel);

		if (currFileSize != prevFileSize) {
			if (qa.getFile().getCarryForward().equals("true")) {
				writeExcel
						.addRow(MessageType.FAILURE,"FileSizeTest,Current,Failed,Carry forward set to TRUE Bytes: "
								+ currFileSize
								+ " Size don't match,"
								+ currFile.getAbsoluteFile());
				writeExcel.addRow(MessageType.FAILURE,"FileSizeTest,Previous,Failed,Carry forward set to TRUE Bytes: "
								+ prevFileSize
								+ " Size don't match,"
								+ prevFile.getAbsoluteFile());
			} else {
				writeExcel.addRow(MessageType.SUCCESS,"FileSizeTest,Current,Passed, ,"
						+ currFile.getAbsoluteFile());
				writeExcel.addRow(MessageType.SUCCESS,"FileSizeTest,Previous,Passed, ,"
						+ prevFile.getAbsoluteFile());
			}

			if (logger.isDebugEnabled())
				logger.debug("Previous and Current Release File Size DONT match");
			
		} else {		
			
			String prevHashCode = getMD5Checksum(prevFile.getAbsoluteFile().toString());
			String currHashCode = getMD5Checksum(currFile.getAbsoluteFile().toString());
			
			if(prevHashCode.equals(currHashCode)){
				System.out.println(prevFile.getAbsoluteFile().toString() + " & " + prevHashCode);
				System.out.println(currFile.getAbsoluteFile().toString() + " & " + currHashCode);
			
				if (qa.getFile().getCarryForward().equals("false")) {
					writeExcel.addRow(MessageType.FAILURE,"FileSizeTest,Current,Failed,Carry forward set to FALSE Bytes: "
									+ currFileSize
									+ " Size do match,"
									+ currFile.getAbsoluteFile());
					writeExcel.addRow(MessageType.FAILURE,"FileSizeTest,Previous,Failed,Carry forward set to FASLE Bytes: "
									+ prevFileSize
									+ " Size do match,"
									+ prevFile.getAbsoluteFile());
				}
			} else {
				writeExcel.addRow(MessageType.SUCCESS,"FileSizeTest,Current,Passed, ,"
						+ currFile.getAbsoluteFile());
				writeExcel.addRow(MessageType.SUCCESS,"FileSizeTest,Previous,Passed, ,"
						+ prevFile.getAbsoluteFile());
			}
			if (logger.isDebugEnabled())
				logger.debug("Previous and Current Release File Size DO match");
			
			passed = true;
		}

		return passed;
	}
}
