package BirtIntegration.BirtViewer;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
/**
 * This class is responsible for processing the reports and rendering or downloading the reports.
 * It handles the request for rendering the report.
 * The method processReportDesignDocAndRenderReport writes the content in response output stream.
 * 
 */
public class ReportRenderer  {


	public static final String PARAM_ISNULL = "__isnull";
	public static final String UTF_8_ENCODE = "UTF-8"; 

	private IReportEngine birtEngine;
	private String reportNameRequestParameter = "ReportName" ; 
	private String reportFormatRequestParameter = "ReportFormat"; 
	private IRenderOption renderOptions ;
	
	//private static final Logger logger = Logger.getLogger(BirtView.class);

	public void setRenderOptions(IRenderOption ro) { 
		this.renderOptions = ro;
	} 

	
	/**
	 * This is overridden method which responsible for processing report  i.e .rptdesign document.and also rendering the report.
	 * <br>It also handles downloading the report.
	 * 
	 */
	
	protected void processReportDesignDocAndRenderReport(HttpServletRequest request,
			HttpServletResponse response) {
		
		try{
			
		//get report name from request object. 
		String reportName = (String)request.getAttribute( this.reportNameRequestParameter );
		
		//logger.info("Processing report:"+reportName);
		
		//get format in which we are going to render report i.e:html,pdf,excel
		String format = (String)request.getAttribute( this.reportFormatRequestParameter );
		
		//give the download report Name here.
		String downloadFileName = "MyReport";
		
		//Base URL
		String baseUrl = request.getScheme() + "://"  + request.getServerName() + ":"  + request.getServerPort()+request.getContextPath();
//		
		ServletContext sc = request.getSession().getServletContext();
		if( format == null ){
			format="html";//default format
		}
		
		
		IReportRunnable runnable = null;

		//opend design document
		runnable = birtEngine.openReportDesign("C://Users//Md.Atequer//git//Tabulation-Sheet-Management-System//TabulationSheetMS//WebContent//WEB-INF//Reports//"+reportName );
		
		//first process the report using Iruntask which will create the temp.rptdocument 
		IRunTask iRunTask = birtEngine.createRunTask(runnable);
	
		iRunTask.getAppContext().put( EngineConstants.APPCONTEXT_BIRT_VIEWER_HTTPSERVET_REQUEST, request );
		
		//put the parameter values from request to the report parameter
		iRunTask.setParameterValues(discoverAndSetParameters( runnable, request ));
		
		//create temp rpddocument
		iRunTask.run(sc.getRealPath("/Reports")+"/temp.rptdocument");
		iRunTask.close();
		
		
		//now do the rendering operation
		IReportDocument reportDoc = birtEngine.openReportDocument( sc.getRealPath("/Reports")+"/temp.rptdocument" );
		IRenderTask iRenderTask= birtEngine.createRenderTask(reportDoc);
		
		//set the format 
		response.setContentType( birtEngine.getMIMEType( format ));
		IRenderOption options =  null == this.renderOptions ? new RenderOption() : this.renderOptions;

		//if html set html related options 
		if( format.equalsIgnoreCase("html")){
			
			HTMLRenderOption htmlOptions = new HTMLRenderOption( options);
			htmlOptions.setOutputFormat("html");
			htmlOptions.setOutputStream(response.getOutputStream());
			htmlOptions.setImageHandler(new HTMLServerImageHandler());
			htmlOptions.setHtmlPagination(true);
			htmlOptions.setBaseImageURL(baseUrl+"/images");//TODO:Change from local host to actual path
			htmlOptions.setImageDirectory(sc.getRealPath("/images"));
			htmlOptions.setSupportedImageFormats("PNG");
			htmlOptions.setEmbeddable(true);
			
			
			iRenderTask.setRenderOption(htmlOptions);

			
			//if pdf set pdf related downloading options 
		}else if( format.equalsIgnoreCase("pdf") ){
			
			PDFRenderOption pdfOptions = new PDFRenderOption( options );
			pdfOptions.setSupportedImageFormats("PNG;GIF;JPG;BMP");
			
			pdfOptions.setOutputFormat("pdf");
			pdfOptions.setImageHandler(new HTMLServerImageHandler());
			pdfOptions.setBaseURL(baseUrl);
			//pdfOptions.setOutputFileName("my.pdf");
			pdfOptions.setOption(IPDFRenderOption.PAGE_OVERFLOW, IPDFRenderOption.FIT_TO_PAGE_SIZE);
			response.setHeader(	"Content-Disposition", "attachment; filename="+downloadFileName );
			pdfOptions.setOutputStream(response.getOutputStream());
			
			iRenderTask.setRenderOption(pdfOptions);
			
			
		//if XLS set XLS related downloading options 
		}else if(format.equalsIgnoreCase("xls")){
			
			  EXCELRenderOption xlsOptions = new EXCELRenderOption(options); 
			  xlsOptions.setOutputFormat("xls");
			  response.setHeader(	"Content-Disposition", "attachment; filename="+downloadFileName);
			  xlsOptions.setImageHandler(new HTMLServerImageHandler());
			  xlsOptions.setOutputStream(response.getOutputStream());
			  //xlsOptions.setOption(IRenderOption.EMITTER_ID, "org.uguess.birt.report.engine.emitter.xls");
			  xlsOptions.setOption(IRenderOption.EMITTER_ID, "org.eclipse.birt.report.engine.emitter.prototype.excel");
			  iRenderTask.setRenderOption(xlsOptions);
			  
		}else{

			response.setHeader(	"Content-Disposition", "attachment; filename=\"" + downloadFileName + "\"" );
			options.setOutputStream(response.getOutputStream());
			options.setOutputFormat(format);
			iRenderTask.setRenderOption(options);
			
		}

		//render report
		iRenderTask.render();
		//close task and doc
		iRenderTask.close();
		reportDoc.close();
		
		//logger.info("Processing report completed successfully:"+reportName);
		
		}catch (Exception e) {
			
			//logger.error("Exception while proceessing report ",e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Set parameters from request to the report parameter.
	 * 
	 * @param report
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected HashMap discoverAndSetParameters( IReportRunnable report, HttpServletRequest request ) throws Exception{

		HashMap<String, Object> parms = new HashMap<String, Object>();  
		IGetParameterDefinitionTask task = birtEngine.createGetParameterDefinitionTask( report );

		@SuppressWarnings("unchecked")
		Collection<IParameterDefnBase> params = task.getParameterDefns( true );
		Iterator<IParameterDefnBase> iter = params.iterator( );
		while ( iter.hasNext( ) )
		{
			IParameterDefnBase param = (IParameterDefnBase) iter.next( );

			IScalarParameterDefn scalar = (IScalarParameterDefn) param;
			if( request.getParameter(param.getName()) != null && !request.getParameter(param.getName()).trim().equals("")){
				parms.put( param.getName(), getParamValueObject( request, scalar));
			}
		}
		task.close();
		return parms;
	}
	/**
	 * Get parameter value
	 * @param request
	 * @param parameterObj
	 * @return
	 * @throws Exception
	 */
	protected Object getParamValueObject( HttpServletRequest request,
			IScalarParameterDefn parameterObj ) throws Exception
			{
		String paramName = parameterObj.getName( );
		String format = parameterObj.getDisplayFormat( );
		if ( doesReportParameterExist( request, paramName ) )
		{
			ReportParameterConverter converter = new ReportParameterConverter(format, request.getLocale( ) );
			// Get value from http request
			String paramValue = getReportParameter( request,
					paramName, null );
			return converter.parse( paramValue, parameterObj.getDataType( ) );
		}
		return null;
			}
	
	public static String getReportParameter( HttpServletRequest request,
			String name, String defaultValue )
	{
		assert request != null && name != null;

		String value = getParameter( request, name );
		if ( value == null || value.length( ) <= 0 ) // Treat
			// it as blank value.
		{
			value = ""; //$NON-NLS-1$
		}

		Map paramMap = request.getParameterMap( );
		if ( paramMap == null || !paramMap.containsKey( name ) )
		{
			value = defaultValue;
		}

		Set nullParams = getParameterValues( request, PARAM_ISNULL );

		if ( nullParams != null && nullParams.contains( name ) )
		{
			value = null;
		}

		return value;
	}
	
	/**
	 * Check if report parameter exist
	 * @param request
	 * @param name
	 * @return
	 */
	public static boolean doesReportParameterExist( HttpServletRequest request,
			String name )
	{
		assert request != null && name != null;

		boolean isExist = false;

		Map paramMap = request.getParameterMap( );
		if ( paramMap != null )
		{
			isExist = ( paramMap.containsKey( name ) );
		}
		Set nullParams = getParameterValues( request, PARAM_ISNULL );
		if ( nullParams != null && nullParams.contains( name ) )
		{
			isExist = true;
		}

		return isExist;
	}
	/**
	 * Get paramter value from request object
	 * @param request
	 * @param parameterName
	 * @return
	 */
	public static String getParameter( HttpServletRequest request,
			String parameterName )
	{

		if ( request.getCharacterEncoding( ) == null )
		{
			try
			{
				request.setCharacterEncoding( UTF_8_ENCODE );
			}
			catch ( UnsupportedEncodingException e )
			{
			}
		}
		return request.getParameter( parameterName );
	}

	//allows setting parameter values to null using __isnull
	public static Set getParameterValues( HttpServletRequest request,
			String parameterName )
	{
		Set<String> parameterValues = null;
		String[] parameterValuesArray = request.getParameterValues( parameterName );

		if ( parameterValuesArray != null )
		{
			parameterValues = new LinkedHashSet<String>( );

			for ( int i = 0; i < parameterValuesArray.length; i++ )
			{
				parameterValues.add( parameterValuesArray[i] );
			}
		}

		return parameterValues;
	}

	/**
	 * Setter for birtEngine
	 * @param birtEngine
	 */
	public void setBirtEngine(IReportEngine birtEngine) {
		this.birtEngine = birtEngine;
	}
}