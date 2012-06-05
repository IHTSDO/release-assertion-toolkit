package org.ihtsdo.release.fileqa.model;

public class Props {

	private String curRelDate;
	private String releaseName;
	private String prevReleaseDir;
	private String currReleaseDir;
	private String sourceFileDir;
	private String reportName;

	public String getCurRelDate() {
		return curRelDate;
	}

	public String getReleaseName() {
		return releaseName;
	}

	public String getPrevReleaseDir() {
		return prevReleaseDir;
	}

	public String getCurrReleaseDir() {
		return currReleaseDir;
	}

	public String getSourceFileDir() {
		return sourceFileDir;
	}
	
	public String getReportName() {
		return reportName;
	}

	public void setCurRelDate(String curRelDate) {
		this.curRelDate = curRelDate;
	}

	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}

	public void setPrevReleaseDir(String prevReleaseDir) {
		this.prevReleaseDir = prevReleaseDir;
	}

	public void setCurrReleaseDir(String currReleaseDir) {
		this.currReleaseDir = currReleaseDir;
	}

	public void setSourceFileDir(String sourceFileDir) {
		this.sourceFileDir = sourceFileDir;
	}


	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
}
