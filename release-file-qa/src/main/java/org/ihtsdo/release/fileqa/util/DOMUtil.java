package org.ihtsdo.release.fileqa.util;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class DOMUtil {
	
	static Logger domLogger = Logger.getLogger(DOMUtil.class);
	
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
		return getElementsByType(manifestFile, "File", "Name");
	}
	
	/**
	 * getElementsByName - this is a general method used to get the values of any specified attribute 
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
		//read file
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    
	    dbf.setNamespaceAware(true);
	    dbf.setIgnoringElementContentWhitespace(true);

	    // Parse the input to produce a parse tree with its root
	    // in the form of a Document object
	    Document doc = null;
	    
	    try {
	      DocumentBuilder builder = dbf.newDocumentBuilder();
	      //builder.setErrorHandler(new MyErrorHandler());
	      InputSource is = new InputSource(fileName);
	      doc = builder.parse(is);

	    } catch (Exception e) {
			domLogger.error("Exception while getting DOM from file " + fileName + ".");
			domLogger.error("Exception is " + e.getMessage());
			throw e;
	    }
	    
	    return doc;
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

	
}
