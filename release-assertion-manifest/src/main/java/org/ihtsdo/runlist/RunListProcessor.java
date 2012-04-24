package org.ihtsdo.runlist;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.ihtsdo.runlist.mojo.ExecutionLogger;
import org.ihtsdo.xml.elements.RunList;
import org.ihtsdo.xml.elements.Script;

public class RunListProcessor {
	private RunList runList = null;
	private int totalScriptCountInRunList;
	private int scriptsExecuted = 0;
	private String sqlDirectory;
	private Map<String, String> nameToPathMap = new HashMap<String, String>();

	public RunListProcessor(String sqlDirectory, File runlistFile) throws JAXBException {
		this.sqlDirectory = sqlDirectory;
		
		JAXBContext jaxbContext = JAXBContext.newInstance(RunList.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		runList = (RunList) jaxbUnmarshaller.unmarshal(runlistFile);

		totalScriptCountInRunList = runList.getScript().size();
		mapNameToPath();
	}

	public boolean containsScripts() {
		if (runList == null) {
			return false;
		} else if (totalScriptCountInRunList == 0) {
			return false;
		} else if (nameToPathMap.size() == 0) {
			return false;
		}
			
		return true;
	}
	
	public boolean hasNext() {
		return scriptsExecuted < totalScriptCountInRunList;
	}

	public Script getNextScript() {
		Script script = runList.getScript().get(scriptsExecuted);
		scriptsExecuted++;
		
		return script;
	}

	private void mapNameToPath() {
		mapNameToPath(new File(sqlDirectory));
	}

	private void mapNameToPath(File potentialCSFile) {
		if (potentialCSFile.isDirectory()) {
			if (!potentialCSFile.isHidden() && !potentialCSFile.getName().startsWith(".")) {
				File[] children = potentialCSFile.listFiles();

				for (int i = 0; i < children.length; i++) {
					mapNameToPath(children[i]);
				}
			}
		} else {
			if (!potentialCSFile.isHidden() &&
				!potentialCSFile.getName().startsWith(".") &&
				potentialCSFile.getName().toLowerCase().endsWith(".sql")) {
				nameToPathMap.put(potentialCSFile.getName().toLowerCase(), potentialCSFile.getAbsolutePath().toLowerCase());
			}
		}
	}

	public File getScriptFile(Script script, ExecutionLogger logger) {
		if (!nameToPathMap.containsKey(script.getSqlFile().toLowerCase())) {
			logger.logError("Script \"" + script.getSqlFile() + "\" was never identified by RunListProcessor during recursive inspection at sqlDirectory: " + sqlDirectory);
			return null;
		}
		
		String path = nameToPathMap.get(script.getSqlFile().toLowerCase());

		if (path == null) {
			logger.logError("Script \"" + script.getSqlFile() + "\" doesn't exist at path: " + path);
			return null;
		} else {
			return new File(path);
		}
	}

}
