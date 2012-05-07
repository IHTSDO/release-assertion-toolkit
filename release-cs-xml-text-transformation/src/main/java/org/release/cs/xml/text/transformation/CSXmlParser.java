package org.release.cs.xml.text.transformation;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.apache.log4j.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.xml.sax.SAXException;

/**
 * @author Varsha Parekh
 * 
 * @goal xml-text-builder
 * @requiresDependencyResolution compile
 */

public class CSXmlParser extends AbstractMojo {
	
	private static Logger logger = Logger.getLogger(CSXmlParser.class);
	
	/**
	 * changesetFileName
	 * 
	 * @parameter
	 * @required
	 */
	private String changesetFileName;
	
	/**
	 * changesetFileName
	 * 
	 * @parameter
	 * @required
	 */
	private String outputPath;

	public void execute() throws MojoExecutionException, MojoFailureException {
	
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			
			if (changesetFileName.indexOf(File.pathSeparator) < 0) {
				changesetFileName = changesetFileName.replace('/', '\\');
			}
			
			changesetFileName = changesetFileName + "/changeset.xml";
			CSXmlElementHandler handler = new CSXmlElementHandler(outputPath);
			logger.info("outputFileName:- " + changesetFileName);
			
			saxParser.parse(changesetFileName, handler);
		} catch (ParserConfigurationException e) {
			logger.error("===ParserConfigurationException==" + e.getMessage());
		} catch (SAXException e) {
			logger.error("===SAXException==" + e.getMessage());
		} catch (IOException e) {
			logger.error("===IOException==" + e.getMessage());
		}
	}
}
