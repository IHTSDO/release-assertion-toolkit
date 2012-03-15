package org.ihtsdo.runlist;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.ihtsdo.xml.elements.RunList;
import org.ihtsdo.xml.elements.Script;

@SuppressWarnings("restriction")
public class RunListProcessor {
	private RunList runList = null;
	private int totalScriptCountInRunList = 0;
	private int scriptsExecuted = 0;

	public RunListProcessor(File runlistFile) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(RunList.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		runList = (RunList) jaxbUnmarshaller.unmarshal(runlistFile);

		totalScriptCountInRunList += runList.getScript().size();
	}

	public boolean containsScripts() {
		if (runList == null) {
			return false;
		} else if (totalScriptCountInRunList == 0) {
			return false;
		} 
			
		return true;
	}
	
	public boolean hasNext() {
		return scriptsExecuted < totalScriptCountInRunList;
	}

	public Script nextSqlFileName() {
		Script script = runList.getScript().get(scriptsExecuted);
		scriptsExecuted++;
		
		return script;
	}
}
