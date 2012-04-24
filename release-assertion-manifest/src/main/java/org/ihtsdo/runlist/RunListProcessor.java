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
	private int totalScriptCountInRunList = 0;
	private int scriptsExecuted = 0;
	private Map<String, String> nameToPathMap = new HashMap<String, String>();

	public RunListProcessor(String sqlDirectory, File runlistFile, ExecutionLogger logger) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(RunList.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		runList = (RunList) jaxbUnmarshaller.unmarshal(runlistFile);

		logger.logInfo("Inspecting sqlDirectory: " + sqlDirectory + " for defined scripts");
		
		if (runList != null) {
			totalScriptCountInRunList = runList.getScript().size();
			int mapsFound = mapNameToPath(sqlDirectory);
			
			if (mapsFound == 0) {
				totalScriptCountInRunList = 0;
				logger.logError("SqlDirectory: " + sqlDirectory + " does not contain any sql scripts");
			}
		}
	}
	
	public boolean hasNext() {
		return scriptsExecuted < totalScriptCountInRunList;
	}

	public Script getNextScript() {
		Script script = runList.getScript().get(scriptsExecuted);
		scriptsExecuted++;
		
		return script;
	}

	private int mapNameToPath(String sqlDirectory) {
		mapNameToPath(new File(sqlDirectory));
		
		return nameToPathMap.size();
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
			logger.logError("Script \"" + script.getSqlFile() + "\" was never identified by RunListProcessor during recursive inspection");
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
