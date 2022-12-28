package up.visulog.analyzer;

import java.lang.module.Configuration;

import up.visulog.webgen.WebGen;

/**
 * <b>AnalyzerPlugin</b> is an interface which represents the plugin
 * All the plugins implement this interface
 */
public interface AnalyzerPlugin extends Runnable{
	
	/**
	 * This is where the plugin result is stored
	 */
    interface Result {
    	/**
    	 * @return the result of the plugin as a <b>String</b>
    	 */
        String getResultAsString();
        
        /**
         * This is the old function to generate the html
         * @return the result of the plugin as a <b>String</b> in html format
         * @deprecated
         */
        @Deprecated
        String getResultAsHtmlDiv();
        
        /**
         * This method uses the <b>WebGen</b> object to generate the html
         * @param wg is an object that generate the html
         */
        void getResultAsHtmlDiv(WebGen wg);
    }
    
    /**
     * Computes the result for the git project specified in configuration.
     */
    void run();
    
    /**
     * Computes the result if it has not already been done, and returns it.
     * @return the result
     */
    Result getResult();
}
