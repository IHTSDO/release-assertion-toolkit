package org.release.cs.xml.text.transformation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CSXmlElementHandler extends DefaultHandler {
	private BufferedWriter conWriter;
	private BufferedWriter descWriter;
	private BufferedWriter relWriter;
	private BufferedWriter writer;	
	private boolean firstElementFound;	
	private boolean writerIsRefset = false;
	private boolean isRefsetId = false;
	private StringBuffer refsetStrBuffer = null;
	private HashMap<String, BufferedWriter> refsetWriters;
	private String currentRefsetId;
	private boolean isRefsetName;
	private String outputPath;

	private static Logger logger = Logger.getLogger(CSXmlElementHandler.class);


	public CSXmlElementHandler(String outputPath) {
		this.outputPath = outputPath;
		File outputDir = new File(outputPath);
		outputDir.mkdirs();
	}

	public void startDocument() throws SAXException {
		try {	
			conWriter = new BufferedWriter(new FileWriter(new File(outputPath + File.separator + "concepts.txt")));
			addConceptHeader();
			descWriter = new BufferedWriter(new FileWriter(new File(outputPath + File.separator + "descriptions_temp.txt")));
			addDescHeader();
			relWriter = new BufferedWriter(new FileWriter(new File(outputPath + File.separator + "relationships.txt")));
			addRelationshipHeader();
			refsetWriters = new HashMap<String, BufferedWriter>();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	private void clearBrackets() throws FileNotFoundException, UnsupportedEncodingException, IOException {
		
		FileInputStream fis = new FileInputStream(outputPath + File.separator + "descriptions_temp.txt");
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");				
		BufferedReader fileReader = new BufferedReader(isr);
		
		FileOutputStream fos = new FileOutputStream(outputPath + File.separator + "descriptions.txt");
		OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF8");
		BufferedWriter fileWriter = new BufferedWriter(osw);
		String lineRead = "";
		
		while ((lineRead = fileReader.readLine()) != null) {		
			lineRead =lineRead.replaceAll("START TAG", "<");
			lineRead =lineRead.replaceAll("END TAG", ">");
			
			fileWriter.append(lineRead);
			fileWriter.write("\r\n");
			
		}
		
		fileReader.close();
		fileWriter.close();
	}

	public void endDocument() throws SAXException {
		try {
			conWriter.close();
			descWriter.close();
			relWriter.close();
			clearBrackets();
		for (String refsetId : refsetWriters.keySet()) {
			refsetWriters.get(refsetId).close();
		}
		
		logger.info("Parsing Finished Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    	if (qName.equalsIgnoreCase("description")) {
    		writer = descWriter;
		} else if (qName.equalsIgnoreCase("relationship")) {
    		writer = relWriter;
		} else if (qName.equalsIgnoreCase("attribute")) {
    		writer = conWriter;
		} else if (qName.equalsIgnoreCase("refsetMember")) {
			writerIsRefset = true;
			refsetStrBuffer = new StringBuffer();
		} else if (qName.equalsIgnoreCase("refsetId")) {
			isRefsetId = true;
		} else if (qName.equalsIgnoreCase("refsetName")) {
			isRefsetName = true;
		} else {
			if (writer != null && firstElementFound) {
				try {
					writer.append("\t");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
    	if (qName.equalsIgnoreCase("attribute") ||
			qName.equalsIgnoreCase("description") ||
			qName.equalsIgnoreCase("relationship") ||
			qName.equalsIgnoreCase("refsetMember")) {
    		firstElementFound = false;
    		
    		try {
	    		if (writer != null) {
					writer.newLine();
			    	writer.flush();
			    	writer = null;
			    	
			    	writerIsRefset = false;
	        	}
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
    }
    
    public void characters(char ch[], int start, int length) throws SAXException {
    	String str = new String(ch, start, length);
    	
    	if (str.trim().length() > 0) {
    		if (!writerIsRefset && writer != null) {
	    		try {
					writer.append(str);
		    		firstElementFound = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	} else if (isRefsetId) {
	    		isRefsetId = false;
	    		currentRefsetId = str;
	    		refsetStrBuffer.append(str);
	    		refsetStrBuffer.append("\t");
	    	} else if (isRefsetName) {
	    		isRefsetName = false;
	    		if (!refsetWriters.containsKey(currentRefsetId)) {
	    			try {
	    				logger.info("Refset : " + str);
	    				BufferedWriter refsetWriter = new BufferedWriter(new FileWriter(new File(outputPath + File.separator + str + ".txt")));
						refsetWriters.put(currentRefsetId, refsetWriter);
						addRefsetHeader(refsetWriter);
					} catch (IOException e) {
						e.printStackTrace();
					}
	    		}

	    		writer = refsetWriters.get(currentRefsetId);
				try {
					writer.append(refsetStrBuffer.toString());
					writer.append(str);
					writer.append("\t");
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	} else if (writerIsRefset) {
	    		if (writer != null) {
		    		try {
						writer.append(str);
			    		firstElementFound = true;
					} catch (IOException e) {
						e.printStackTrace();
					}
		    	} else {
		    		refsetStrBuffer.append(str);
		    		refsetStrBuffer.append("\t");
		    	}
	    	}
    	}
    }

    private void addRefsetHeader(BufferedWriter refsetWriter) throws IOException {
		refsetWriter.append("id\t");
		refsetWriter.append("effectiveTime\t");
		refsetWriter.append("active\t");
		refsetWriter.append("refsetId\t");
		refsetWriter.append("refsetName\t");
		refsetWriter.append("concept\t");
		refsetWriter.append("refCompId\t");
		refsetWriter.append("refCompType\t");
		refsetWriter.append("refset-uuid\t");
		refsetWriter.append("concept-uuid\t");
		refsetWriter.append("refCompId-uuid\t");
		refsetWriter.append("author\t");
		refsetWriter.append("path\t");
		refsetWriter.append("commitTime\t");
		refsetWriter.append("type\t");
		refsetWriter.append("values");
		refsetWriter.newLine();
		refsetWriter.flush();
	}

	private void addRelationshipHeader() throws IOException {
		relWriter.append("id\t");
		relWriter.append("effectiveTime\t");
		relWriter.append("active\t");
		relWriter.append("sourceId\t");
		relWriter.append("destinationId\t");
		relWriter.append("relationshipGroup\t");
		relWriter.append("typeId\t");
		relWriter.append("characteristicTypeId\t");
		relWriter.append("modifierId\t");
		relWriter.append("relationship-uuid\t");
		relWriter.append("source-uuid\t");
		relWriter.append("target-uuid\t");
		relWriter.append("type-uuid\t");
		relWriter.append("characteristic-uuid\t");
		relWriter.append("author\t");
		relWriter.append("path\t");
		relWriter.append("commitTime");
		relWriter.newLine();
		relWriter.flush();
	}

	private void addDescHeader() throws IOException {
		descWriter.append("id\t");
		descWriter.append("effectiveTime\t");
		descWriter.append("active\t");
		descWriter.append("conceptId\t");
		descWriter.append("languageCode\t");
		descWriter.append("typeId\t");
		descWriter.append("term\t");
		descWriter.append("caseSignificanceId\t");
		descWriter.append("description-uuid\t");
		descWriter.append("concept-uuid\t");
		descWriter.append("type-uuid\t");
		descWriter.append("isCaseSig-uuid\t");
		descWriter.append("author\t");
		descWriter.append("path\t");
		descWriter.append("commitTime");
		descWriter.newLine();
		descWriter.flush();
	}

	private void addConceptHeader() throws IOException {
		conWriter.append("id\t");
		conWriter.append("effectiveTime\t");
		conWriter.append("active\t");
		conWriter.append("definitionStatusId\t");
		conWriter.append("concept-uuid\t");
		conWriter.append("isDefined-uuid\t");
		conWriter.append("author\t");
		conWriter.append("path\t");
		conWriter.append("commitTime");
		conWriter.newLine();
		conWriter.flush();
	}

}
