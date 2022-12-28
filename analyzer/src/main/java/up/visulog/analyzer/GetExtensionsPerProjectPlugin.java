package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.*;
import up.visulog.webgen.WebGen;

import java.util.*;

/**
 * Counts the percentage of each file extension of a GitLab project.
 */
public class GetExtensionsPerProjectPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    /**
     * Constructor
     * @param generalConfiguration stores the id and private token of the GitLab project to analyze
     */
    public GetExtensionsPerProjectPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    /**
     * Generates a result object in order to store the percentages of the extensions.
     * @param extensionsLog a HashMap which links the name of the extensions to their percentage
     * @return a Result object which contains a HashMap which links the name of the extensions to their percentage
     */
    static Result processLog(HashMap<String,Double> extensionsLog) {
            Result result = new Result();
            result.extensionsResult = extensionsLog;
            return result;
    }

    @Override
    public void run() {
        //Create an api to get extensions
        Extensions apiExtensions = new Extensions();
        //Get results of extensions
        HashMap<String,Double> resExtensions =  apiExtensions.parseExtensionsFromLog(configuration.getPrivateToken(),configuration.getIdProject());
        result = processLog(resExtensions);
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    /**
     * Stores the percentage of each extension, and manages how this data is outputted.
     */
    static class Result implements AnalyzerPlugin.Result {
        private HashMap<String, Double> extensionsResult  = new HashMap<>();

        /**
         * Returns the result of the plugin
         * @return the result of the plugin
         */
        HashMap<String, Double> getExtensionsResult() {
            return extensionsResult;
        }

        @Override
        public String getResultAsString() {
            return extensionsResult.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Extensions in project: <ul>");
            for (var item : extensionsResult.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
        
        @Override
        public void getResultAsHtmlDiv(WebGen wg) {
            ArrayList<String> extension = new ArrayList<String>();
            ArrayList<Double> percentage = new ArrayList<Double>();

            for(var item : extensionsResult.entrySet()) {
                extension.add(item.getKey());
                percentage.add(item.getValue());
            }

            wg.addChartDouble("pie", "Percentage of each extension", "Percentage of each extension", extension, percentage);
        }
    }
}
