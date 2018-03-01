package BirtIntegration.factory;

import java.io.File;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;


/**
*
*Factory class for the instance of the {@link IReportEngine report engine}.
*Responsible for creating IReportEngine and also destroying the engine. 
*
*/
public class BirtEngineFactory {  

	public boolean isSingleton(){ return true ; } 

 
	private IReportEngine birtEngine ;	
	private File _resolvedDirectory ;
	private java.util.logging.Level logLevel ;
	private static BirtEngineFactory birtEngineFactory;
	
	
	private BirtEngineFactory(){
		
	}
	
	public static BirtEngineFactory getBirtEngineFactory(){
		if(birtEngineFactory == null){
			birtEngineFactory = new BirtEngineFactory();
		}
		return birtEngineFactory;
	}
	
	
	
	/**
	 * Destroy birt engine.
	 * and shut down platform
	 * 
	 * call this method while destroying your application context.
	 */
	public void destroy() {
		
		try{
			
		birtEngine.destroy();
		Platform.shutdown() ;
		System.out.println("Engine successfully destroyed and platform is shutdown!!");
		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set log level
	 * @param ll
	 */
	public void setLogLevel(  java.util.logging.Level  ll){
		this.logLevel = ll ;
	}

	/**
	 * Setting log file.
	 * if log directory is not present.It will be created
	 * @param f
	 */
	public void setLogDirectory ( java.io.File f ){
		
		//TODO: if  puts wrong path please validate that here.
		if(!f.exists()){
			f.mkdirs();
		}
		this._resolvedDirectory = f; 
	}

	/**
	 * Factory method for birt engine.
	 */
	public IReportEngine getEngine(){ 

		if(birtEngine !=null){
			
		}
		
		EngineConfig config = getEngineConfig();
		
		try {
			Platform.startup( config );
		}
		catch ( BirtException e ) {
			e.printStackTrace();
		}

		IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
		IReportEngine be = factory.createReportEngine( config );
		this.birtEngine = be ;
		
		return be ;
	}

	/**
	 * Set  engine configuration like logs and other things as per the need
	 * @return
	 */
	public EngineConfig getEngineConfig(){
		EngineConfig config = new EngineConfig();
		
		//This line injects the Spring Context into the BIRT Context
		//Put what ever you want to put in Birt Context which will be avail thought out the life cycle of birt engine context
		//config.getAppContext().put("struts", springContext );
		
		
		//config.setLogConfig( null != this._resolvedDirectory ? this._resolvedDirectory.getAbsolutePath() : null  , this.logLevel);
		
		//please take care of how many log files are created and with how much size
		//below code some how not taking of it.Check javaDoc of EngineConfig
		
		//config.setLogMaxBackupIndex(5);
		//config.setLogRollingSize(5242880); //5mb
		
		return config;
	}
	
	
	public Class<?> getObjectType() {
		return IReportEngine.class;
	}
}
