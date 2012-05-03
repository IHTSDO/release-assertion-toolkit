package org.release.cs.xml.text.transformation;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

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
	
	/*
	public static void main(String[] args) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			
			CSXmlElementHandler handler = new CSXmlElementHandler();
			
			saxParser.parse("src//main//resources//TestFile.xml", handler);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * xmlFilePath
	 * 
	 * @parameter
	 * @required
	 */
	private String xmlFilePath;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		// TODO Auto-generated method stub
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			
			CSXmlElementHandler handler = new CSXmlElementHandler();
			
			//saxParser.parse("src//main//resources//TestFile.xml", handler);
			saxParser.parse(xmlFilePath, handler);			
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.out.println("===ParserConfigurationException==" + e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			System.out.println("===SAXException==" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("===IOException==" + e.getMessage());
		}
	}
}
