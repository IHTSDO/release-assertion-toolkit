package org.release.report.component;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.maven.doxia.markup.HtmlMarkup;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkEventAttributeSet;
import org.apache.maven.doxia.sink.SinkEventAttributes;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

import com.mysql.jdbc.Connection;

/**
 * The <codebatchQACheck</code> class iterates through the concepts from a
 * viewpoint and preforms QA
 * 
 * @author termmed
 * @goal assertion-report
 * @phase site
 */
public class ReleaseAssertionWebReport extends AbstractMavenReport {
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
	 * dbConnection JDBC URL
	 * 
	 * @parameter
	 * @required
	 */
	private String url;

	/**
	 * dbConnection JDBC username
	 * 
	 * @parameter
	 * @required
	 */
	private String username;

	/**
	 * dbConnection JDBC password
	 * 
	 * @parameter
	 * @required
	 */
	private String password;

	private Connection con;
	private static final Logger log = Logger.getLogger(ReleaseAssertionWebReport.class);

	@Override
	protected void executeReport(Locale arg0) throws MavenReportException {
		try {
			createConnection();
			log.info("Database Connection Created...");

			String runId = getRunId();

			if (runId != null) {

				createReport(runId);
				log.info("Report Created Successfully...");

				closeConnection();
				log.info("Database Connection Closed...");
			}

		} catch (SQLException sq) {
			log.info(sq.getMessage());
		} catch (Exception e) {
			log.info(e.getMessage());
		}
	}

	private void createReport(String runId) {

		// writeExcel.addHeaderRow("Assertionuuid" + "\t" + "Result" + "\t" +
		// "Count" + "\t" + "Assertiontext");

		try {
			Sink sink = getSink();

			sink.head();
			sink.title();
			sink.text("Release Assertion Report");
			sink.title_();

			SinkEventAttributeSet jsatts = new SinkEventAttributeSet();
			jsatts.addAttribute(SinkEventAttributes.TYPE, "text/javascript");
			jsatts.addAttribute(SinkEventAttributes.SRC, "js/jquery.js");
			sink.unknown("script", new Object[] { new Integer(HtmlMarkup.TAG_TYPE_START) }, jsatts);
			sink.unknown("script", new Object[] { new Integer(HtmlMarkup.TAG_TYPE_END) }, null);

			SinkEventAttributeSet pagerAttr = new SinkEventAttributeSet();
			pagerAttr.addAttribute(SinkEventAttributes.TYPE, "text/javascript");
			pagerAttr.addAttribute(SinkEventAttributes.SRC, "js/jquery.pajinate.js");
			sink.unknown("script", new Object[] { new Integer(HtmlMarkup.TAG_TYPE_START) }, pagerAttr);
			sink.unknown("script", new Object[] { new Integer(HtmlMarkup.TAG_TYPE_END) }, null);

			SinkEventAttributeSet sorterAttr = new SinkEventAttributeSet();
			sorterAttr.addAttribute(SinkEventAttributes.TYPE, "text/javascript");
			sorterAttr.addAttribute(SinkEventAttributes.SRC, "js/tablesort.js");
			sink.unknown("script", new Object[] { new Integer(HtmlMarkup.TAG_TYPE_START) }, sorterAttr);
			sink.unknown("script", new Object[] { new Integer(HtmlMarkup.TAG_TYPE_END) }, null);

			SinkEventAttributeSet atts = new SinkEventAttributeSet();
			atts.addAttribute(SinkEventAttributes.TYPE, "text/javascript");
			atts.addAttribute(SinkEventAttributes.SRC, "js/page.js");
			sink.unknown("script", new Object[] { new Integer(HtmlMarkup.TAG_TYPE_START) }, atts);
			sink.unknown("script", new Object[] { new Integer(HtmlMarkup.TAG_TYPE_END) }, null);

			sink.head_();

			sink.body();

			ResultSet rs = con.createStatement().executeQuery("select a.effectivetime," + " b.assertionuuid ," + " b.assertiontext , b.result , b.count " + " from qa_run a , qa_report b " + " where a.runid = b.runid " + " and a.runid = " + runId);

			List<String> lista = new ArrayList<String>();
			while (rs.next()) {
				// String effectivetime = rs.getString(1);
				String assertionuuid = rs.getString(2);
				String assertiontext = rs.getString(3);
				String result = rs.getString(4);
				String count = rs.getString(5);

				String row = assertionuuid + "," + result + "," + count + "," + assertiontext;
				lista.add(row);
			}

			sink.section1();
			// [5/6/13 3:30:29 PM] Alejandro Lopez Osornio: Last execution:
			// 2013-05-06 03:10:32
			// [5/6/13 3:31:00 PM] Alejandro Lopez Osornio: Result: 32 findings.
			sink.sectionTitle1();
			sink.text("QA run and report");
			sink.sectionTitle1_();

			sink.lineBreak();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sink.definitionList();
			sink.definitionListItem();
			sink.text("Last execution: " + sdf.format(new Date()));
			sink.definitionListItem_();
			sink.definitionList_();
			sink.lineBreak();

			SinkEventAttributes tableAttr = new SinkEventAttributeSet();
			tableAttr.addAttribute(SinkEventAttributes.ID, "results");
			tableAttr.addAttribute(SinkEventAttributes.CLASS,
					"bodyTable sortable-onload-3 no-arrow rowstyle-alt colstyle-alt paginate-20 max-pages-7 paginationcallback-callbackTest-calculateTotalRating paginationcallback-callbackTest-displayTextInfo sortcompletecallback-callbackTest-calculateTotalRating");
			String header = "Assertionuuid" + "\t" + "Result" + "\t" + "Count" + "\t" + "Assertiontext";
			sink.table(tableAttr);
			sink.tableRow();
			String[] rulesHeaderSplited = header.split("\\t", -1);
			SinkEventAttributes headerAttrs = new SinkEventAttributeSet();
			for (int i = 0; i < rulesHeaderSplited.length; i++) {
				headerAttrs.addAttribute(SinkEventAttributes.CLASS, "sortable-text fd-column-" + (i));
				sink.tableHeaderCell(headerAttrs);
				sink.text(rulesHeaderSplited[i]);
				sink.tableHeaderCell_();
			}
			sink.tableRow_();

			for (String string : lista) {
				sink.tableRow();
				String[] splited = string.split(",");
				for (int i = 0; i < splited.length; i++) {
					sink.tableCell();
					sink.text(splited[i]);
					sink.tableCell_();
				}
				sink.tableRow_();
			}
			sink.table_();
			sink.section1_();
			sink.body_();
			sink.flush();
			sink.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getRunId() {
		int runId = 0;
		try {
			ResultSet rs = con.createStatement().executeQuery("select max(runid) from qa_run");

			while (rs.next()) {
				runId = rs.getInt(1);
				log.info("RunId : " + runId);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Integer.toString(runId);
	}

	private void createConnection() throws ClassNotFoundException, SQLException {
		// create database connection
		Class.forName("com.mysql.jdbc.Driver");

		if (con == null) {
			con = (com.mysql.jdbc.Connection) DriverManager.getConnection(url, username, password);
		}

	}

	private void closeConnection() throws SQLException {
		con.close();
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
		return ResourceBundle.getBundle("assertion-report");
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
