package org.ihtsdo.release.fileqa.mojo;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

/**
 * The <codebatchQACheck</code> class iterates through the concepts from a
 * viewpoint and preforms QA
 * 
 * @author termmed
 * @goal assertion-file-qa-report
 * @phase site
 */
public class FileQaWebReport extends AbstractMavenReport {
	/**
	 * The Maven Project Object
	 * 
	 * @parameter default-value="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * @component
	 * @required
	 * @readonly
	 */
	private Renderer siteRenderer;

	/**
	 * Specifies the directory where the report will be generated
	 * 
	 * @parameter default-value="${project.reporting.outputDirectory}"
	 * @required
	 */
	private String outputDirectory;
	
	/**
	 * dbConnection JDBC password
	 * 
	 * @parameter
	 * @required
	 */
	private String excelFileLocation;

	private static final Logger log = Logger.getLogger(FileQaWebReport.class);

	@Override
	protected void executeReport(Locale arg0) throws MavenReportException {
		createReport();
	}

	private void createReport() {
		try {
			Sink sink = getSink();

			sink.head();
			sink.title();
			sink.text("File QA Report");
			sink.title_();
			sink.head_();

			sink.body();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected MavenProject getProject() {
		return project;
	}

	@Override
	protected String getOutputDirectory() {
		return outputDirectory;
	}

	private ResourceBundle getBundle(Locale locale) {
		return ResourceBundle.getBundle("assertion-file-qa-report");
	}

	@Override
	public String getDescription(Locale locale) {
		return getBundle(locale).getString("report.name");
	}

	@Override
	public String getName(Locale locale) {
		return getBundle(locale).getString("report.name");
	}

	@Override
	public String getOutputName() {
		return project.getArtifactId();
	}

	@Override
	protected Renderer getSiteRenderer() {
		return siteRenderer;
	}
}
