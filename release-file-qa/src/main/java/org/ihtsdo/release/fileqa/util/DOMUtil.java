package org.ihtsdo.release.fileqa.util;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class DOMUtil {
	
	//some static members for efficiency
	static Logger domLogger = Logger.getLogger(DOMUtil.class);
	static Document sDoc = null;
	static String sFileName = "";
	
	
	
	/**
	 * getFlatFileListFromManifest - Use this method to get the file names from the xml manifest file
	 * 
	 * @param manifestFile - the name of the xml file containing the directory structure and file list 
	 * @return files - ArrayList of file names found in the manifest xml document
	 * @throws Exception
	 */
	public static ArrayList<String> getFlatFileListFromManifest(String manifestFile)
		throws Exception
	{
		return getElementsByType(manifestFile, "file", "Name");
	}
	
	/**
	 * getElementsByType - this is a general method used to get the values of any specified attribute 
	 * from the the specified elements contained in an XML file
	 * 
	 * @param fileName - the name of the xml file including path
	 * @param elementType - the element type that should be returned in the list
	 * @param attributeName - the attribute to return in the list
	 * @return elements -ArrayList of all the String names of the nodes specified type
	 * 
	 * @throws Exception - re-throws any exceptions after logging
	 */
	public static ArrayList<String> getElementsByType (String fileName, 
														String elementType, 
														String attributeName) 
			throws Exception 
	{

		ArrayList<String> elements = new ArrayList<String>();
		elements.clear();
		
		try {

			Document doc = getDomDocument(fileName);
			
			    
			NodeList nodes = doc.getElementsByTagName(elementType);  
			Node memberNode = null;
			String attributeValue = "";
			NamedNodeMap attributes = null;
			
			domLogger.info("Fetched  " + nodes.getLength() + " nodes from file " + fileName);
			
			for (int i = 0; i < nodes.getLength(); i++) {
				memberNode = nodes.item(i);
				attributes = memberNode.getAttributes();
				attributeValue = attributes.getNamedItem(attributeName).getTextContent();
				elements.add(attributeValue);
				
				if (domLogger.isDebugEnabled())
					domLogger.debug("Added attribute " + attributeName + " = " + attributeValue);
	
			}
			
			if (domLogger.isDebugEnabled())
			{
				domLogger.debug("Dumping the document tree:\n" + doc.getNodeName());
				dumpTree(doc, "   ");
			}
			
	    } catch (Exception e) {
			domLogger.error("Exception while getting elements " + elementType + " from file " + fileName + ".");
			domLogger.error("Exception is " + e.getMessage());
		throw e;
	    }

		return elements;

	} // end getElementsByType
	
	/**
	 * getTargetPathsFromManifest - Used to get the relative paths of all the files
	 * as listed in the manifest xml file used for release packaging
	 * 
	 * @param manifestFile - the name of the xml file containing the directory structure and file list 
	 * @return paths - list of files including their paths
	 */
	public static ArrayList<String> getTargetPathsFromManifest(String manifestFile)
		throws Exception
	{
		
		// TODO: implement this for the packaging program
		ArrayList<String> paths = new ArrayList<String>();
		
		try {
			Document doc = getDomDocument(manifestFile);
			
			//process doc to extract all files including their paths
			
		} catch (Exception e) {
			throw e;
		}
		
		return paths;
	} // end getSourcePathsFromManifest()
	
	
	/**
	 * getDomDocument - builds the DOM structure from an XML file
	 * 
	 * @param fileName - the name of the xml file containing the directory structure and file list
	 * @return - doc - the DOM document constructed from the xml file
	 * @throws Exception
	 */
	private static Document getDomDocument(String fileName)
		throws Exception
	{
		if (sFileName == fileName) {
			// same instance as was parsed before
			return sDoc;
		}
		sFileName = fileName;
		
		//read file
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    
	    dbf.setNamespaceAware(true);
	    dbf.setIgnoringElementContentWhitespace(true);

	    // Parse the input to produce a parse tree with its root
	    // in the form of a Document object
	     sDoc = null;
	    
	    try {
	      DocumentBuilder builder = dbf.newDocumentBuilder();
	      //builder.setErrorHandler(new MyErrorHandler());
	      InputSource is = new InputSource(fileName);
	      sDoc = builder.parse(is);

	    } catch (Exception e) {
			domLogger.error("Exception while getting DOM from file " + fileName + ".");
			domLogger.error("Exception is " + e.getMessage());
			throw e;
	    }
	    
	    return sDoc;
	}
	
    /**
     * dumpTree - Dumps the xml tree to the log - useful for debugging
     * 
     * @param node - a node from the DOM
     * @param indent
     */
	private static void dumpTree(Node node, String indent) {
        
       NamedNodeMap nodes =node.getAttributes();
       
       if (!(nodes == null) && (nodes.getLength() > 0)) {
    	   
    	   domLogger.debug(indent + node.getNodeName() + nodes.getNamedItem("Name").toString());
       }    
        NodeList list = node.getChildNodes();
        for(int i=0; i<list.getLength(); i++)
            dumpTree(list.item(i),indent + "   ");

    }
	
	/**
	 * 
	 * @param fileName 		- name of the xml file to be updated with new attributes
	 * @param nodeType 		- node type that will receive the new attributes
	 * @param keyName 		- the attribute used to match to the HashMap values e.g. "id" or "Name"
	 * @param newAttributeName - the name of the new attribute to be added to the nodes
	 * @param pairs 		- the HashMap containing the key, value pairs , (e.g. id, metadata)
	 * @throws Exception
	 */
	public static void addNewAttributes(String fileName, String nodeType, String keyName, String newAttributeName, HashMap <String,String> pairs) 
		throws Exception 
	{
		Document doc2 = getDomDocument(fileName);	    
		NodeList nodes = doc2.getElementsByTagName(nodeType); 
		
		Node memberNode = null;
		String attributeValue = "";
		String metaFile = "";
		NamedNodeMap attributes = null;
		
		domLogger.info("Fetched  " + nodes.getLength() + " nodes from file " + fileName);
		
		for (int i = 0; i < nodes.getLength(); i++) {
			memberNode = nodes.item(i);
			attributes = memberNode.getAttributes();
			attributeValue = attributes.getNamedItem(keyName).getTextContent();
			metaFile = pairs.get(attributeValue);
			domLogger.debug("Adding " + metaFile + " to " + attributeValue);
			((Element)memberNode).setAttribute(newAttributeName, metaFile);
			
		}
		
		// now write out new file
		domLogger.info("Writing file with new attributes " );
			
		writeXmlFile (doc2, fileName + "_new.xml")	;
			
	}
	
	// This method writes a DOM document to a file
	private static void writeXmlFile(Document doc, String filename) {
	    try {
	        // Prepare the DOM document for writing
	        Source source = new DOMSource(doc);

	        // Prepare the output file
	        File file = new File(filename);
	        Result result = new StreamResult(file);

	        // Write the DOM document to the file
	        Transformer xformer = TransformerFactory.newInstance().newTransformer();
	        xformer.transform(source, result);
	    } catch (Exception e) {
	    	domLogger.error("Could not write out DOM " + e.getMessage());
	    }
	}

	/**
	 * getAttributePairsByType - this is a general method used to get the 
	 * specified attribute pairs of any specified node type 
	 *  contained in an XML file
	 * 
	 * @param fileName - the name of the xml file including path
	 * @param elementType - the element type that should be returned in the list
	 * @param keyAttributeName - the attribute to return in the list
	 * @param valueAttributeName - the attribute to return in the list
	 * @return attributeMap - HashMap of all key, value pairs specified 
	 * 
	 * @throws Exception - re-throws any exceptions after logging
	 */
	public static HashMap <String, String> getAttributePairsByType (String fileName, 
														String elementType,
														String keyAttributeName,
														String valueAttributeName) 
			throws Exception 
	{

		HashMap<String, String> pairs = new HashMap<String, String>();
		pairs.clear();
		
		try {

			Document doc = getDomDocument(fileName);
			
			    
			NodeList nodes = doc.getElementsByTagName(elementType);  
			Node memberNode = null;
			String keyValue = "";
			String valueValue = "";
			NamedNodeMap attributes = null;
			
			domLogger.info("Fetched  " + nodes.getLength() + " key, value attribute pairs from file " + fileName);
			
			for (int i = 0; i < nodes.getLength(); i++) {
				memberNode = nodes.item(i);
				attributes = memberNode.getAttributes();
				keyValue = attributes.getNamedItem(keyAttributeName).getTextContent();
				valueValue = attributes.getNamedItem(valueAttributeName).getTextContent();
				pairs.put(keyValue, valueValue);
				
				if (domLogger.isDebugEnabled())
					domLogger.debug("Added key, value pair: " + keyValue + " : " + valueValue);
	
			}
			
			if (domLogger.isDebugEnabled())
			{
				domLogger.debug("Dumping the document tree:\n" + doc.getNodeName());
				dumpTree(doc, "   ");
			}
			
	    } catch (Exception e) {
			domLogger.error("Exception while getting attributes of nodeType " + elementType + " from file " + fileName + ".");
			domLogger.error("Exception is " + e.getMessage());
		throw e;
	    }

		return pairs;
	}
	

}
