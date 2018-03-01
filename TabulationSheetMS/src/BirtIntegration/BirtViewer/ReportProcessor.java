package BirtIntegration.BirtViewer;

import java.io.File;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BirtIntegration.factory.BirtEngineFactory;

/**
 * This class is responsible for initlizing birt engine configuring birt engine,and processing and rendering reports.
 *
 */
public class ReportProcessor {

	private BirtEngineFactory birtEngineFactory; 
	private ReportRenderer reportRenderer;
	private static ReportProcessor reportProcessor =null; 
	
	//private constructor for single tone object. 
	private ReportProcessor(){
		
	}
	
	public boolean initilizeBirtEngine(){
		boolean isInitialized =true;
		reportRenderer = new ReportRenderer();
		reportRenderer.setBirtEngine( this.getBitEngineFactory().getEngine() );
		
		System.out.println("Bit Engine Successfully Started.");
		
		return isInitialized;
	}
	
	/**
	 * Annotated with @ bean and will create BirtEngineFactory bean.
	 * @return
	 */
	private BirtEngineFactory getBitEngineFactory(){
		
		birtEngineFactory = BirtEngineFactory.getBirtEngineFactory() ;
		//uncomment to use logging
		//birtEngineFactory.setLogLevel( Level.FINEST);
		//birt engine logs will be created under this directory.
		//currently this line is commented
		//birtEngineFactory.setLogDirectory( new File("E:/WorkSpaces/PracticeWorkspace/BirtIntegration/birtlogs"));
		
	  return birtEngineFactory; 
	}
	

	public void shutDownBirtEngine(){
		birtEngineFactory.destroy();
		
	}
	
	public void processReport(HttpServletRequest request,
	    HttpServletResponse response) {
		reportRenderer.processReportDesignDocAndRenderReport(request, response);
	}
	
	public static ReportProcessor getReportProcessor() {
		if(reportProcessor !=null){
			return reportProcessor;
		}
		reportProcessor = new ReportProcessor();
		return reportProcessor;
	}
	
}
