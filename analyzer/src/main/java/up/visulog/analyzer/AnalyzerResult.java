package up.visulog.analyzer;

import java.util.List;

import up.visulog.webgen.WebGen;

/**
 * This class stores all the results of the plugins in a List
 */
public class AnalyzerResult {
	/**
	 * Return the list of results
	 * @return the list of results
	 */
    public List<AnalyzerPlugin.Result> getSubResults() {
        return subResults;
    }
    
    /**
     * This is where the results are stored
     */
    private final List<AnalyzerPlugin.Result> subResults;
    
    
    /**
     * @param subResults the list of results that we need to store
     */
    public AnalyzerResult(List<AnalyzerPlugin.Result> subResults) {
        this.subResults = subResults;
    }

    @Override
    public String toString() {
        return subResults.stream().map(AnalyzerPlugin.Result::getResultAsString).reduce("", (acc, cur) -> acc + "\n" + cur);
    }
    
    /**
     * This is the old method to generate html
     * @return all the results as a <b>String</b> in html format
     * @deprecated
     */
    @Deprecated
    public String toHTML() {
        return "<html><body>"+subResults.stream().map(AnalyzerPlugin.Result::getResultAsHtmlDiv).reduce("", (acc,cur) -> acc + cur )+"</body></html>";
    }

    /**
     * This method generates the html thanks to the <b>WebGen</b> object and the list of results
     * @param wg the <b>WebGen</b> object that generate the html
     */
    public void toHTML(WebGen wg){
        for(var result : subResults){
            result.getResultAsHtmlDiv(wg);
        }
    }
}
