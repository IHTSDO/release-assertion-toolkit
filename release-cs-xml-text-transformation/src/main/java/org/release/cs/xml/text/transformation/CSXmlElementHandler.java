package org.release.cs.xml.text.transformation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CSXmlElementHandler extends DefaultHandler {
	private BufferedWriter attrWriter;
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
	
	public void startDocument() throws SAXException {
		try {
			attrWriter = new BufferedWriter(new FileWriter(new File("src//main//resources//attributes.txt")));
			addAttrHeader();
			descWriter = new BufferedWriter(new FileWriter(new File("src//main//resources//descriptions.txt")));
			addDescHeader();
			relWriter = new BufferedWriter(new FileWriter(new File("src//main//resources//relationships.txt")));
			addRelationshipHeader();
			refsetWriters = new HashMap<String, BufferedWriter>();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	public void endDocument() throws SAXException {
		try {
	    	attrWriter.close();
	    	descWriter.close();
	    	relWriter.close();
	    	for (String refsetId : refsetWriters.keySet()) {
	    		refsetWriters.get(refsetId).close();
	    	}
	    	System.out.println("Done Parsing");
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
    		writer = attrWriter;
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
	    				
	    				System.out.println(str);
	    				//Here is Non Identified txt gets created
	    				BufferedWriter refsetWriter = new BufferedWriter(new FileWriter(new File("src//main//resources//" + str + ".txt")));
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

	private void addAttrHeader() throws IOException {
		attrWriter.append("id\t");
		attrWriter.append("effectiveTime\t");
		attrWriter.append("active\t");
		attrWriter.append("definitionStatusId\t");
		attrWriter.append("concept-uuid\t");
		attrWriter.append("isDefined-uuid\t");
		attrWriter.append("author\t");
		attrWriter.append("path\t");
		attrWriter.append("commitTime");
		attrWriter.newLine();
		attrWriter.flush();
	}

}
