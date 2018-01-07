package com.parallel.listner;

import java.util.List;

import org.testng.ISuite;
import org.testng.reporters.EmailableReporter2;
import org.testng.xml.XmlSuite;
import com.relevantcodes.extentreports.ExtentReports;

public class ParallelReporter extends EmailableReporter2 {
	String suiteName;
	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		try {
			super.generateReport(xmlSuites, suites, outputDirectory);
			for(XmlSuite xmlSuite:xmlSuites){
				suiteName=xmlSuite.getName();
			}
			String css = "#topbar { background-color: #8bb1ec; }" +
					".topbar-items-right span { color: white; }" +
					".menu span { color: darkgreen; }" +
					".menu-item-selected span { border-bottom: 1px solid green; }" +
					"#dashboard { background-color: transparent; }" +
					".test { border: 1px solid lightseagreen; }" + 
					".description { background-color: transparent; border-left: 2px solid orange; padding: 2px 15px;}" + 
					".name { color: darkgreen; }" + 
					".extent-table { border: 1px solid #bbb; }" + 
					".extent-table th { background: none repeat scroll 0 0 olivedrab; color: #fff; }" + 
					".extent-table td { border-bottom: 1px solid #bbb; }";

			String style = "p{font-size:20px;}" +
					".test{background-color:#000 !important;color:#fff !important;}";



			ExtentReports extent =ExtentManager.getInstance();
			// optional
			extent.config()
			.documentTitle("Automation Report")
			.reportName(suiteName)
			.reportHeadline("Report")
			.addCustomStylesheet(style)
			.insertJs("var t = $('.tests-quick-view table'); for (var i = 4; i < 8; i++) { t.find('th:nth-child(' + i + '), td:nth-child(' + i + ')').hide(); } t.find('th:nth-child(9), td:nth-child(9)').hide();");
			extent.flush();
			extent.close();
		} catch (Exception e) {
			
		}
	}

}
