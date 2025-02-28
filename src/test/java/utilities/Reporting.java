package utilities;
//Listener class used to generate extent reports
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Reporting extends TestListenerAdapter{
	
	public ExtentHtmlReporter htmlReporter;
	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent;
	public ExtentTest logger;
	private String reportPath;
    private String pdfPath;

    @Override
	public void onStart(ITestContext testContext)
		{
			// Generate timestamp-based report name
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());//time stamp
			String repName="Test-Report-"+timeStamp+ ".html";
			reportPath = System.getProperty("user.dir") + "/test-output/" + repName;
			pdfPath = System.getProperty("user.dir") + "/test-output/" + repName.replace(".html", ".pdf");

			
			// // Initialize ExtentReports and attach reporters
			htmlReporter=new ExtentHtmlReporter(reportPath);		
			try {
				htmlReporter.loadXMLConfig(System.getProperty("user.dir")+"/src/test/resources/extent_config.xml");
			} catch (IOException e) {
				System.out.println(e);
			}

			sparkReporter = new ExtentSparkReporter(reportPath);
			extent=new ExtentReports();
			extent.attachReporter(htmlReporter);
			extent.attachReporter(sparkReporter);

		// System information for the report
		extent.setSystemInfo("Host Name", "Localhost");
		extent.setSystemInfo("Environment", "QA");
		extent.setSystemInfo("Tester", "Reka");
		extent.setSystemInfo("OS", "Windows10");
		extent.setSystemInfo("Browser", "Chrome");
		
		// HTML Report Configurations
		htmlReporter.config().setDocumentTitle("Amazon Test Project");//title of report
		htmlReporter.config().setReportName("Functional Test Automation Report");//name of report
		//htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.DARK);

		// Report Configurations
        sparkReporter.config().setDocumentTitle("Amazon Test Project");
        sparkReporter.config().setReportName("Functional Test Report");
		sparkReporter.config().setTheme(Theme.DARK);

/*
		 // **EXPLICITLY ADD PDF REPORTER**
		 ExtentSparkReporter pdfReporter = new ExtentSparkReporter(System.getProperty("user.dir")+"/test-output/" + repName + ".pdf");
		 extent.attachReporter(pdfReporter);
		 pdfReporter.config().setDocumentTitle("Amazon Test Project");//title of report
		 pdfReporter.config().setReportName("Functional Test Automation Report");//name of report
		 //pdfReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		 pdfReporter.config().setTheme(Theme.DARK);
		 pdfReporter.config().setEncoding("utf-8");
		 pdfReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, HH:mm:ss");

		// Convert HTML to PDF
		try {
			HtmlConverter.convertToPdf(new File(reportPath), new FileOutputStream(pdfPath));
			System.out.println("PDF Report Generated: " + pdfPath);
		} catch (IOException e) {
			System.out.println("Error while converting to PDF: " + e.getMessage());
		}
*/
	}
        @Override
		public void onTestSuccess(ITestResult tr)
		{
		logger=extent.createTest(tr.getName());//create new entry in the report
		logger.log(Status.PASS, MarkupHelper.createLabel(tr.getName(), ExtentColor.GREEN));//Send passed info
		}
			@Override
			public void onTestFailure(ITestResult tr)
		{
			logger=extent.createTest(tr.getName());//create new entry in the report
			logger.log(Status.FAIL, MarkupHelper.createLabel(tr.getName(), ExtentColor.RED));
			
			// Capture Screenshot on Failure
			String screenshotPath = System.getProperty("user.dir") + "/Screenshots/" + tr.getName() + ".png";
			//String screenshotPath= System.getProperty("user.dir") + "/Screenshots/";
			File screenshotDir  = new File(screenshotPath);
			//String screenshotPath=System.getProperty("user.dir")+"/Screenshots/"+tr.getName()+".png";  // screenshot with name of test case
			if (!screenshotDir.exists()) {
				screenshotDir.mkdirs(); // Create folder if it doesn’t exist
			}	
		
		if(screenshotDir.exists())
		 {			
			logger.fail("Screenshot is below:"+logger.addScreenCaptureFromPath(screenshotPath));			
		}
	}

        @Override
		public void onTestSkipped(ITestResult tr)
		{
			logger=extent.createTest(tr.getName());
			logger.log(Status.SKIP, MarkupHelper.createLabel(tr.getName(), ExtentColor.ORANGE));
		}
		
        @Override
		public void onFinish(ITestContext testContext)
		{
			extent.flush();
			
		}
}

	/*public void onStart(ITestContext testContext) {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());  // Time Stamp
		String repName="Test-Report-" + timeStamp+".html";
		htmlReporter= new ExtentHtmlReporter(System.getProperty("user.dir")+ "/test-output/"+ repName); //specify location
		try {
			htmlReporter.loadXMLConfig(System.getProperty("user.dir")+"/extent-config.xml");
		} catch (IOException e) {
			
			e.printStackTrace();
		}		
		
		extent=new ExtentReports();
		
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Host name",  "localhost");
		extent.setSystemInfo("Environment","QA");
		extent.setSystemInfo("user"," reka");
		
		htmlReporter.config().setDocumentTitle("nopcommerce test project"); // title of report
		htmlReporter.config().setReportName(" Functional test report");  // name of report
		//htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP); // location of chart
		htmlReporter.config().setTheme(Theme.DARK);
	 }*/